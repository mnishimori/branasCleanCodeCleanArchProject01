package br.com.tecnoride.account.domain.entity;

import static br.com.tecnoride.account.shared.testdata.AccountTestData.CAR_PLATE;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.CPF;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_BELTRANO;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_EMAIL;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_DRIVER;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_PASSENGER;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AccountTest {

  @Test
  void shouldThrowExceptionWhenItWasTriedToCreateAccountWithoutSettingPassengerOrDriver() {
    assertThatThrownBy(() -> new Account(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, false, false))
        .isInstanceOf(RuntimeException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowExceptionWhenItWasTriedToCreateAccountForDriverWithoutCarPlate(String carPlate) {
    assertThatThrownBy(() -> new Account(FULANO_EMAIL, FULANO_BELTRANO, CPF, carPlate, false, true))
        .isInstanceOf(RuntimeException.class);
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void shouldThrowExceptionWhenItWasTriedToCreateTheSameAccountForPassengerAndDriverAtTheSameTime(boolean value) {
    assertThatThrownBy(() -> new Account(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, value, value))
        .isInstanceOf(RuntimeException.class);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowExceptionWhenItWasTriedToCreateDriverAccountWithoutCarPlate(String carPlate) {
    assertThatThrownBy(() -> new Account(FULANO_EMAIL, FULANO_BELTRANO, CPF, carPlate, false, true))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldCreateAccountWhenAllAttributesAreCorrectForPassenger() {
    assertThatCode(() -> new Account(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER))
        .doesNotThrowAnyException();
  }

  @Test
  void shouldCreateAccountWhenAllAttributesAreCorrectForDriver() {
    assertThatCode(() -> new Account(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, false, true))
        .doesNotThrowAnyException();
  }
}
