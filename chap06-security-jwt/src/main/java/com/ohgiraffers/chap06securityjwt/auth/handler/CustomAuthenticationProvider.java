package com.ohgiraffers.chap06securityjwt.auth.handler;

import com.ohgiraffers.chap06securityjwt.auth.model.DetailsUser;
import com.ohgiraffers.chap06securityjwt.auth.service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private DetailsService detailsService; // 사용자 세부 정보 서비스를 주입 

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // matches 를 위한 주입


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. 로그인 요청에서 전달된 인증 토큰을 가져온다.
        UsernamePasswordAuthenticationToken loginToken =
                (UsernamePasswordAuthenticationToken) authentication;
        String username = loginToken.getName();
        String password = (String) loginToken.getCredentials();
        System.out.println(loginToken);
        // 2. DB에서 username에 해당하는 정보 조회
        DetailsUser foundUser =
                (DetailsUser) detailsService.loadUserByUsername(username);
        if(!passwordEncoder.matches(password,foundUser.getPassword())){
            throw new BadCredentialsException("password가 일치하지 않습니다.");
        }
        return new UsernamePasswordAuthenticationToken(foundUser,password,foundUser.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 어떤 타입의 인증을 처리할 수 있는지 확인
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
