package edu.adhira.adhira.service;

import edu.adhira.adhira.authentication.Login;
import edu.adhira.adhira.authentication.User;

public interface AuthService {
    User findByEmail(String email);
    String saveUser(User user);
    boolean login(Login login);
}
