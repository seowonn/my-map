package com.seowonn.mymap.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

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

    Claims claims = Jwts.claims().setSubject(username);
    claims.put(KEY_ROLES, roles);

    Date now = new Date();
    Date expireDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

    String accessToken = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expireDate)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();

    log.info("[createAccessToken] : accessToken 생성 완료");
    return accessToken;
  }

}
