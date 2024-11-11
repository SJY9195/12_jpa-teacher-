package com.ohgiraffers.chap06securityjwt.auth.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.chap06securityjwt.auth.model.dto.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    // 지정된 url 요청 시 해당 요청을 가로채서 검증 로직을 수행하는 메소드 - 우리는 login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken;
        // 사용자가 입력한 사용자 아이디와 비밀번호를 담을 토큰

        try {
            authenticationToken = getAuthRequest(request);
            setDetails(request,authenticationToken);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
    
    /**
     * 사용자의 로그인 리소스 요청 시 요청 정보를 임시 토큰에 저장하는 메소드
     * */
    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // json 형식으로 파싱 후 리소스를 자동으로 닫도록 설정 - 성능 최적화 (필수 아님)
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

        // HTTP 요청 본문에서 JSON 데이터를 추출하여 객체로 매핑
        LoginDTO user = objectMapper.readValue(request.getInputStream(), LoginDTO.class);

        return new UsernamePasswordAuthenticationToken(user.getId(), user.getPass());
    }
    
}
