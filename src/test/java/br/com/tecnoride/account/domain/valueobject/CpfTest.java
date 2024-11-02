package br.com.tecnoride.account.domain.valueobject;

import static br.com.tecnoride.account.domain.message.AccountDomainMessage.CPF_IS_INVALID;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.CPF;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.tecnoride.account.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CpfTest {

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"72387289316", "18939181068", "12345678901", "11111111111", "1111111111a", "1234567890"})
  void shouldReturnCpfIsInvalidWhenCpfPatternDoesNotMatchesWithCpfNumber(String cpfNumber) throws Exception {
    assertThatThrownBy(() -> new Cpf(cpfNumber))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(CPF_IS_INVALID.formatted(cpfNumber));
  }

  @Test
  void shouldValidateCpfNumberWhenCpfPatternMatchesWithCpfNumber() {
    assertThatCode(() -> new Cpf(CPF)).doesNotThrowAnyException();
  }
}
