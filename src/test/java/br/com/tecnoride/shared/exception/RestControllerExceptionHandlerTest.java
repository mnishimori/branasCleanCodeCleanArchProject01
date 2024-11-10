package br.com.tecnoride.shared.exception;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.tecnoride.account.domain.exception.BusinessException;
import br.com.tecnoride.account.application.exception.DuplicatedException;
import br.com.tecnoride.account.infrastructure.exception.NoResultException;
import br.com.tecnoride.account.infrastructure.exception.ValidatorException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

class RestControllerExceptionHandlerTest {

  public static final String NO_RESULT_FOUND = "No result found";
  public static final String DUPLICATE_ENTRY = "Duplicate entry";
  public static final String VALIDATION_FAILED = "Validation failed";
  public static final String BUSINESS_RULE_VIOLATED = "Business rule violated";
  public static final String INTERNAL_SERVER_ERROR = "Internal server error";
  private final RestControllerExceptionHandler exceptionHandler = new RestControllerExceptionHandler();

  @Test
  void shouldHandleNoResultExceptionWhenThrewNoResultException() {
    var ex = new NoResultException(new FieldError("", "", NO_RESULT_FOUND));
    var response = exceptionHandler.handlerNoResultException(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isEqualTo(NO_RESULT_FOUND);
  }

  @Test
  void shouldHandleDuplicatedExceptionWhenThrewDuplicatedException() {
    var ex = new DuplicatedException(new FieldError("", "", DUPLICATE_ENTRY));
    var response = exceptionHandler.handlerDuplicatedException(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody()).isEqualTo(DUPLICATE_ENTRY);
  }

  @Test
  void shouldHandleValidatorExceptionWhenThrewValidatorException() {
    var ex = new ValidatorException(new FieldError("", "", VALIDATION_FAILED));
    var response = exceptionHandler.handlerValidatorException(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(VALIDATION_FAILED);
  }

  @Test
  void shouldHandleBusinessExceptionWhenThrewBusinessException() {
    var ex = new BusinessException(new FieldError("", "", BUSINESS_RULE_VIOLATED));
    var response = exceptionHandler.handlerBusinessException(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(BUSINESS_RULE_VIOLATED);
  }

  @Test
  void shouldHandleInternalServerErrorWhenThrewInternalServerError() {
    var ex = new Exception(INTERNAL_SERVER_ERROR);
    var response = exceptionHandler.handlerInternalServerError(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).isEqualTo(INTERNAL_SERVER_ERROR);
  }
}
