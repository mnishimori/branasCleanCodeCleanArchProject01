package br.com.tecnoride.account.application.validator;

import static br.com.tecnoride.account.domain.field.AccountDomainField.ACCOUNT_EMAIL_FIELD;
import static br.com.tecnoride.account.infrastructure.message.AccountInfrastructureMessage.EMAIL_ALREADY_EXISTS;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_EMAIL;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.createAccount;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.application.exception.DuplicatedException;
import br.com.tecnoride.account.infrastructure.validator.VerifyIfAccountAlreadyExistsByEmailValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.FieldError;

@ExtendWith(MockitoExtension.class)
class VerifyIfAccountAlreadyExistsByEmailValidatorImplTest {

  @Mock
  private AccountGateway accountGateway;
  @InjectMocks
  private VerifyIfAccountAlreadyExistsByEmailValidatorImpl verifyIfAccountAlreadyExistsByEmailValidator;

  @Test
  void shouldValidateAccountWhenEmailDoesNotExist() {
    when(accountGateway.findAccountByEmail(FULANO_EMAIL)).thenReturn(null);

    assertThatCode(() -> verifyIfAccountAlreadyExistsByEmailValidator.validate(
        FULANO_EMAIL)).doesNotThrowAnyException();
  }

  @Test
  void shouldThrowExceptionWhenEmailAlreadyExists() {
    when(accountGateway.findAccountByEmail(FULANO_EMAIL)).thenThrow(
        new DuplicatedException(new FieldError(this.getClass().getSimpleName(), ACCOUNT_EMAIL_FIELD,
            EMAIL_ALREADY_EXISTS.formatted(FULANO_EMAIL))));

    assertThatThrownBy(() -> verifyIfAccountAlreadyExistsByEmailValidator.validate(FULANO_EMAIL))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining(EMAIL_ALREADY_EXISTS.formatted(FULANO_EMAIL));
  }

  @Test
  void shouldThrowExceptionWhenEmailAlreadyExistsAndItsReturnIsNotNull() {
    var account = createAccount();
    when(accountGateway.findAccountByEmail(FULANO_EMAIL)).thenReturn(account);

    assertThatThrownBy(() -> verifyIfAccountAlreadyExistsByEmailValidator.validate(FULANO_EMAIL))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining(EMAIL_ALREADY_EXISTS.formatted(FULANO_EMAIL));
  }
}
