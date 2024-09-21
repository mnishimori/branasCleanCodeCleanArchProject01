package br.com.tecnoride.account;

import java.util.UUID;

public interface AccountDao {

  Account findAccountBy(UUID uuid);

  Account findAccountBy(String email);
}
