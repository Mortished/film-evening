package ru.open.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionHandlingControllerAdvice {

  private static final String REQUEST_VALIDATION_REASON = "Incorrectly made request.";
  private static final String CONFLICT_REASON = "Integrity constraint has been violated.";

//  @ResponseStatus(HttpStatus.CONFLICT)
//  @ExceptionHandler(ConstraintViolationException.class)
//  public ErrorResponse handleValidationExceptions(ConstraintViolationException ex) {
//    return ErrorResponse.builder()
//        .status(HttpStatus.CONFLICT.name())
//        .reason(CONFLICT_REASON)
//        .message(ex.getMessage())
//        .timestamp(LocalDateTime.now())
//        .build();
//  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
    return ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.name())
        .reason(REQUEST_VALIDATION_REASON)
        .message(ex.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ErrorResponse handleTypeMismatchExceptions(RuntimeException ex) {
    return ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.name())
        .reason(REQUEST_VALIDATION_REASON)
        .message(ex.getMessage())
        .timestamp(LocalDateTime.now())
        .build();
  }

}
