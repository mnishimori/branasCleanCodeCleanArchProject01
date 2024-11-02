package br.com.tecnoride.account.domain.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;

class BusinessExceptionTest {

  @Test
  void shouldCreateBusinessExceptionWithFieldError() {
    var field = "username";
    var errorMessage = "Username is required";
    var fieldError = new FieldError("User", field, errorMessage);

    var exception = new BusinessException(fieldError);

    assertThat(exception).isNotNull();
    assertThat(exception.getFieldError()).isEqualTo(fieldError);
    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void shouldThrowBusinessExceptionWithCorrectMessage() {
    var field = "email";
    var errorMessage = "Email is invalid";
    var fieldError = new FieldError("User", field, errorMessage);

    assertThatThrownBy(() -> {
      throw new BusinessException(fieldError);
    })
        .isInstanceOf(BusinessException.class)
        .hasMessage(errorMessage)
        .extracting("fieldError")
        .isEqualTo(fieldError);
  }
}
