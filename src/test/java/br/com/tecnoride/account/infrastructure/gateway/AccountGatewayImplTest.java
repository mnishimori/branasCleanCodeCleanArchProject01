package br.com.tecnoride.account.infrastructure.gateway;

import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_EMAIL;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.createAccountSaved;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.infrastructure.exception.NoResultException;
import br.com.tecnoride.account.infrastructure.repository.AccountRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountGatewayImplTest {

  @Mock
  private AccountRepository accountRepository;
  @InjectMocks
  private AccountGatewayImpl accountService;

  @Test
  void shouldThrowNoResultExceptionWhenAccountWasNotFoundById() {
    var id = UUID.randomUUID();
    when(accountRepository.findAccountBy(id)).thenThrow(NoResultException.class);

    assertThatThrownBy(() -> accountService.findAccountByIdRequired(id)).isInstanceOf(NoResultException.class);
  }

  @Test
  void shouldThrowNoResultExceptionWhenAccountWasNotFoundByIdAndItsReturnIsNull() {
    var id = UUID.randomUUID();
    when(accountRepository.findAccountBy(id)).thenReturn(null);

    assertThatThrownBy(() -> accountService.findAccountByIdRequired(id)).isInstanceOf(NoResultException.class);
  }

  @Test
  void shouldFindAccountWhenAccountIdWasFound() {
    var id = UUID.randomUUID();
    var account = createAccountSaved(id);
    when(accountRepository.findAccountBy(id)).thenReturn(account);

    var accountFound = accountService.findAccountByIdRequired(id);

    assertThat(accountFound).isNotNull();
    assertThat(accountFound).usingRecursiveComparison().isEqualTo(account);
  }

  @Test
  void shouldReturnNullWhenAccountWasNotFoundByEmail() {
    when(accountRepository.findAccountBy(FULANO_EMAIL)).thenReturn(null);

    var account = accountService.findAccountByEmail(FULANO_EMAIL);

    assertThat(account).isNull();
  }

  @Test
  void shouldFindAccountWhenAccountWasFoundByEmail() {
    var id = UUID.randomUUID();
    var account = createAccountSaved(id);
    when(accountRepository.findAccountBy(account.getEmail())).thenReturn(account);

    var accountFound = accountService.findAccountByEmail(account.getEmail());

    assertThat(accountFound).isNotNull();
    assertThat(accountFound).usingRecursiveComparison().isEqualTo(account);
  }

  @Test
  void shouldSaveAnAccountWhenAccountAttributesAreCorrect() {
    var id = UUID.randomUUID();
    var account = createAccountSaved(id);
    when(accountRepository.save(any(Account.class))).thenReturn(account);

    var accountSaved = accountService.save(account);

    assertThat(accountSaved).isNotNull();
    assertThat(accountSaved.getId()).isNotNull().isEqualTo(id);
    assertThat(accountSaved).usingRecursiveComparison().isEqualTo(account);
  }
}
