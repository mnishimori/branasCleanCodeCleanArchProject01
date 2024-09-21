package br.com.tecnoride.account;

import org.assertj.core.api.Assertions;
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

  @Mock
  private AccountService accountService;
  @InjectMocks
  private GetAccountByIdUseCase getAccountByIdUseCase;

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"a", "1", "a1"})
  void shouldThrowExceptionWhenAccountIdIsInvalid(String id) {
    Assertions.fail("shouldThrowExceptionWhenAccountIdIsInvalid");
  }

  @Test
  void shouldThrowExceptionWhenAccountWasNotFoundById() {
    Assertions.fail("shouldThrowExceptionWhenAccountWasNotFoundById");
  }

  @Test
  void shouldGetAccountWhenAccountWasFoundById() {
    Assertions.fail("shouldGetAccountWhenAccountWasFoundById");
  }
}