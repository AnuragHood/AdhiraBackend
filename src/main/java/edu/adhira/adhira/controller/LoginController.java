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
    logger.info("inside Registration get method " +user.toString());
        User userExists = userService.findByEmail(user.getEmail());
        if (userExists != null) {
            logger.info("Email id exist");
            return "EmailId Already Exist.";

        } else {
            userService.saveUser(user);
            logger.info("registered successfully");
            return "You have been registered successfully";
        }

    }
    @PostMapping(value = "/login")
    @ResponseBody
    public String login(@RequestBody Login login){
        logger.info("inside Login get method " +login.getEmail());

        User userExists = userService.findByEmail(login.getEmail());
        if (userExists == null) {
            logger.info("Email does'nt exist");
            return "EmailId does'nt Exist.";

        } else {
            boolean result = userService.login(login);
            logger.info("Login credencials matched");
           if(result) { return "Logged-in successfully.";}
           else {return "Email and /or password incorrect";}
        }

    }

}
