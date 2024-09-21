package br.com.tecnoride.signup;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.tecnoride.signup.shared.annotation.DatabaseTest;
import br.com.tecnoride.signup.shared.annotation.IntegrationTest;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@DatabaseTest
class GetAccountControllerTest {

  public static final String URL_ACCOUNT_ID = "/api/account/%s";
  private final MockMvc mockMvc;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  GetAccountControllerTest(MockMvc mockMvc, JdbcTemplate jdbcTemplate) {
    this.mockMvc = mockMvc;
    this.jdbcTemplate = jdbcTemplate;
  }

  private String insertUser(UserInput input) {
    var id = UUID.randomUUID().toString();
    String insertSQL = "INSERT INTO cccat15.account (id, name, email, cpf, car_plate, is_passenger, is_driver) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(insertSQL, id, input.getName(), input.getEmail(), input.getCpf(),
        input.getCarPlate(), input.getIsPassenger(), input.getIsDriver());
    return id;
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"a", "1", "a1"})
  void shouldReturnBadRequestWhenAccountIdIsInvalidUuid(String id) throws Exception {
    Assertions.fail("shouldReturnBadRequestWhenAccountIdIsInvalidUuid");
  }

  @Test
  void shouldReturnNotFoundWhenAccountIdWasNotFound() throws Exception {
    Assertions.fail("shouldReturnNotFoundWhenAccountIdWasNotFound");
  }

  @Test
  void shouldReturnAccountWhenAccountWasFoundById() throws Exception {
    var userInputDto = new UserInput("Fulano Ciclano", "fulano@email.com", "46768134221", "ABC-1234", true, false);
    var id = insertUser(userInputDto);

    var request = get(URL_ACCOUNT_ID.formatted(id));
    var response = mockMvc.perform(request);

    response.andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON));
    response
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name", equalTo(userInputDto.getName())))
        .andExpect(jsonPath("$.email", equalTo(userInputDto.getEmail())));
  }
}
