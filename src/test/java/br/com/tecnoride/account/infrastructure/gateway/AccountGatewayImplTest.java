package br.com.tecnoride.account.infrastructure.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.infrastructure.repository.AccountRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountGatewayImplTest {

  public static final String FULANO_CICLANO_NAME = "Fulano Ciclano";
  public static final String FULANO_CICLANO_EMAIL = "fulano.ciclano@domain.com";
  public static final String FULANO_CICLANO_CPF = "62835691022";
  public static final String CAR_PLATE = "ABC-1234";
  public static final boolean IS_PASSENGER = true;
  public static final boolean IS_DRIVER = false;
  @Mock
  private AccountRepository accountRepository;
  @InjectMocks
  private AccountGatewayImpl accountService;

  @Test
  void shouldThrowExceptionWhenAccountWasNotFoundById() {
    var id = UUID.randomUUID();
    when(accountRepository.findAccountBy(id)).thenThrow(RuntimeException.class);

    assertThatThrownBy(() -> accountService.findAccountById(id)).isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldFindAccountWhenAccountIdWasFound() {
    var id = UUID.randomUUID();
    var account = new Account(id, FULANO_CICLANO_EMAIL, FULANO_CICLANO_NAME, FULANO_CICLANO_CPF, CAR_PLATE,
        IS_PASSENGER, IS_DRIVER);
    when(accountRepository.findAccountBy(id)).thenReturn(account);

    var accountFound = accountService.findAccountById(id);

    assertThat(accountFound).isNotNull();
    assertThat(accountFound).usingRecursiveComparison().isEqualTo(account);
  }
}
