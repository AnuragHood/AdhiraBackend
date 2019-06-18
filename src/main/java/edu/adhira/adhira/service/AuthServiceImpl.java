package edu.adhira.adhira.service;

import edu.adhira.adhira.WebMvcConfig;
import edu.adhira.adhira.authentication.Login;
import edu.adhira.adhira.authentication.Role;
import edu.adhira.adhira.authentication.User;
import edu.adhira.adhira.repository.RoleRepository;
import edu.adhira.adhira.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public int compPassword(Login login) {
        String result = userRepo.compPassword(login.getEmail());
        logger.info("Passwoprd for db query "+result);
        logger.info("Passwoprd for login "+web.passwordEncoder().encode(login.getPassword()));

        if (login.getPassword() != null & web.passwordEncoder().matches(login.getPassword(),result)) {
            logger.info("Passwoprd  encoded "+web.passwordEncoder().encode(login.getPassword()));
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(web.passwordEncoder().encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepo.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepo.save(user);
    }

    @Override
    public boolean login(Login login) {
        if (compPassword(login) == 1) {
            logger.info("returning true");
            return true;
        }
        return false;

    }
}
