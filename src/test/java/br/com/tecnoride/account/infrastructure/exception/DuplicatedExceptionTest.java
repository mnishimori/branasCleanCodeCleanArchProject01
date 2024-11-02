package br.com.tecnoride.account.infrastructure.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;

class DuplicatedExceptionTest {

  @Test
  void shouldCreateDuplicatedExceptionWithFieldError() {
    var field = "email";
    var errorMessage = "Email already exists";
    var fieldError = new FieldError("User", field, errorMessage);

    var exception = new DuplicatedException(fieldError);

    assertThat(exception).isNotNull();
    assertThat(exception.getFieldError()).isEqualTo(fieldError);
    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void shouldThrowDuplicatedExceptionWithCorrectMessage() {
    var field = "username";
    var errorMessage = "Username is already taken";
    var fieldError = new FieldError("User", field, errorMessage);

    assertThatThrownBy(() -> {
      throw new DuplicatedException(fieldError);
    })
        .isInstanceOf(DuplicatedException.class)
        .hasMessage(errorMessage)
        .extracting("fieldError")
        .isEqualTo(fieldError);
  }
}
