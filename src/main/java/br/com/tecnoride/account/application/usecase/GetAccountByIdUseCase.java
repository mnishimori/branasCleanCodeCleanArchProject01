package br.com.tecnoride.account.application.usecase;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.shared.validator.UuidValidator;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GetAccountByIdUseCase {

  private final UuidValidator uuidValidator;
  private final AccountGateway accountGateway;

  public GetAccountByIdUseCase(UuidValidator uuidValidator, AccountGateway accountGateway) {
    this.uuidValidator = uuidValidator;
    this.accountGateway = accountGateway;
  }

  public Account execute(String id) {
    uuidValidator.validate(id);
    return accountGateway.findAccountById(UUID.fromString(id));
  }
}
