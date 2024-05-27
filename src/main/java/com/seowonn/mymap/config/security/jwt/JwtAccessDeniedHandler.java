package com.seowonn.mymap.config.security.jwt;

import static com.seowonn.mymap.global.type.ErrorCode.ACCESS_DENIED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seowonn.mymap.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {
    setResponse(response);
  }

  private void setResponse(HttpServletResponse response)
    throws IOException {
    // HTTP 응답 response 생성
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");
    ApiResponse<?> apiResponse = ApiResponse.createFail(ACCESS_DENIED);
    String jsonResponse = objectMapper.writeValueAsString(apiResponse);

    // JSON 응답 전송
    response.getWriter().write(jsonResponse);
  }
}
