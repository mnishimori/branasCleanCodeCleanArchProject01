package br.com.tecnoride.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.tecnoride.signup.shared.annotation.DatabaseTest;
import br.com.tecnoride.signup.shared.annotation.IntegrationTest;
import br.com.tecnoride.signup.shared.api.JsonUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@DatabaseTest
class SignupControllerTest {

  private final MockMvc mockMvc;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  SignupControllerTest(MockMvc mockMvc, JdbcTemplate jdbcTemplate) {
    this.mockMvc = mockMvc;
    this.jdbcTemplate = jdbcTemplate;
  }

  private UserInput getUserBy(String email) throws SQLException {
    var query = "SELECT * FROM account WHERE email = ?";
    var resultSet = (ResultSet) jdbcTemplate.queryForRowSet(query, new Object[]{email});
    UserInput user = null;
    if (!resultSet.next()) {
      var name = resultSet.getString("name");
      var userEmail = resultSet.getString("email");
      var cpf = resultSet.getString("cpf");
      var carPlate = resultSet.getString("car_plate");
      var isPassenger = resultSet.getBoolean("is_passenger");
      var isDriver = resultSet.getBoolean("is_driver");
      user = new UserInput(name, userEmail, cpf, carPlate, isPassenger, isDriver);
    }
    resultSet.close();
    return user;
  }

  @Test
  void shouldSignUpNewAccountWhenEmailDoesNotExist() throws Exception {
    var userInputDto = new UserInput("Fulano Tal", "fulano@email.com", "46768134221", "ABC-1234", true, false);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post("/api/signup")
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isCreated())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo("Cadastro realizado com sucesso!");
  }

  @Test
  void shouldSignUpUserNameWithTwoWordsWithUpperLowerCaseCharacters() {
    fail("shouldSignUpUserNameWithTwoWordsWithUpperLowerCaseCharacters");
  }

  @Test
  void shouldSignUpUserEmailIsValid() throws Exception {
    fail("shouldSignUpUserEmailIsValid");
  }

  @Test
  void shouldSignUpUserCpfIsValid() throws Exception {
    fail("shouldSignUpUserCpfIsValid");
  }
}
