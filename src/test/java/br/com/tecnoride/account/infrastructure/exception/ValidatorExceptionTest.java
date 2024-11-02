package br.com.tecnoride.account.infrastructure.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;

class ValidatorExceptionTest {

  @Test
  void shouldCreateValidatorExceptionWithFieldError() {
    var field = "age";
    var errorMessage = "Age must be positive";
    var fieldError = new FieldError("User", field, errorMessage);

    var exception = new ValidatorException(fieldError);

    assertThat(exception).isNotNull();
    assertThat(exception.getFieldError()).isEqualTo(fieldError);
    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void shouldThrowValidatorExceptionWithCorrectMessage() {
    var field = "name";
    var errorMessage = "Name is required";
    var fieldError = new FieldError("User", field, errorMessage);

    assertThatThrownBy(() -> {
      throw new ValidatorException(fieldError);
    })
        .isInstanceOf(ValidatorException.class)
        .hasMessage(errorMessage)
        .extracting("fieldError")
        .isEqualTo(fieldError);
  }
}
