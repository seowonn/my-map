package com.seowonn.mymap.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // 필터 위치로 인해 수정. "/member/**" 경로일 경우 패스
    String requestURI = request.getRequestURI();
    AntPathMatcher pathMatcher = new AntPathMatcher();

    if (pathMatcher.match("/member/**", requestURI)) {
      return;
    }

    // JWT 인증 로직 실행

  }
}
