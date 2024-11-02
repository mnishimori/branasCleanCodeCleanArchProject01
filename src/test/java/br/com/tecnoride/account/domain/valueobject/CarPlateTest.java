package br.com.tecnoride.account.domain.valueobject;

import static br.com.tecnoride.account.domain.message.AccountDomainMessage.CAR_PLATE_IS_INVALID;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.CAR_PLATE;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.tecnoride.account.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CarPlateTest {

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowExceptionWhenCarPlateIsNullOrEmpty(String carPlateNumber) throws Exception {
    assertThatThrownBy(() -> new CarPlate(carPlateNumber))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(CAR_PLATE_IS_INVALID.formatted(carPlateNumber));
  }

  @ParameterizedTest
  @ValueSource(strings = {"123-1234", "ABC-DEFG", "ABC-D3FG"})
  void shouldThrowExceptionWhenCarPlatePatternDoesNotMatchesWithCarPlateNumber(String carPlateNumber) throws Exception {
    assertThatThrownBy(() -> new CarPlate(carPlateNumber))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining(CAR_PLATE_IS_INVALID.formatted(carPlateNumber));
  }

  @Test
  void shouldValidateCarPlateWhenCarPlatePatternMatchesWithCarPlateNumber() {
    assertThatCode(() -> new CarPlate(CAR_PLATE)).doesNotThrowAnyException();
  }
}
