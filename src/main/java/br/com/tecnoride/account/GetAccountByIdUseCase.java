package br.com.tecnoride.account;

import br.com.tecnoride.shared.validator.UuidValidator;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GetAccountByIdUseCase {

  private final UuidValidator uuidValidator;
  private final AccountService accountService;

  public GetAccountByIdUseCase(UuidValidator uuidValidator, AccountService accountService) {
    this.uuidValidator = uuidValidator;
    this.accountService = accountService;
  }

  public Account execute(String id) {
    uuidValidator.validate(id);
    return accountService.findAccountById(UUID.fromString(id));
  }
}
