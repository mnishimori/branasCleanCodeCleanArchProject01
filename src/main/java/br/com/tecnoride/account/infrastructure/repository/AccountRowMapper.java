package br.com.tecnoride.account.infrastructure.repository;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.domain.valueobject.AccountName;
import br.com.tecnoride.account.domain.valueobject.Cpf;
import br.com.tecnoride.account.domain.valueobject.Email;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class AccountRowMapper implements RowMapper<Account> {

  @Override
  public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
    var id = UUID.fromString(rs.getString("account_id"));
    var email = new Email(rs.getString("email"));
    var name = new AccountName(rs.getString("name"));
    var cpf = new Cpf(rs.getString("cpf"));
    var carPlate = rs.getString("car_plate");
    var isPassenger = rs.getBoolean("is_passenger");
    var isDriver = rs.getBoolean("is_driver");
    return new Account(id, email, name, cpf, carPlate, isPassenger, isDriver);
  }
}
