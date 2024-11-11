package com.ohgiraffers.chap06securityjwt.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더 추가를 위해 HttpServletResponse 로 캐스팅
        HttpServletResponse res = (HttpServletResponse) response;
        // 외부 요청에 대한 접근을 허용하는 설정
        // "*" 은 모든 도메인에서 요청을 허용한다는 의미
        res.setHeader("Access-Control-Allow-Origin","*");
        // 요청할 수 있는 HTTP 메소드 종류를 설정
        res.setHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE");
        // 외부 리소스 요청에 대해 얼마나 오랫동안 응답을 캐싱할지를 설정
        // 캐싱 - 자주 사용되는 데이터를 임시 저장하여 같은 데이터를 다시 요청할 때 더 빠르게 제공하는 기술
        // 3600초 - 1시간 동안 캐시 허용
        res.setHeader("Access-Control-Max-Age", "3600");

        // 헤더에 어떤 값이 포함될 지 허용하는 설정
        res.setHeader("Access-Control-Allow-Headers",
                "X-Requested-With, Content-Type" +
                ", Authorization, X-XSRF-token");
        /*
        *  X-requested-with : 클라이언트가 AJAX 등 비동기 요청을 보낼 때 사용하는 헤더
        *  Content-type : 요청 본문에 담기는 데이터의 타입을 명시
        *  Authorization : 서버에 요청할 때 인증 정보를 포함하는 헤더
        *  X-XSRF-token : 보안 설정
        * */
        // 서버가 인증 정보를 포함하지 않도록 설정
        // 이 설정이 false 로 되어 있으면 쿠키는 자동으로 포함되지 않는다.
        // 보안상의 이유로 인증 정보를 직접 포함시키는 것 외에는 포함되지 않게 설정
        res.setHeader("Access-Control-Allow-Credentials", "false");
        chain.doFilter(request, response);
        
    }
}
