package br.com.tecnoride.account.domain.valueobject;

import static br.com.tecnoride.account.domain.message.AccountDomainMessage.NAME_IS_INVALID_MESSAGE;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_BELTRANO;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.tecnoride.account.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AccountNameTest {

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowExceptionWhenUserNameIsNullOrEmpty(String name) {
    assertThatThrownBy(() -> new AccountName(name)).isInstanceOf(BusinessException.class)
        .hasMessageContaining(NAME_IS_INVALID_MESSAGE.formatted(name));
  }

  @ParameterizedTest
  @ValueSource(strings = {"fulano", "Fulano", "FULANO", "Fulano beltrano", "Fulano BELTRANO",
      "Fulano Beltrano Ciclano"})
  void shouldThrowExceptionWhenUserNameDoesNotHaveTwoWordsWithFirstWordCharUpperCaseAndTheRestAreNotLowerCaseChars(
      String name) {
    assertThatThrownBy(() -> new AccountName(name)).isInstanceOf(BusinessException.class)
        .hasMessageContaining(NAME_IS_INVALID_MESSAGE.formatted(name));
  }

  @Test
  void shouldValidateNameWhenNameHasTwoWordsWithFirstLetterIsCapital() {
    assertThatCode(() -> new AccountName(FULANO_BELTRANO)).doesNotThrowAnyException();
  }
}
