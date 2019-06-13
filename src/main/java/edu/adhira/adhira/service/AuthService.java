package edu.adhira.adhira.service;

import edu.adhira.adhira.authentication.User;

public interface AuthService {
    User findByEmail(String email);
    void saveUser(User user);
}
