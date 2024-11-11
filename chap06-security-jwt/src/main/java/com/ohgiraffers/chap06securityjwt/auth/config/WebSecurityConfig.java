package com.ohgiraffers.chap06securityjwt.auth.config;

import com.ohgiraffers.chap06securityjwt.auth.filter.CustomAuthenticationFilter;
import com.ohgiraffers.chap06securityjwt.auth.filter.JwtAuthorizationFilter;
import com.ohgiraffers.chap06securityjwt.auth.handler.CustomAuthFailureHandler;
import com.ohgiraffers.chap06securityjwt.auth.handler.CustomAuthSuccessHandler;
import com.ohgiraffers.chap06securityjwt.auth.handler.CustomAuthenticationProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    /**
     *  1. 정적 자원에 대한 인증된 사용자의 접근을 설정하는 메소드
     * @return WebSecurityCustomizer
     * */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().
                requestMatchers(PathRequest.
                        toStaticResources().atCommonLocations());
    }

    /**
     *  2. 비밀번호 암호화 인코더
     * @return BCryptPasswordEncoder
     * */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     *  3. 사용자의 아이디와 패스워드를 DB 검증하는 핸들러
     * @return CustomAuthenticationProvider
     * */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(){
        return new CustomAuthenticationProvider();
    }
    /**
    *  4. Authentication 의 인증 메소드를 제공하는 매니저로 Provider의 인터페이스
     * @return AuthenticationManager
    * */
    @Bean
    public AuthenticationManager authenticationManager(){
        // 시큐리티에서 사용자 인증을 관리하는 핵심 객체
        return new ProviderManager(customAuthenticationProvider());
        // ProviderManager 는 AuthenticationManager 를 구현한 클래스.
        // 여러 AuthenticationProvider 를 이용해 인증을 처리함.
    }
    /**
     * 5. 사용자의 인증 요청을 가로채서 로그인 로직을 수행하는 필터
     *
     * @return CustomAuthenticationFilter
     * */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(){
        CustomAuthenticationFilter authenticationFilter
                = new CustomAuthenticationFilter(authenticationManager());
        
        authenticationFilter.setFilterProcessesUrl("/login"); // 어떤 요청을 가로챌지
        authenticationFilter.setAuthenticationSuccessHandler(customAuthSuccessHandler()); // 요청 성공 동작
        authenticationFilter.setAuthenticationFailureHandler(customAuthFailureHandler()); //  요청 실패 동작
        return authenticationFilter;
        
    }

    /**
     *  6. spring security 기반의 사용자의 정보가 맞을 경우 결과를 수행하는 handler
     * */
    @Bean
    public CustomAuthSuccessHandler customAuthSuccessHandler(){
        return new CustomAuthSuccessHandler();
    }

    /**
    *  7. spring security 기반의 사용자의 정보가 맞지 않을 경우 수행하는 handler
     * */
    @Bean
    public CustomAuthFailureHandler customAuthFailureHandler(){
        return new CustomAuthFailureHandler();
    }

    /**
     * 8. 사용자의 요청 시 수행되는 메소드
     * */
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter(authenticationManager());
    }
    
    
    /**
     *  security filter chain 설정
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // csrf 보호 비활성화 -> 세션 기반 인증 시 가능한 공격. 토큰방식에서는 의미없음
        http.csrf(csrf -> csrf.disable())
                // JWT 토큰을 사용하는 경우 기본 HTTP 인증 대신 JWT 인증 필터를 사용하도록 지정
                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
                // 세션을 유지하지 않도록 설정. 세션 관리 설정을 빼줌
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 기본 제공 로그인 폼 비활성화
                .formLogin(form -> form.disable())
                // 기본 제공 UsernamePasswordAuthenticationFilter 대신 우리가 만든것 쓰도록
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // HTTP basic 인증(사용자 이름과 비밀번호를 기본 헤더로 보내는 방식) 비활성화
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
