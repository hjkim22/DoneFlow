package com.doneflow.security;

import com.doneflow.common.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

  private static final String CLAIM_ROLES = "roles";
  private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1시간
  private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7일

  @Value("${jwt.secret}")
  private String secretKey;

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // Access Token 생성
  public String generateAccessToken(Long userId, Role role) {
    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim(CLAIM_ROLES, "ROLE_" + role.name())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
        .signWith(getSigningKey())
        .compact();
  }

  // Refresh Token 생성
  public String generateRefreshToken(Long userId) {
    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
        .signWith(getSigningKey())
        .compact();
  }

  // 토큰에서 userId 추출
  public Long getUserIdFromToken(String token) {
    return Long.valueOf(getClaims(token).getSubject());
  }

  // 토큰에서 인증 객체 추출 (Spring Security 연동)
  public Authentication getAuthentication(String token) {
    Long userId = getUserIdFromToken(token);
    String roleClaim = getClaims(token).get(CLAIM_ROLES, String.class);

    if (roleClaim == null) {
      throw new IllegalStateException("JWT 에서 roles 클레임을 찾을 수 없습니다.");
    }

    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleClaim);

    return new UsernamePasswordAuthenticationToken(userId, null, Collections.singletonList(authority));
  }

  // 토큰 유효성 검사 (최신 jjwt 1.0.0 방식)
  public boolean isValidToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.error("토큰 만료: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("지원되지 않는 토큰: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("잘못된 토큰 형식: {}", e.getMessage());
    } catch (SignatureException e) {
      log.error("서명 오류: {}", e.getMessage());
    } catch (Exception e) {
      log.error("토큰 검증 실패: {}", e.getMessage());
    }
    return false;
  }

  // 토큰에서 클레임 추출
  private Claims getClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // HTTP 요청에서 JWT 토큰 추출
  public String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}