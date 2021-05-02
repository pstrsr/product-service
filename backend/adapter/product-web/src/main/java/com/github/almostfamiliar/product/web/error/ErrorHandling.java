package com.github.almostfamiliar.product.web.error;

import com.github.almostfamiliar.exception.ApplicationException;
import com.github.almostfamiliar.exception.NotFoundException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandling {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> constrainViolationException(
      HttpServletRequest req, ConstraintViolationException exc) {
    return ErrorResponse.createErrorResp(req, HttpStatus.BAD_REQUEST, exc);
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ErrorResponse> constrainViolationException(
      HttpServletRequest req, ApplicationException exc) {
    return ErrorResponse.createErrorResp(req, HttpStatus.CONFLICT, exc);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> constrainViolationException(
      HttpServletRequest req, NotFoundException exc) {
    return ErrorResponse.createErrorResp(req, HttpStatus.NOT_FOUND, exc);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> unexpectedException(HttpServletRequest req, Exception exc) {
    return ErrorResponse.createErrorResp(req, HttpStatus.INTERNAL_SERVER_ERROR, exc);
  }

  @Value
  public static class ErrorResponse {
    @Schema(
        description = "Timestamp in ISO-Format",
        example = "2021-05-02T00:31:20.884820",
        required = true)
    String timestamp;

    @Schema(description = "HTTP Status", example = "404", required = true)
    Integer status;

    @Schema(
        description = "Error that was thrown",
        example = "CategoryDoesNotExistExc",
        required = true)
    String error;

    @Schema(
        description = "Descriptive error message",
        example = "Category with id '3' does not exist!",
        required = true)
    String message;

    @Schema(
        description = "request path that was called",
        example = "/v1/category/3",
        required = true)
    String path;

    public static ResponseEntity<ErrorResponse> createErrorResp(
        HttpServletRequest req, HttpStatus code, Exception e) {
      final ErrorResponse errResponse =
          new ErrorResponse(
              LocalDateTime.now().toString(),
              code.value(),
              e.getClass().getSimpleName(),
              e.getMessage(),
              req.getRequestURI());
      return ResponseEntity.status(code).body(errResponse);
    }
  }
}
