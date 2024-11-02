package br.com.tecnoride.account.infrastructure.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;

class NoResultExceptionTest {

  @Test
  void shouldCreateNoResultExceptionWithFieldError() {
    var field = "query";
    var errorMessage = "No result found for the query";
    var fieldError = new FieldError("Database", field, errorMessage);

    var exception = new NoResultException(fieldError);

    assertThat(exception).isNotNull();
    assertThat(exception.getFieldError()).isEqualTo(fieldError);
    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void shouldThrowNoResultExceptionWithCorrectMessage() {
    var field = "search";
    var errorMessage = "No matching records found";
    var fieldError = new FieldError("Database", field, errorMessage);

    assertThatThrownBy(() -> {
      throw new NoResultException(fieldError);
    })
        .isInstanceOf(NoResultException.class)
        .hasMessage(errorMessage)
        .extracting("fieldError")
        .isEqualTo(fieldError);
  }
}
