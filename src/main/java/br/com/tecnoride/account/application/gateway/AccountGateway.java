package br.com.tecnoride.account.application.gateway;

import br.com.tecnoride.account.domain.entity.Account;
import java.util.UUID;

public interface AccountGateway {

  Account findAccountByIdRequired(UUID id);
  Account save(Account account);
  Account findAccountByEmail(String email);
}
