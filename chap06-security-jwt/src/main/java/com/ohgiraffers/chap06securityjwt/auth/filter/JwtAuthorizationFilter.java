package com.ohgiraffers.chap06securityjwt.auth.filter;

import com.ohgiraffers.chap06securityjwt.auth.model.DetailsUser;
import com.ohgiraffers.chap06securityjwt.common.AuthConstants;
import com.ohgiraffers.chap06securityjwt.common.util.TokenUtils;
import com.ohgiraffers.chap06securityjwt.user.entity.User;
import com.ohgiraffers.chap06securityjwt.user.model.OhgiraffersRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 권한이 필요 없는 URL 리스트
        List<String> roleLessList = Arrays.asList(
                "/signup"
        );

        // 요청된 URI 가 권한이 필요 없는 목록에 포함되어 있으면 ,필터 통과
        if(roleLessList.contains(request.getRequestURI())){
            chain.doFilter(request,response);
            return;
        }
        // 권한이 필요하면 여기부터 실행
        String header = request.getHeader(AuthConstants.AUTH_HEADER);

        try {
            if(header != null && !header.isEmpty()){
                String token = TokenUtils.splitHeader(header);
                // 토큰이 유효한지
                if(TokenUtils.isValidtoken(token)){
                    // 토큰이 유효하면 토큰의 클레임 정보를 가져옴
                    Claims claims = TokenUtils.getClaimsFromToken(token);
                    // 인증 정보 객체 생성
                    DetailsUser authentication = new DetailsUser();
                    // 사용자 정보를 담기 위한 User 객체
                    User user = new User();
                    // 클레임에서 값을 가져와 담아줌
                    user.setUserId(claims.get("userId").toString());
                    // 역할을 가져와 Enum 으로 변환
                    user.setRole(OhgiraffersRole.valueOf(claims.get("role").toString()));
                    // DetailsUser 객체에 사용자 정보 저장
                    authentication.setUser(user);

                    // UsernamePasswordAuthenticationToken 생성
                    AbstractAuthenticationToken authenticationToken =
                            UsernamePasswordAuthenticationToken.authenticated(authentication, token, authentication.getAuthorities());
                    // 인증 정보를 포함시켜 SecurityContextHolder에 설정
                    // 인증된 토큰 정보를 저장해 이후 다른 보안 필터나 서비스에서 인증 정보를 활용할 수 있음.
                    authenticationToken.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    chain.doFilter(request,response);
                }else{
                    throw new RuntimeException("토큰이 유효하지 않습니다.");
                }
            }else{
                throw new RuntimeException("토큰이 존재하지 않습니다.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
