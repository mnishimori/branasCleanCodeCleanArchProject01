package br.com.tecnoride.account.infrastructure.repository;

import br.com.tecnoride.account.domain.entity.Account;
import java.util.UUID;

public interface AccountRepository {

  Account findAccountBy(UUID uuid);
  Account findAccountBy(String email);
  Account save(Account account);
}
