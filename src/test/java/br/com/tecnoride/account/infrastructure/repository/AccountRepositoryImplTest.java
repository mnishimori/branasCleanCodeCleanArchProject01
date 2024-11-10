package br.com.tecnoride.account.infrastructure.repository;

import static br.com.tecnoride.account.shared.testdata.AccountTestData.CAR_PLATE;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.CPF;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_BELTRANO;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_EMAIL;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_DRIVER;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_PASSENGER;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.createAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.domain.valueobject.AccountName;
import br.com.tecnoride.account.domain.valueobject.Cpf;
import br.com.tecnoride.account.domain.valueobject.Email;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;
  @InjectMocks
  private AccountRepositoryImpl accountDao;

  @Test
  void shouldThrowExceptionWhenAccountDoesNotExist() {
    var uuid = UUID.randomUUID();
    when(jdbcTemplate.queryForObject(anyString(), any(AccountRowMapper.class), eq(uuid)))
        .thenThrow(EmptyResultDataAccessException.class);

    var account = accountDao.findAccountBy(uuid);

    assertThat(account).isNull();
  }

  @Test
  void shouldNotFindAccountByIdWhenAccountDoesNotExist() {
    var uuid = UUID.randomUUID();
    when(jdbcTemplate.queryForObject(anyString(), any(AccountRowMapper.class), eq(uuid)))
        .thenThrow(new EmptyResultDataAccessException(1));

    var account = accountDao.findAccountBy(uuid);

    assertThat(account).isNull();
  }

  @Test
  void shouldThrowExceptionWhenDatabaseErrorOccurs() {
    var uuid = UUID.randomUUID();
    when(jdbcTemplate.queryForObject(anyString(), any(AccountRowMapper.class), eq(uuid)))
        .thenThrow(new RuntimeException("Database error"));

    assertThatThrownBy(() -> accountDao.findAccountBy(uuid))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Database error");
  }

  @Test
  void shouldFindAccountByIdWhenAccountIdExists() {
    var uuid = UUID.randomUUID();
    var expectedAccount = createAccount(uuid, FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    when(jdbcTemplate.queryForObject(anyString(), any(AccountRowMapper.class), eq(uuid)))
        .thenReturn(expectedAccount);

    var actualAccount = accountDao.findAccountBy(uuid);

    assertThat(actualAccount).isNotNull();
    assertThat(expectedAccount).isNotNull().usingRecursiveComparison().isEqualTo(actualAccount);
  }

  @Test
  void shouldThrowExceptionWhenFindAccountByEmailDoesNotExist() {
    when(jdbcTemplate.queryForObject(anyString(), any(AccountRowMapper.class), anyString()))
        .thenThrow(EmptyResultDataAccessException.class);

    var account = accountDao.findAccountBy(FULANO_EMAIL);

    assertThat(account).isNull();
  }

  @Test
  void shouldNotFindAccountByEmailWhenAccountDoesNotExist() {
    when(jdbcTemplate.queryForObject(anyString(), any(AccountRowMapper.class), anyString()))
        .thenThrow(new EmptyResultDataAccessException(1));

    var account = accountDao.findAccountBy(FULANO_EMAIL);

    assertThat(account).isNull();
  }

  @Test
  void shouldFindAccountByEmailWhenAccountEmailExists() {
    var uuid = UUID.randomUUID();
    var email = new Email(FULANO_EMAIL);
    var name = new AccountName(FULANO_BELTRANO);
    var cpf = new Cpf(CPF);
    var expectedAccount = new Account(uuid, email, name, cpf, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    when(jdbcTemplate.queryForObject(anyString(), any(AccountRowMapper.class), eq(FULANO_EMAIL)))
        .thenReturn(expectedAccount);

    var actualAccount = accountDao.findAccountBy(FULANO_EMAIL);

    assertThat(actualAccount).isNotNull();
    assertThat(expectedAccount).isNotNull().usingRecursiveComparison().isEqualTo(actualAccount);
  }

  @Test
  void shouldSaveAccountWhenUserIsPassengerAndAllAttributesAreCorrect() {
    var uuid = UUID.randomUUID();
    var email = new Email(FULANO_EMAIL);
    var name = new AccountName(FULANO_BELTRANO);
    var cpf = new Cpf(CPF);
    var account = new Account(uuid, email, name, cpf, null, true, false);
    String insertSQL = "INSERT INTO cccat15.account (name, email, cpf, car_plate, is_passenger, is_driver) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    when(jdbcTemplate.update(insertSQL, account.getAccountName(), account.getEmail(), account.getCpf(),
        null, account.isPassenger(), account.isDriver())).thenReturn(1);

    assertThatCode(() -> accountDao.save(account)).doesNotThrowAnyException();
  }

  @Test
  void shouldSaveAccountWhenUserIsDriverAndAllAttributesAreCorrect() {
    var uuid = UUID.randomUUID();
    var email = new Email(FULANO_EMAIL);
    var name = new AccountName(FULANO_BELTRANO);
    var cpf = new Cpf(CPF);
    var account = new Account(uuid, email, name, cpf, CAR_PLATE, false, true);
    String insertSQL = "INSERT INTO cccat15.account (name, email, cpf, car_plate, is_passenger, is_driver) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    when(jdbcTemplate.update(insertSQL, account.getAccountName(), account.getEmail(), account.getCpf(),
        account.getCarPlate(), account.isPassenger(), account.isDriver())).thenReturn(1);

    assertThatCode(() -> accountDao.save(account)).doesNotThrowAnyException();
  }
}
