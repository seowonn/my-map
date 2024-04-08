package com.seowonn.mymap.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
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
      filterChain.doFilter(request, response);
      return;
    }

    // JWT 인증 로직 실행
    String token = jwtTokenProvider.resolveToken(request);

    if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.info("[doFilterInternal] : Security Context에 '{}' 인증 정보 저장 완료. uri : {}",
          authentication.getName(), requestURI);

    } else {
      log.info("[doFilterInternal] : 유효한 JWT 토큰 없음. uri : {}", requestURI);
    }

    filterChain.doFilter(request, response);
  }
}
