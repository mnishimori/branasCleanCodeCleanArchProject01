package br.com.tecnoride.account.application.usecase;

import static br.com.tecnoride.account.shared.testdata.AccountTestData.CAR_PLATE;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.CPF;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_BELTRANO;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_EMAIL;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_DRIVER;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_PASSENGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.domain.valueobject.AccountName;
import br.com.tecnoride.account.domain.valueobject.Cpf;
import br.com.tecnoride.account.domain.valueobject.Email;
import br.com.tecnoride.account.infrastructure.exception.NoResultException;
import br.com.tecnoride.account.infrastructure.exception.ValidatorException;
import br.com.tecnoride.account.infrastructure.gateway.AccountGatewayImpl;
import br.com.tecnoride.account.infrastructure.usecase.GetAccountByIdInteractor;
import br.com.tecnoride.shared.validator.UuidValidator;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAccountByIdInteractorTest {

  @Mock
  private AccountGatewayImpl accountGateway;
  @Mock
  private UuidValidator uuidValidator;
  @InjectMocks
  private GetAccountByIdInteractor getAccountByIdUseCase;

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"a", "1", "a1"})
  void shouldThrowValidatorExceptionWhenAccountIdIsInvalid(String id) {
    doThrow(ValidatorException.class).when(uuidValidator).validate(id);

    assertThatThrownBy(() -> getAccountByIdUseCase.execute(id)).isInstanceOf(ValidatorException.class);
  }

  @Test
  void shouldThrowNoResultExceptionWhenAccountWasNotFoundById() {
    var id = UUID.randomUUID().toString();
    when(getAccountByIdUseCase.execute(id)).thenThrow(NoResultException.class);

    assertThatThrownBy(() -> getAccountByIdUseCase.execute(id)).isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldGetAccountWhenAccountWasFoundById() {
    var id = UUID.randomUUID();
    var email = new Email(FULANO_EMAIL);
    var name = new AccountName(FULANO_BELTRANO);
    var cpf = new Cpf(CPF);
    var account = new Account(id, email, name, cpf, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    when(accountGateway.findAccountByIdRequired(id)).thenReturn(account);

    var accountFound = getAccountByIdUseCase.execute(id.toString());

    assertThat(accountFound).isNotNull();
    assertThat(accountFound).usingRecursiveComparison().isEqualTo(account);
  }
}