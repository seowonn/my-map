package com.seowonn.mymap.security.jwt;

import static com.seowonn.mymap.global.type.TimeSettings.ACCESS_TOKEN_EXPIRE_TIME;

import com.seowonn.mymap.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final CustomUserDetailsService customUserDetailsService;

  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  @Value("${jwt.secret}")
  private String secretKeyConfig;

  private static final String KEY_ROLES = "roles";
  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    byte[] keyBytes = Base64.getDecoder().decode(secretKeyConfig);
    secretKey = Keys.hmacShaKeyFor(keyBytes);
    log.info("[init] : secretKey 초기화 완료");
  }

  public String createAccessToken(String username, List<String> roles) {
    log.info("[createAccessToken] : accessToken 생성 시작");

    String authorities = String.join(",", roles);

    Claims claims = Jwts.claims().setSubject(username);
    claims.put(KEY_ROLES, authorities);

    Date now = new Date();
    Date expireDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME.getTime());

    String accessToken = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expireDate)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();

    log.info("[createAccessToken] : accessToken 생성 완료");
    return accessToken;
  }

  public String resolveToken(HttpServletRequest request) {

    String token = request.getHeader(TOKEN_HEADER);
    if(token == null || !token.startsWith(TOKEN_PREFIX)) {
      return null;
    }

    log.info("[resolveToken] : HTTP 헤더에서 Token 값 추출 완료");
    return token.substring(TOKEN_PREFIX.length());
  }

  public boolean validateToken(String token) {

    log.info("[validateToken] : 토큰 유효성 체크 시작");

    try{
      Jwts.parserBuilder()
          .setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.debug("[validateToken] : 잘못된 JWT 서명 " + e.getMessage());
    } catch (ExpiredJwtException e) {
      log.debug("[validateToken] : 만료된 JWT 토큰 " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.debug("[validateToken] : 지원되지 않는 JWT 토큰 " + e.getMessage());
    } catch (IllegalArgumentException e) {
      log.debug("[validateToken] : 잘못된 JWT 토큰 " + e.getMessage());
    }

    return false;
  }

  public Authentication getAuthentication(String token) {

    log.info("[getAuthentication] : 토큰 인증 정보 조회 시작");

    // claim 추출
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(KEY_ROLES).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .toList();

    UserDetails principal =
        customUserDetailsService.loadUserByUsername(claims.getSubject());

    log.info("[getAuthentication] 토큰 인증 정보 조회 완료, userName : {}",
        claims.getSubject());

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

}
