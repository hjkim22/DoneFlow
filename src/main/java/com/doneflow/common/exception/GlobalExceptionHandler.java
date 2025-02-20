package com.doneflow.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
}
