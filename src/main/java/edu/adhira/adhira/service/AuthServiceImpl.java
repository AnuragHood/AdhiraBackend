package edu.adhira.adhira.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import javax.transaction.Transactional;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

import edu.adhira.adhira.WebMvcConfig;
import edu.adhira.adhira.authentication.EmailToken;
import edu.adhira.adhira.authentication.Login;
import edu.adhira.adhira.authentication.MobileOtp;
import edu.adhira.adhira.authentication.Role;
import edu.adhira.adhira.authentication.User;
import edu.adhira.adhira.repository.EmailTokenRepo;
import edu.adhira.adhira.repository.MobileAuthRepository;
import edu.adhira.adhira.repository.RoleRepository;
import edu.adhira.adhira.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {
	private static Logger logger = LogManager.getLogger(AuthServiceImpl.class);
	@Autowired
	WebMvcConfig web;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	EmailTokenRepo tokenRepo;

	@Autowired
	MobileAuthRepository mobileRepo;
	private static boolean result;
	// Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "ACa74d1a5d314a5e3af13a697419a7a79c";
	public static final String AUTH_TOKEN = "a06a167848db8b336490065a3d53e64f";
	public static final String TWILIO_NUMBER = "+15712487795";

	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Transactional
	public int verifyOtp(long userId, String otp) {
		String otpFromDb = mobileRepo.verifyOtp(userId);
		if (otpFromDb.equals(otp)) {
			userRepo.setActiveInactiveUser('Y', (int) userId);
			logger.info("settting  active user done::" + userId);
			return 1;
		}
		return 0;
	}

	public int compPassword(Login login) {
		String result = userRepo.compPassword(login.getEmail());

		if (login.getPassword() != null & web.passwordEncoder().matches(login.getPassword(), result)) {

			return 1;
		} else {
			return 0;
		}
	}

	@Transactional
	public String saveUser(User user) {
		try {
			if (userRepo.findByPhone(user.getPhone()) == null) {
				user.setPassword(web.passwordEncoder().encode(user.getPassword()));
				user.setActive('N');
				Role userRole = roleRepo.findByRole("USER");
				user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

				userRepo.save(user);
				if (user.getRegistrationMode().equals("email")) {
					result = sendVerificationMail(user);
				} else {
					if (userRepo.findByPhone(user.getPhone()) != null) {
						String random = getRandomNumber();
						saveOtp(new MobileOtp(user, random));
						sendSMS(user, random);
						return user.getId() + "";
					} else {
						return null;
					}
				}

				if (result && user.getRegistrationMode().equals("email")) {

					logger.info("registered successefully");
					return "You have been registered successfully..check your mail to varify!!";
				} else {

					return user.getId() + "";
				}
			} else {
				if (mobileRepo.getTokenCreationDate(user.getId()) != null) {
					mobileRepo.deleteToken(user.getId());
				}
				String random = getRandomNumber();
				saveOtp(new MobileOtp(user, random));
				sendSMS(user, random);
				return user.getId() + "";
			}
		} catch (Exception e) {
			logger.info("Error in verification options:" + e.getMessage());
			return "Save user process failed";
		}

	}

	public boolean login(Login login) {
		if (compPassword(login) == 1) {
			if (userRepo.findByEmail(login.getEmail()).getActive() == 'Y')
				return true;
		}
		return false;

	}

	@Transactional
	public boolean sendVerificationMail(User user) {
		try {
			EmailToken confirmationToken = new EmailToken(user);
			if (tokenRepo.getTokenCreationDate(user.getId()) != null) {
				tokenRepo.deleteToken(user.getId());
			}
			tokenRepo.save(confirmationToken);

			SimpleMailMessage mailMessage = new SimpleMailMessage();

			mailMessage.setTo(user.getEmail());
			mailMessage.setSubject("Welcome to Adhira..Complete your  Registration!");
			mailMessage.setFrom("Adhira!!");
			mailMessage.setText("To confirm your account, please click below link : "
					+ "<a href = http://localhost:8080/adhira/confirm-account?token="
					+ confirmationToken.getConfirmationToken() + "&userId=" + user.getId() + ">"
					+ "Verify your account!!</a>");

			emailSenderService.sendEmail(mailMessage);
			return true;
		} catch (Exception e) {
			logger.info("Problem in email verification " + e.getMessage());
			return false;
		}
	}

	@Transactional
	public boolean findByToken(String token, String userId) throws NumberFormatException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dbDate = sdf.format(tokenRepo.getTokenCreationDate(Integer.parseInt(userId)));
		String dateToday = sdf.format(new Date());

		if (dbDate.equals(dateToday)) {

			if (tokenRepo.findByConfirmationToken(token) != null)
				logger.info("setting active user::" + userId);
			userRepo.setActiveInactiveUser('Y', Integer.parseInt(userId));
			logger.info("settting  active user done::" + userId);
			return true;
		} else {
			return false;
		}

	}

	public void sendSMS(User user, String random) {
		try {
			TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

			// Build a filter for the MessageList
			java.util.List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Body",
					"Hello, " + user.getName() + " use this otp to register to Adhira!: " + random));
			params.add(new BasicNameValuePair("To", "+91" + user.getPhone())); // Add real number here
			params.add(new BasicNameValuePair("From", TWILIO_NUMBER));

			MessageFactory messageFactory = client.getAccount().getMessageFactory();
			Message message = messageFactory.create(params);
			logger.info(message.getBody());
		} catch (TwilioRestException e) {
			logger.info(e.getErrorMessage());
		}
	}

	private String getRandomNumber() {
		Random rand = new Random();
		String randomNumber = String.format("%d", (Object) Integer.valueOf(rand.nextInt(8452)));
		return randomNumber;
	}

	public void saveOtp(MobileOtp mobileAuth) {
		mobileRepo.save(mobileAuth);

	}

	@Override
	public User findByPhone(String phone) {

		return userRepo.findByPhone(phone);
	}

	public User searchUser(String query) {
		return userRepo.searchUser( query, query, query);
	}
}
