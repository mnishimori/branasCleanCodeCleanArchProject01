package br.com.tecnoride.account.infrastructure.repository;

import br.com.tecnoride.account.domain.entity.Account;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

  private final JdbcTemplate jdbcTemplate;

  public AccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Account findAccountBy(UUID uuid) {
    var query = "SELECT * FROM cccat15.account WHERE account_id = ?";
    try {
      var accounts = jdbcTemplate.query(query, new Object[]{uuid}, new AccountRowMapper());
      return accounts.isEmpty() ? null : accounts.get(0);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public Account findAccountBy(String email) {
    var query = "SELECT * FROM account WHERE email = ?";
    try {
      var accounts = jdbcTemplate.query(query, new Object[]{email}, new AccountRowMapper());
      return accounts.isEmpty() ? null : accounts.get(0);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }
}
