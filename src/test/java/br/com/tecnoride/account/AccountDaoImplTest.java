package br.com.tecnoride.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
class AccountDaoImplTest {

  public static final String FULANO_CICLANO_NAME = "Fulano Ciclano";
  public static final String FULANO_CICLANO_EMAIL = "fulano.ciclano@domain.com";
  public static final String FULANO_CICLANO_CPF = "62835691022";
  public static final String CAR_PLATE = "ABC-1234";
  public static final boolean IS_PASSENGER = true;
  public static final boolean IS_DRIVER = false;
  @Mock
  private JdbcTemplate jdbcTemplate;
  @InjectMocks
  private AccountDaoImpl accountDao;

  @Test
  void shouldNotFindAccountByIdWhenAccountDoesNotExist() {
    var uuid = UUID.randomUUID();
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(AccountRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    var account = accountDao.findAccountBy(uuid);

    assertThat(account).isNull();
  }

  @Test
  void shouldThrowExceptionWhenDatabaseErrorOccurs() {
    var uuid = UUID.randomUUID();
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(AccountRowMapper.class)))
        .thenThrow(new RuntimeException("Database error"));

    assertThatThrownBy(() -> accountDao.findAccountBy(uuid))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Database error");
  }

  @Test
  void shouldFindAccountByIdWhenAccountIdExists() {
    var uuid = UUID.randomUUID();
    var expectedAccount = new Account(uuid, FULANO_CICLANO_NAME, FULANO_CICLANO_EMAIL, FULANO_CICLANO_CPF, CAR_PLATE,
        IS_PASSENGER, IS_DRIVER);
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(AccountRowMapper.class)))
        .thenReturn(expectedAccount);

    var actualAccount = accountDao.findAccountBy(uuid);

    assertThat(actualAccount).isNotNull();
    assertThat(expectedAccount).isNotNull().usingRecursiveComparison().isEqualTo(actualAccount);
  }

  @Test
  void shouldNotFindAccountByEmailWhenAccountDoesNotExist() {
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(AccountRowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    var account = accountDao.findAccountBy(FULANO_CICLANO_EMAIL);

    assertThat(account).isNull();
  }

  @Test
  void shouldFindAccountByEmailWhenAccountEmailExists() {
    var uuid = UUID.randomUUID();
    var expectedAccount = new Account(uuid, FULANO_CICLANO_NAME, FULANO_CICLANO_EMAIL, FULANO_CICLANO_CPF, CAR_PLATE,
        IS_PASSENGER, IS_DRIVER);
    when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(AccountRowMapper.class)))
        .thenReturn(expectedAccount);

    var actualAccount = accountDao.findAccountBy(FULANO_CICLANO_EMAIL);

    assertThat(actualAccount).isNotNull();
    assertThat(expectedAccount).isNotNull().usingRecursiveComparison().isEqualTo(actualAccount);
  }
}
