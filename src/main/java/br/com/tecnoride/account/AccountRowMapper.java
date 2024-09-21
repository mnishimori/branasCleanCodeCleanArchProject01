package br.com.tecnoride.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class AccountRowMapper implements RowMapper<Account> {

  @Override
  public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
    var id = UUID.fromString(rs.getString("id"));
    var name = rs.getString("name");
    var email = rs.getString("email");
    var cpf = rs.getString("cpf");
    var carPlate = rs.getString("car_plate");
    var isPassenger = rs.getBoolean("is_passenger");
    var isDriver = rs.getBoolean("is_driver");
    return new Account(id, name, email, cpf, carPlate, isPassenger, isDriver);
  }
}
