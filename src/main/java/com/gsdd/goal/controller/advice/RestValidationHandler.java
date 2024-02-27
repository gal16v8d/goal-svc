package com.gsdd.goal.controller.advice;

import com.gsdd.goal.exception.BadDataException;
import com.gsdd.goal.model.response.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestValidationHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadDataException.class)
  protected ResponseEntity<ApiError> handleBadDataException(BadDataException e) {
    log.warn("BadDataException", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
        .body(ApiError.builder().message(e.getLocalizedMessage()).build());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  protected ResponseEntity<ApiError> handleIntegrityException(DataIntegrityViolationException e) {
    log.warn("DataIntegrityViolationException", e);
    return ResponseEntity.status(HttpStatus.CONFLICT.value())
        .body(ApiError.builder().message(e.getLocalizedMessage()).build());
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiError> handleException(Exception e) {
    log.warn("Exception", e);
    return ResponseEntity.internalServerError()
        .body(ApiError.builder().message(e.getLocalizedMessage()).build());
  }

}
