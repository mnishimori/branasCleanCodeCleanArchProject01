package br.com.tecnoride.account;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

  private final AccountDao accountDao;

  public AccountServiceImpl(AccountDao accountDao) {
    this.accountDao = accountDao;
  }

  @Override
  public Account findAccountById(UUID uuid) {
    var account = accountDao.findAccountBy(uuid);
    if (account == null) {
      throw new RuntimeException("Account not found with id %s".formatted(uuid));
    }
    return account;
  }
}
