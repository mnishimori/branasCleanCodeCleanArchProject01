package br.com.tecnoride.account.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.application.gateway.AccountGateway;
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
class GetAccountByIdUseCaseTest {

  public static final String FULANO_CICLANO_NAME = "Fulano Ciclano";
  public static final String FULANO_CICLANO_EMAIL = "fulano.ciclano@domain.com";
  public static final String FULANO_CICLANO_CPF = "62835691022";
  public static final String CAR_PLATE = "ABC-1234";
  public static final boolean IS_PASSENGER = true;
  public static final boolean IS_DRIVER = false;
  @Mock
  private AccountGateway accountGateway;
  @Mock
  private UuidValidator uuidValidator;
  @InjectMocks
  private GetAccountByIdUseCase getAccountByIdUseCase;

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"a", "1", "a1"})
  void shouldThrowExceptionWhenAccountIdIsInvalid(String id) {
    doThrow(RuntimeException.class).when(uuidValidator).validate(id);

    assertThatThrownBy(() -> getAccountByIdUseCase.execute(id))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldThrowExceptionWhenAccountWasNotFoundById() {
    var id = UUID.randomUUID().toString();
    when(getAccountByIdUseCase.execute(id)).thenReturn(null);

    var account = getAccountByIdUseCase.execute(id);

    assertThat(account).isNull();
  }

  @Test
  void shouldGetAccountWhenAccountWasFoundById() {
    var id = UUID.randomUUID();
    var account = new Account(id, FULANO_CICLANO_EMAIL, FULANO_CICLANO_NAME, FULANO_CICLANO_CPF, CAR_PLATE,
        IS_PASSENGER, IS_DRIVER);
    when(accountGateway.findAccountById(id)).thenReturn(account);

    var accountFound = getAccountByIdUseCase.execute(id.toString());

    assertThat(accountFound).isNotNull();
    assertThat(accountFound).usingRecursiveComparison().isEqualTo(accountFound);
  }
}