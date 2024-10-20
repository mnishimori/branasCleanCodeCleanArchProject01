package br.com.tecnoride.account.application.gateway;

import br.com.tecnoride.account.domain.entity.Account;
import java.util.UUID;

public interface AccountGateway {

  Account findAccountById(UUID id);
}
