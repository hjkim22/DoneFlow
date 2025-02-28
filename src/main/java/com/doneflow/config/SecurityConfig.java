package com.doneflow.config;

import com.doneflow.security.JwtAuthenticationFilter;
import com.doneflow.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtProvider jwtProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
        .cors(AbstractHttpConfigurer::disable) // CORS 설정
        .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
        .sessionManagement(session -> session.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS)) // 세션 사용 안 함 (JWT 방식 적용)
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // 회원가입, 로그인
                .requestMatchers("/api/users/**").hasRole("USER")  // 일반 유저만 접근 가능
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // 관리자만 접근 가능
                .anyRequest().authenticated()
//            .anyRequest().permitAll() // 임시
        )
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
