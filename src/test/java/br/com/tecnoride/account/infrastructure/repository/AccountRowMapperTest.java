package br.com.tecnoride.account.infrastructure.repository;

import static br.com.tecnoride.account.shared.testdata.AccountTestData.CAR_PLATE;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.CPF;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_BELTRANO;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import br.com.tecnoride.account.domain.entity.Account;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;

class AccountRowMapperTest {

  @Test
  void shouldMapRowToAccount() throws SQLException {
    var resultSet = Mockito.mock(ResultSet.class);
    var accountId = UUID.randomUUID();
    var name = FULANO_BELTRANO;
    var email = FULANO_EMAIL;
    var cpf = CPF;
    var carPlate = CAR_PLATE;
    var isPassenger = false;
    var isDriver = true;

    when(resultSet.getString("account_id")).thenReturn(accountId.toString());
    when(resultSet.getString("name")).thenReturn(name);
    when(resultSet.getString("email")).thenReturn(email);
    when(resultSet.getString("cpf")).thenReturn(cpf);
    when(resultSet.getString("car_plate")).thenReturn(carPlate);
    when(resultSet.getBoolean("is_passenger")).thenReturn(isPassenger);
    when(resultSet.getBoolean("is_driver")).thenReturn(isDriver);

    RowMapper<Account> rowMapper = new AccountRowMapper();
    var account = rowMapper.mapRow(resultSet, 1);

    assertThat(account).isNotNull();
    assertThat(account.getId()).isEqualTo(accountId);
    assertThat(account.getAccountName()).isEqualTo(name);
    assertThat(account.getEmail()).isEqualTo(email);
    assertThat(account.getCpf()).isEqualTo(cpf);
    assertThat(account.getCarPlate()).isEqualTo(carPlate);
    assertThat(account.isPassenger()).isEqualTo(isPassenger);
    assertThat(account.isDriver()).isEqualTo(isDriver);
  }
}
