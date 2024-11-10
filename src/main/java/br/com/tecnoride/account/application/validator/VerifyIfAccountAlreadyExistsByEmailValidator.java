package br.com.tecnoride.account.application.validator;

import static br.com.tecnoride.account.domain.field.AccountDomainField.ACCOUNT_EMAIL_FIELD;
import static br.com.tecnoride.account.infrastructure.message.AccountInfrastructureMessage.EMAIL_ALREADY_EXISTS;

import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.application.exception.DuplicatedException;
import org.springframework.validation.FieldError;

public abstract class VerifyIfAccountAlreadyExistsByEmailValidator {

  private final AccountGateway accountGateway;

  public VerifyIfAccountAlreadyExistsByEmailValidator(AccountGateway accountGateway) {
    this.accountGateway = accountGateway;
  }

  public void validate(String email) {
    var account = accountGateway.findAccountByEmail(email);
    if (account != null) {
      throw new DuplicatedException(
          new FieldError(this.getClass().getSimpleName(), ACCOUNT_EMAIL_FIELD, EMAIL_ALREADY_EXISTS.formatted(email)));
    }
  }
}
