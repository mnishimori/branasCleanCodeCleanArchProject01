package br.com.tecnoride.account;

import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDaoImpl implements AccountDao {

  private final JdbcTemplate jdbcTemplate;

  public AccountDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Account findAccountBy(UUID uuid) {
    var query = "SELECT * FROM account WHERE id = ?";
    try {
      return jdbcTemplate.queryForObject(query, new Object[]{uuid}, new AccountRowMapper());
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  public Account findAccountBy(String email) {
    var query = "SELECT * FROM account WHERE id = ?";
    try {
      return jdbcTemplate.queryForObject(query, new Object[]{email}, new AccountRowMapper());
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }
}
