package br.com.tecnoride.account;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

  @Mock
  private AccountDao accountDao;
  @InjectMocks
  private AccountService accountService;

  @Test
  void shouldThrowExceptionWhenAccountWasNotFoundById() {
    Assertions.fail("shouldThrowExceptionWhenAccountWasNotFoundById");
  }

  @Test
  void shouldFindAccountWhenAccountIdWasFound() {
    Assertions.fail("shouldFindAccountWhenAccountIdWasFound");
  }
}
