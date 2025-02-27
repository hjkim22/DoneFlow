package com.doneflow.domain.auth.controller;

import com.doneflow.domain.auth.dto.AuthLoginRequestDto;
import com.doneflow.domain.auth.dto.AuthRefreshRequestDto;
import com.doneflow.domain.auth.dto.AuthRegisterRequestDto;
import com.doneflow.domain.auth.dto.AuthResponseDto;
import com.doneflow.domain.auth.dto.UserRegisterResponseDto;
import com.doneflow.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  // 회원가입
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserRegisterResponseDto register(@Valid @RequestBody AuthRegisterRequestDto request) {
    return authService.register(request);
  }

  // 로그인
  @PostMapping("/login")
  public AuthResponseDto login(@Valid @RequestBody AuthLoginRequestDto request) {
    return authService.login(request);
  }

  // 토큰 재발급
  @PostMapping("/refresh")
  public AuthResponseDto refreshToken(@Valid @RequestBody AuthRefreshRequestDto request) {
    return authService.refreshToken(request.getRefreshToken());
  }
}
