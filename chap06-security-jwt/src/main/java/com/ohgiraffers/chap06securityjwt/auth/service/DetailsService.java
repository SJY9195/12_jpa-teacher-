package com.ohgiraffers.chap06securityjwt.auth.service;


import com.ohgiraffers.chap06securityjwt.auth.model.DetailsUser;
import com.ohgiraffers.chap06securityjwt.user.entity.User;
import com.ohgiraffers.chap06securityjwt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 유효성 검사
        if(username == null || username.isEmpty()){
            throw new AuthenticationServiceException(username +" is Empty");
        }
        // db 에서 username에 해당하는 정보를 꺼내옴
        Optional<User> user = userService.findUser(username);

        // 사용자가 존재하는 경우 DetailsUser 객체 반환
        if(user.isPresent()){
            return new DetailsUser(user);
        }else{
            // 사용자가 존재하지 않는 경우 예외 발생
            throw new AuthenticationServiceException(username);
        }
    }
}
