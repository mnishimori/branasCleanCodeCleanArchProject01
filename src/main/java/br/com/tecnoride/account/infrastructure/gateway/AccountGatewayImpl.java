package br.com.tecnoride.account.infrastructure.gateway;

import static br.com.tecnoride.account.infrastructure.field.AccountInfrastructureField.ACCOUNT_ID_FIELD;
import static br.com.tecnoride.account.infrastructure.message.AccountInfrastructureMessage.ACCOUNT_NOT_FOUND_BY_ID_MESSAGE;

import br.com.tecnoride.account.application.gateway.AccountGateway;
import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.infrastructure.exception.NoResultException;
import br.com.tecnoride.account.infrastructure.repository.AccountRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

@Service
public class AccountGatewayImpl implements AccountGateway {

  private final AccountRepository accountRepository;

  public AccountGatewayImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Account findAccountByIdRequired(UUID uuid) {
    var account = accountRepository.findAccountBy(uuid);
    if (account == null) {
      throw new NoResultException(new FieldError(this.getClass().getSimpleName(), ACCOUNT_ID_FIELD,
          ACCOUNT_NOT_FOUND_BY_ID_MESSAGE.formatted(uuid)));
    }
    return account;
  }

  @Override
  public Account save(Account account) {
    return accountRepository.save(account);
  }

  @Override
  public Account findAccountByEmail(String email) {
    return accountRepository.findAccountBy(email);
  }
}
