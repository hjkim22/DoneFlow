package com.doneflow.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // 커스텀 예외
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<String> handleCustomException(CustomException ex) {
    log.error("CustomException 발생: {}", ex.getErrorCode());
    return ResponseEntity
        .status(ex.getErrorCode().getHttpStatus())
        .body(ex.getErrorCode().getMessage());
  }

  // 요청 데이터 유효성 검증 실패 시
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
    StringBuilder errorMessage = new StringBuilder();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errorMessage.append(fieldError.getDefaultMessage()).append(" ");
    }
    log.warn("유효성 검증 실패: {}", errorMessage.toString().trim());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString().trim());
  }
}
