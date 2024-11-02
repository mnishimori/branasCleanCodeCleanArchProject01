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
      return jdbcTemplate.queryForObject(query, new AccountRowMapper(), uuid);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public Account findAccountBy(String email) {
    var query = "SELECT * FROM cccat15.account WHERE email = ?";
    try {
      return jdbcTemplate.queryForObject(query, new AccountRowMapper(), email);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public Account save(Account account) {
    String insertSQL = "INSERT INTO cccat15.account (name, email, cpf, car_plate, is_passenger, is_driver) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(insertSQL, account.getAccountName(), account.getEmail(), account.getCpf(),
        account.getCarPlate(), account.isPassenger(), account.isDriver());
    return findAccountBy(account.getEmail());
  }
}
