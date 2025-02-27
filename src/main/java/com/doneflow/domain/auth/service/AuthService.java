package com.doneflow.domain.auth.service;

import com.doneflow.common.enums.Role;
import com.doneflow.common.exception.CustomException;
import com.doneflow.common.exception.ErrorCode;
import com.doneflow.domain.auth.dto.AuthLoginRequestDto;
import com.doneflow.domain.auth.dto.AuthRegisterRequestDto;
import com.doneflow.domain.auth.dto.AuthResponseDto;
import com.doneflow.domain.auth.dto.UserRegisterResponseDto;
import com.doneflow.domain.user.entity.User;
import com.doneflow.domain.user.repository.UserRepository;
import com.doneflow.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  // 회원가입
  @Transactional
  public UserRegisterResponseDto register(AuthRegisterRequestDto request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User user= User.builder()
        .email(request.getEmail())
        .password(encodedPassword)
        .nickname(request.getNickname())
        .role(Role.USER)
        .build();

    userRepository.save(user);

    return UserRegisterResponseDto.from(user);
  }

  // 로그인 & JWT 토큰 발급
  public AuthResponseDto login(AuthLoginRequestDto request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
    }

    // AccessToken & RefreshToken 발급
    String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole());
    String refreshToken = jwtProvider.generateRefreshToken(user.getId());

    return new AuthResponseDto(accessToken, refreshToken);
  }

  // Refresh Token 이용한 토큰 재발급
  @Transactional
  public AuthResponseDto refreshToken(String refreshToken) {
    if (!jwtProvider.isValidToken(refreshToken)) {
      throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    Long userId = jwtProvider.extractUserIdFromToken(refreshToken);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    String newAccessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole());
    String newRefreshToken = jwtProvider.generateRefreshToken(user.getId());

    return new AuthResponseDto(newAccessToken, newRefreshToken);
  }
}
