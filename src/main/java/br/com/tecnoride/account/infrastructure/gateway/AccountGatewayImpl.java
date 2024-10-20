package br.com.tecnoride.account.infrastructure.gateway;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.infrastructure.repository.AccountRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AccountGatewayImpl implements AccountGateway {

  private final AccountRepository accountRepository;

  public AccountGatewayImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Account findAccountById(UUID uuid) {
    var account = accountRepository.findAccountBy(uuid);
    if (account == null) {
      throw new RuntimeException("Account not found with id %s".formatted(uuid));
    }
    return account;
  }
}
