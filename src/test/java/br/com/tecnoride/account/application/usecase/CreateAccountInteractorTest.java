package br.com.tecnoride.account.application.usecase;

import static br.com.tecnoride.account.shared.testdata.AccountTestData.CAR_PLATE;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.CPF;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_BELTRANO;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_EMAIL;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_DRIVER;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_PASSENGER;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.createAccount;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.createAccountInputDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.infrastructure.gateway.AccountGatewayImpl;
import br.com.tecnoride.account.infrastructure.usecase.CreateAccountInteractor;
import br.com.tecnoride.account.infrastructure.validator.VerifyIfAccountAlreadyExistsByEmailValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateAccountInteractorTest {

  @Mock
  private AccountGatewayImpl accountGateway;
  @Mock
  private VerifyIfAccountAlreadyExistsByEmailValidatorImpl verifyIfAccountAlreadyExistsByEmailValidator;
  @InjectMocks
  private CreateAccountInteractor createAccountInteractor;

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"fulano", "Fulano", "FULANO", "Fulano beltrano", "Fulano BELTRANO",
      "Fulano Beltrano Ciclano"})
  void shouldThrowExceptionWhenNameIsInvalid(String name) {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, name, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);

    assertThatThrownBy(() -> createAccountInteractor.execute(accountInputDto))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Name is invalid.");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"email.domain.com", " email.domain.com", "@", "1"})
  void shouldThrowExceptionWhenEmailAddressIsInvalid(String emailAddress) {
    var accountInputDto = createAccountInputDto(emailAddress, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);

    assertThatThrownBy(() -> createAccountInteractor.execute(accountInputDto))
        .isInstanceOf(RuntimeException.class).hasMessageContaining("Email is invalid.");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"72387289316", "18939181068", "12345678901", "11111111111", "1111111111a"})
  void shouldReturnCpfIsInvalidWhenUserCpfIsValid(String cpfNumber) {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, cpfNumber, CAR_PLATE, IS_PASSENGER,
        IS_DRIVER);

    assertThatThrownBy(() -> createAccountInteractor.execute(accountInputDto))
        .isInstanceOf(RuntimeException.class).hasMessageContaining("CPF is invalid.");
  }

  @Test
  void shouldThrowExceptionWhenEmailAccountAlreadyExists() {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    doThrow(new RuntimeException("Email already exists. %s".formatted(accountInputDto.email())))
        .when(verifyIfAccountAlreadyExistsByEmailValidator).validate(accountInputDto.email());

    assertThatThrownBy(() -> createAccountInteractor.execute(accountInputDto))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Email already exists. %s".formatted(accountInputDto.email()));
  }

  @Test
  void shouldThrowExceptionWhenUserIsPassengerAndIsDriver() {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, null, true, true);

    assertThatThrownBy(() -> createAccountInteractor.execute(accountInputDto))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("User cannot be passenger and driver at the same time.");
  }

  @Test
  void shouldCreateAccountWhenUserIsPassengerAndAccountAttributesAreCorrect() {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, null, true, false);
    var account = createAccount(FULANO_EMAIL, FULANO_BELTRANO, CPF, null, true, false);
    when(accountGateway.save(any(Account.class))).thenReturn(account);

    var accountSaved = createAccountInteractor.execute(accountInputDto);

    assertThat(accountSaved).isNotNull();
    assertThat(accountSaved.getId()).isNotNull().isEqualTo(account.getId());
    assertThat(accountSaved.getEmail()).isNotBlank().isEqualTo(account.getEmail());
    assertThat(accountSaved.getAccountName()).isNotBlank().isEqualTo(account.getAccountName());
    assertThat(accountSaved.getCpf()).isNotBlank().isEqualTo(account.getCpf());
    assertThat(accountSaved.isPassenger()).isEqualTo(account.isPassenger());
    assertThat(accountSaved.isDriver()).isEqualTo(account.isDriver());
  }

  @Test
  void shouldCreateAccountWhenUserIsDriverAndAccountAttributesAreCorrect() {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, false, true);
    var account = createAccount(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, false, true);
    when(accountGateway.save(any(Account.class))).thenReturn(account);

    var accountSaved = createAccountInteractor.execute(accountInputDto);

    assertThat(accountSaved).isNotNull();
    assertThat(accountSaved.getId()).isNotNull().isEqualTo(account.getId());
    assertThat(accountSaved.getEmail()).isNotBlank().isEqualTo(account.getEmail());
    assertThat(accountSaved.getAccountName()).isNotBlank().isEqualTo(account.getAccountName());
    assertThat(accountSaved.getCpf()).isNotBlank().isEqualTo(account.getCpf());
    assertThat(accountSaved.isPassenger()).isEqualTo(account.isPassenger());
    assertThat(accountSaved.isDriver()).isEqualTo(account.isDriver());
  }
}
