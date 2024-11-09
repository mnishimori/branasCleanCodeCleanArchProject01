package br.com.tecnoride.account.infrastructure.usecase;

import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.application.usecase.CreateAccountUseCase;
import br.com.tecnoride.account.application.validator.VerifyIfAccountAlreadyExistsByEmailValidator;
import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.infrastructure.presentation.dto.AccountInputDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateAccountInteractor extends CreateAccountUseCase {

  public CreateAccountInteractor(AccountGateway accountGateway,
      VerifyIfAccountAlreadyExistsByEmailValidator verifyIfAccountAlreadyExistsByEmailValidator) {
    super(accountGateway, verifyIfAccountAlreadyExistsByEmailValidator);
  }

  @Transactional
  @Override
  public Account execute(AccountInputDto accountInputDto) {
    return super.execute(accountInputDto);
  }
}
