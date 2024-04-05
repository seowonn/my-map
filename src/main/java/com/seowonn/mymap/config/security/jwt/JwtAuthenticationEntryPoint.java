package com.seowonn.mymap.config.security.jwt;

import static com.seowonn.mymap.type.ErrorCode.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seowonn.mymap.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {

    log.debug("[JwtAuthenticationEntryPoint] : 인증 실패");

    // HTTP 응답 response 생성
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");
    ApiResponse<?> apiResponse = ApiResponse.createFail(UNAUTHORIZED);
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writeValueAsString(apiResponse);

    // JSON 응답 전송
    response.getWriter().write(jsonResponse);
  }
}
