package br.com.tecnoride.account.infrastructure.usecase;

import br.com.tecnoride.account.application.usecase.GetAccountByIdUseCase;
import br.com.tecnoride.account.infrastructure.gateway.AccountGatewayImpl;
import br.com.tecnoride.shared.validator.UuidValidator;
import org.springframework.stereotype.Service;

@Service
public class GetAccountByIdInteractor extends GetAccountByIdUseCase {

  public GetAccountByIdInteractor(UuidValidator uuidValidator, AccountGatewayImpl accountGateway) {
    super(uuidValidator, accountGateway);
  }
}
