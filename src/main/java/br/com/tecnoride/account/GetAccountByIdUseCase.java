package br.com.tecnoride.account;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GetAccountByIdUseCase {

  private final AccountService accountService;

  public GetAccountByIdUseCase(AccountService accountService) {
    this.accountService = accountService;
  }

  public Account execute(String id) {
    return accountService.findAccountById(UUID.fromString(id));
  }
}
