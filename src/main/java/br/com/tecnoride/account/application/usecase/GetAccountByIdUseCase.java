package br.com.tecnoride.account.application.usecase;

import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.shared.validator.UuidValidator;
import java.util.UUID;

public abstract class GetAccountByIdUseCase {

  protected UuidValidator uuidValidator;
  protected AccountGateway accountGateway;

  protected GetAccountByIdUseCase(UuidValidator uuidValidator, AccountGateway accountGateway) {
    this.uuidValidator = uuidValidator;
    this.accountGateway = accountGateway;
  }

  public Account execute(String id) {
    uuidValidator.validate(id);
    return accountGateway.findAccountByIdRequired(UUID.fromString(id));
  }
}
