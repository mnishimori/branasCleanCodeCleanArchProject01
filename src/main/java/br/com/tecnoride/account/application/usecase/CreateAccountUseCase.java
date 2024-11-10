package br.com.tecnoride.account.application.usecase;

import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.application.validator.VerifyIfAccountAlreadyExistsByEmailValidator;
import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.domain.valueobject.AccountName;
import br.com.tecnoride.account.domain.valueobject.Cpf;
import br.com.tecnoride.account.domain.valueobject.Email;
import br.com.tecnoride.account.infrastructure.presentation.dto.AccountInputDto;

public abstract class CreateAccountUseCase {

  protected AccountGateway accountGateway;
  protected VerifyIfAccountAlreadyExistsByEmailValidator verifyIfAccountAlreadyExistsByEmailValidator;

  protected CreateAccountUseCase(AccountGateway accountGateway,
      VerifyIfAccountAlreadyExistsByEmailValidator verifyIfAccountAlreadyExistsByEmailValidator) {
    this.accountGateway = accountGateway;
    this.verifyIfAccountAlreadyExistsByEmailValidator = verifyIfAccountAlreadyExistsByEmailValidator;
  }

  public Account execute(AccountInputDto accountInputDto) {
    var email = new Email(accountInputDto.email());
    var name = new AccountName(accountInputDto.name());
    var cpf = new Cpf(accountInputDto.cpf());
    var account = new Account(email, name, cpf, accountInputDto.carPlate(), accountInputDto.isPassenger(),
        accountInputDto.isDriver());
    verifyIfAccountAlreadyExistsByEmailValidator.validate(account.getEmail());
    return accountGateway.save(account);
  }
}
