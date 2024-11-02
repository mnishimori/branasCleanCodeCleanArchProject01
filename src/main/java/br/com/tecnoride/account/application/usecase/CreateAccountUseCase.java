package br.com.tecnoride.account.application.usecase;

import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.application.validator.VerifyIfAccountAlreadyExistsByEmailValidator;
import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.presentation.dto.AccountInputDto;

public abstract class CreateAccountUseCase {

  protected AccountGateway accountGateway;
  protected VerifyIfAccountAlreadyExistsByEmailValidator verifyIfAccountAlreadyExistsByEmailValidator;

  protected CreateAccountUseCase(AccountGateway accountGateway,
      VerifyIfAccountAlreadyExistsByEmailValidator verifyIfAccountAlreadyExistsByEmailValidator) {
    this.accountGateway = accountGateway;
    this.verifyIfAccountAlreadyExistsByEmailValidator = verifyIfAccountAlreadyExistsByEmailValidator;
  }

  public Account execute(AccountInputDto accountInputDto) {
    var account = new Account(accountInputDto.email(), accountInputDto.name(), accountInputDto.cpf(),
        accountInputDto.carPlate(), accountInputDto.isPassenger(), accountInputDto.isDriver());
    verifyIfAccountAlreadyExistsByEmailValidator.validate(account.getEmail());
    return accountGateway.save(account);
  }
}
