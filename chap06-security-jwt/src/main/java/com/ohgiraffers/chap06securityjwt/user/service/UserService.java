package com.ohgiraffers.chap06securityjwt.user.service;

import com.ohgiraffers.chap06securityjwt.user.entity.User;
import com.ohgiraffers.chap06securityjwt.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;


    public User signup(User user) {

        user.setUserPass(encoder.encode(user.getUserPass()));
        user.setState("Y");

        User signup = userRepository.save(user);

        return signup;
    }

    public Optional<User> findUser(String username) {
        Optional<User> user = userRepository.findByUserId(username);

        return user;
    }
}
