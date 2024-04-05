package com.seowonn.mymap.config.security;

import com.seowonn.mymap.config.security.jwt.JwtAuthenticationEntryPoint;
import com.seowonn.mymap.config.security.jwt.JwtAuthenticationFilter;
import com.seowonn.mymap.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable);

    http
        .formLogin(AbstractHttpConfigurer::disable);

    http
        .sessionManagement(configurer ->
            configurer.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(authorizeRequests -> {
          authorizeRequests
              .requestMatchers("/member/**")
              .permitAll();

          authorizeRequests.
              requestMatchers("/user/**").authenticated();
        })
        .exceptionHandling(exceptionHandling ->
            exceptionHandling
                .authenticationEntryPoint(
                    new JwtAuthenticationEntryPoint())
        )
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class
        );

    return http.build();
  }

}
