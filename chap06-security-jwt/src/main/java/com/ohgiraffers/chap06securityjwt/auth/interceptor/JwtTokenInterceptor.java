package com.ohgiraffers.chap06securityjwt.auth.interceptor;

import com.ohgiraffers.chap06securityjwt.common.AuthConstants;
import com.ohgiraffers.chap06securityjwt.common.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.rmi.RemoteException;

public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 요청 헤더에서 Authorization 헤더 값 가져옴
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        // 토큰 추출
        String token = TokenUtils.splitHeader(header);

        if(token != null){
            if(TokenUtils.isValidtoken(token)){
                return true;
            }else{
                throw new RemoteException("token 만료");
            }
        }else{
            throw new RemoteException("token 정보가 없습니다.");
        }

    }
}
