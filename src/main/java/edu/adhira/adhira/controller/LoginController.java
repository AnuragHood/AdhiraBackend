package edu.adhira.adhira.controller;

import edu.adhira.adhira.authentication.Login;
import edu.adhira.adhira.authentication.User;
import edu.adhira.adhira.service.AuthServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;

import javax.validation.Valid;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
	private static Logger logger = LogManager.getLogger(LoginController.class);

	@Autowired
	AuthServiceImpl userService;

	@PostMapping(value = "/registration")
	@ResponseBody
	public String createNewUser(@RequestBody User user) {
		logger.info("inside Registration post method " + user.getRegistrationMode() + "email:" + user.getEmail());

		if (userService.findByEmail(user.getEmail()) != null || userService.findByPhone(user.getPhone()) != null) {
			logger.info("Email/phone id exist");
			return "0";

		} else {

			return userService.saveUser(user);
		}

	}

	@PostMapping(value = "/login")
	@ResponseBody
	public String login(@RequestBody Login login) {
		logger.info("inside Login post method " + login.getEmail());

		User userExists = userService.findByEmail(login.getEmail());
		if (userExists == null) {
			logger.info("Email does'nt exist");
			return "EmailId does'nt Exist.";

		} else {
			boolean result = userService.login(login);
			logger.info("Login credencials matched");
			if (result) {
				return "Logged-in successfully.";
			} else {
				return "Email and /or password incorrect";
			}
		}

	}

	@GetMapping(value = "/confirm-account")
	@ResponseBody
	public ModelAndView confirmAccount(@RequestParam String token, @RequestParam Long userId)
			throws NumberFormatException, ParseException {
		logger.info("inside confirm-account post method " + token);
		ModelAndView mv = new ModelAndView("home");
		boolean result = userService.findByToken(token, userId.toString());
		if (result) {
			mv.addObject("success", "You have been registered successfully.Please login using below link:");
		} else {
			mv.addObject("failure", "Your registration token expired.Register again.");
		}
		return mv;
	}

	@GetMapping(value = "/confirm-otp")
	@ResponseBody
	public String confirmOtp(@RequestParam String otp, @RequestParam String inputValue)
			throws NumberFormatException, ParseException {
		logger.info("inside confirmOtp post method " + otp);
		int result = userService.verifyOtp(Long.parseLong(inputValue), otp);
		if (result == 1) {
			return "Registration has been completed.Please login.";
		}
		return "Verification failed";

	}

	@GetMapping(value = "/inquire")
	@ResponseBody
	public User inquireEmaiOrMobile(@RequestParam String inquire) {
		logger.info("inside confirmOtp post method " + inquire);
		return userService.searchUser(inquire);
		

	}

}
