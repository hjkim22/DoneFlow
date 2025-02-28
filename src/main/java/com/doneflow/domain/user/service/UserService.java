package com.doneflow.domain.user.service;

import com.doneflow.common.exception.CustomException;
import com.doneflow.common.exception.ErrorCode;
import com.doneflow.domain.user.dto.UserChangePasswordDto;
import com.doneflow.domain.user.dto.UserInfoResponseDto;
import com.doneflow.domain.user.dto.UserUpdateInfoDto;
import com.doneflow.domain.user.entity.User;
import com.doneflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // 정보 조회
  public UserInfoResponseDto getUserById(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    return UserInfoResponseDto.from(user);
  }

  // 정보 수정
  @Transactional
  public UserInfoResponseDto updateUserInfo(Long userId, UserUpdateInfoDto requestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.setNickname(requestDto.getNickname());
    return UserInfoResponseDto.from(user);
  }

  // 비밀번호 변경
  @Transactional
  public void changePassword(Long userId, UserChangePasswordDto requestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
    }

    String encodeNewPassword = passwordEncoder.encode(requestDto.getNewPassword());
    user.updatePassword(encodeNewPassword);
  }

  // 회원 탈퇴
  @Transactional
  public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    userRepository.delete(user);
  }
}
