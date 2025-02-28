package com.doneflow.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String token = jwtProvider.extractToken(request);

    if (token != null && jwtProvider.isValidToken(token)) {
      try {
        SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthentication(token));
      } catch (ExpiredJwtException e) {
        log.error("JWT 만료: {}", e.getMessage());
      } catch (JwtException e) {
        log.warn("JWT 검증 실패: {}", e.getMessage());
      } catch (Exception e) {
        log.error("JWT 인증 중 예상치 못한 오류 발생: {}", e.getMessage());
      }
    }

    chain.doFilter(request, response);
  }
}
