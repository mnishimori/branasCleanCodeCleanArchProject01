package br.com.tecnoride.account.infrastructure.validator;

import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.application.validator.VerifyIfAccountAlreadyExistsByEmailValidator;
import org.springframework.stereotype.Component;

@Component
public class VerifyIfAccountAlreadyExistsByEmailValidatorImpl extends VerifyIfAccountAlreadyExistsByEmailValidator {

  public VerifyIfAccountAlreadyExistsByEmailValidatorImpl(AccountGateway accountGateway) {
    super(accountGateway);
  }
}
