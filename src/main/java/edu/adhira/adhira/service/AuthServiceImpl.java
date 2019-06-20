package edu.adhira.adhira.service;

import edu.adhira.adhira.WebMvcConfig;
import edu.adhira.adhira.authentication.EmailToken;
import edu.adhira.adhira.authentication.Login;
import edu.adhira.adhira.authentication.Role;
import edu.adhira.adhira.authentication.User;
import edu.adhira.adhira.repository.EmailTokenRepo;
import edu.adhira.adhira.repository.RoleRepository;
import edu.adhira.adhira.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

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

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public int compPassword(Login login) {
        String result = userRepo.compPassword(login.getEmail());


        if (login.getPassword() != null & web.passwordEncoder().matches(login.getPassword(), result)) {

            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String saveUser(User user) {
        user.setPassword(web.passwordEncoder().encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepo.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepo.save(user);
        boolean result = sendVerificationMail(user);

        if (result) {

            logger.info("registered successefully");
            return "You have been registered successfully!!";
        } else {
            logger.info("problem in email verification");
            return "Registration failed";
        }
    }

    @Override
    public boolean login(Login login) {
        if (compPassword(login) == 1) {
            logger.info("returning true");
            return true;
        }
        return false;

    }

    public boolean sendVerificationMail(User user) {
        try {
            EmailToken confirmationToken = new EmailToken(user);

            tokenRepo.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("sn.anurag.pathak@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/confirm-account?token=");

            emailSenderService.sendEmail(mailMessage);
            return true;
        } catch (Exception e) {
            logger.info("Problem in email verification" + e.getMessage());
            return false;
        }
    }
}
