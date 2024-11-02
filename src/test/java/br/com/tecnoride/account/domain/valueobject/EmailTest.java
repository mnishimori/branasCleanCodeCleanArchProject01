package br.com.tecnoride.account.domain.valueobject;

import static br.com.tecnoride.account.domain.message.AccountDomainMessage.EMAIL_IS_INVALID;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_EMAIL;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.tecnoride.account.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowExceptionWhenEmailIsNullOrEmpty(String emailAddress) {
    assertThatThrownBy(() -> new Email(emailAddress))
        .isInstanceOf(BusinessException.class).hasMessageContaining(EMAIL_IS_INVALID.formatted(emailAddress));
  }

  @ParameterizedTest
  @ValueSource(strings = {"email.domain.com", " email.domain.com", "@", "1"})
  void shouldThrowExceptionWhenEmailPatternDoesNotMatchesWithEmailAddress(String emailAddress) {
    assertThatThrownBy(() -> new Email(emailAddress))
        .isInstanceOf(BusinessException.class).hasMessageContaining(EMAIL_IS_INVALID.formatted(emailAddress));
  }

  @Test
  void shouldValidateEmailWhenEmailPatternMatchesEmailAddress() {
    assertThatCode(() -> new Email(FULANO_EMAIL)).doesNotThrowAnyException();
  }
}
