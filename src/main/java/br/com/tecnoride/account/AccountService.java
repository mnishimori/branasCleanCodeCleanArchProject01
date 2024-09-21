package br.com.tecnoride.account;

import java.util.UUID;

public interface AccountService {

  Account findAccountById(UUID id);
}
