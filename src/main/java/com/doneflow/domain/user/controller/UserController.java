package com.doneflow.domain.user.controller;

import com.doneflow.common.annotation.CurrentUserId;
import com.doneflow.domain.user.dto.UserChangePasswordDto;
import com.doneflow.domain.user.dto.UserInfoResponseDto;
import com.doneflow.domain.user.dto.UserUpdateInfoDto;
import com.doneflow.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  // 정보 조회
  @GetMapping("/me")
  public UserInfoResponseDto getUserById(@CurrentUserId Long userId) {
    return userService.getUserById(userId);
  }

  // 정보 수정
  @PutMapping("/me")
  public UserInfoResponseDto updateUserInfo(
      @CurrentUserId Long userId, @Valid @RequestBody UserUpdateInfoDto requestDto) {
    return userService.updateUserInfo(userId, requestDto);
  }

  // 비밀번호 변경
  @PutMapping("/me/password")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changePassword(
      @CurrentUserId Long userId, @RequestBody UserChangePasswordDto requestDto) {
    userService.changePassword(userId, requestDto);
  }

  // 회원 탈퇴
  @DeleteMapping("/me")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@CurrentUserId Long userId) {
    userService.deleteUser(userId);
  }
}
