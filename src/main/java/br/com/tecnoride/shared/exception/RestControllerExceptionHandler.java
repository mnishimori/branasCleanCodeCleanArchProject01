package br.com.tecnoride.shared.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import br.com.tecnoride.account.domain.exception.BusinessException;
import br.com.tecnoride.account.infrastructure.exception.DuplicatedException;
import br.com.tecnoride.account.infrastructure.exception.NoResultException;
import br.com.tecnoride.account.infrastructure.exception.ValidatorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {

  @ExceptionHandler(NoResultException.class)
  public ResponseEntity<String> handlerNoResultException(NoResultException ex) {
    var error = ex.getMessage();
    return ResponseEntity.status(NOT_FOUND).body(error);
  }

  @ExceptionHandler(  DuplicatedException.class)
  public ResponseEntity<String> handlerDuplicatedException(  DuplicatedException ex) {
    var error = ex.getMessage();
    return ResponseEntity.status(CONFLICT).body(error);
  }

  @ExceptionHandler(ValidatorException.class)
  public ResponseEntity<String> handlerValidatorException(ValidatorException ex) {
    var error = ex.getMessage();
    return ResponseEntity.status(BAD_REQUEST).body(error);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<String> handlerBusinessException(BusinessException ex) {
    var error = ex.getMessage();
    return ResponseEntity.status(BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handlerInternalServerError(Exception ex) {
    var error = ex.getMessage();
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
  }
}
