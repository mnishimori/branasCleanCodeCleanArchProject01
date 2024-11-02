package br.com.tecnoride.account.domain.exception;

import org.springframework.validation.FieldError;

public class BusinessException extends RuntimeException {

  private final FieldError fieldError;

  public BusinessException(FieldError fieldError) {
    super(fieldError.getDefaultMessage());
    this.fieldError = fieldError;
  }

  public FieldError getFieldError() {
    return fieldError;
  }
}
