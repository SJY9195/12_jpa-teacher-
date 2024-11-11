package com.ohgiraffers.chap06securityjwt.auth.handler;

import com.ohgiraffers.chap06securityjwt.auth.model.DetailsUser;
import com.ohgiraffers.chap06securityjwt.common.AuthConstants;
import com.ohgiraffers.chap06securityjwt.common.util.ConvertUtil;
import com.ohgiraffers.chap06securityjwt.common.util.TokenUtils;
import com.ohgiraffers.chap06securityjwt.user.entity.User;
import jakarta.persistence.Convert;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User user = ((DetailsUser)authentication.getPrincipal()).getUser();

        // user 객체를 JSON 형식으로 변환
        JSONObject jsonValue = (JSONObject) ConvertUtil.convertObjectToJsonObject(user);
        HashMap<String,Object> responseMap = new HashMap<>();

        if(user.getState().equals("N")){
            responseMap.put("userInfo",jsonValue);
            responseMap.put("message","휴면 상태인 계정입니다.");
        }else{
            // 정상 계정인 경우 JWT 토큰을 포함해 응답에 포함시킴
            String token = TokenUtils.generateJwtToken(user);
            responseMap.put("userInfo", jsonValue);
            responseMap.put("message","로그인 성공");
            // 생성된 토큰을 HTTP 응답 헤더에 포함시킴
            response.addHeader(AuthConstants.AUTH_HEADER,
                    AuthConstants.TOKEN_TYPE+" "+ token);
            JSONObject jsonObject = new JSONObject(responseMap);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            // 클라이언트로 응답을 보내기 위한 객체
            PrintWriter printWriter = response.getWriter();
            printWriter.println(jsonObject);
            // 데이터를 실제 클라이언트로 전송..
            printWriter.flush();
            printWriter.close();
        }


    }
}
