package br.com.tecnoride.account.infrastructure.validator;

import static br.com.tecnoride.account.domain.field.AccountDomainField.ACCOUNT_EMAIL_FIELD;
import static br.com.tecnoride.account.infrastructure.message.AccountInfrastructureMessage.EMAIL_ALREADY_EXISTS;

import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.application.validator.VerifyIfAccountAlreadyExistsByEmailValidator;
import br.com.tecnoride.account.infrastructure.exception.DuplicatedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

@Component
public class VerifyIfAccountAlreadyExistsByEmailValidatorImpl implements VerifyIfAccountAlreadyExistsByEmailValidator {

  private final AccountGateway accountGateway;

  public VerifyIfAccountAlreadyExistsByEmailValidatorImpl(AccountGateway accountGateway) {
    this.accountGateway = accountGateway;
  }

  @Override
  public void validate(String email) {
    var account = accountGateway.findAccountByEmail(email);
    if (account != null) {
      throw new DuplicatedException(
          new FieldError(this.getClass().getSimpleName(), ACCOUNT_EMAIL_FIELD, EMAIL_ALREADY_EXISTS.formatted(email)));
    }
  }
}
