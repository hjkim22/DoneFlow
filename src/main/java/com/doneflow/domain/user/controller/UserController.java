package com.doneflow.domain.user.controller;

import com.doneflow.domain.user.dto.UserChangePasswordDto;
import com.doneflow.domain.user.dto.UserInfoResponseDto;
import com.doneflow.domain.user.dto.UserUpdateInfoDto;
import com.doneflow.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  @GetMapping("/{userId}")
  public UserInfoResponseDto getUserById(@PathVariable("userId") Long userId) {
    return userService.getUserById(userId);
  }

  // 정보 수정
  @PutMapping("/{userId}")
  public UserInfoResponseDto updateUserInfo(
      @PathVariable("userId") Long userId, @Valid @RequestBody UserUpdateInfoDto requestDto) {
    return userService.updateUserInfo(userId, requestDto);
  }

  // 비밀번호 변경
  @PutMapping("/{userId}/password")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changePassword(
      @PathVariable("userId") Long userId, @RequestBody UserChangePasswordDto requestDto) {
    userService.changePassword(userId, requestDto);
  }

  // 회원 탈퇴
  @DeleteMapping("/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("userId") Long userId) {
    userService.deleteUser(userId);
  }
}
