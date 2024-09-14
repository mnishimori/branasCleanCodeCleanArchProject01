package br.com.tecnoride.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.tecnoride.signup.shared.annotation.DatabaseTest;
import br.com.tecnoride.signup.shared.annotation.IntegrationTest;
import br.com.tecnoride.signup.shared.api.JsonUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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

  private void insertUser(UserInput input) {
    String insertSQL = "INSERT INTO cccat15.account (name, email, cpf, car_plate, is_passenger, is_driver) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(insertSQL, input.getName(), input.getEmail(), input.getCpf(),
        input.getCarPlate(), input.getIsPassenger(), input.getIsDriver());
  }

  @ParameterizedTest
  @ValueSource(strings = {"fulano", "Fulano", "FULANO", "Fulano beltrano", "Fulano BELTRANO",
      "Fulano Beltrano Ciclano"})
  void shouldReturnNomeInvalidoWhenUserNameDoesNotHaveTwoWordsWithFirstWordCharUpperCaseAndTheRestAreNotLowerCaseChars(
      String name) throws Exception {
    var userInputDto = new UserInput(name, "fulano@email.com", "46768134221", "ABC-1234", true, false);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post("/api/signup")
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo("Nome inválido.");
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"email.domain.com", " email.domain.com", "@", "1"})
  void shouldReturnEmailInvalidoWhenUserEmailIsValid(String email) throws Exception {
    var userInputDto = new UserInput("Fulano Beltrano", email, "46768134221", "ABC-1234", true, false);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post("/api/signup")
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo("Email inválido.");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"72387289316", "18939181068", "12345678901", "11111111111", "1111111111a"})
  void shouldReturnCpfInvalidoWhenUserCpfIsValid(String cpf) throws Exception {
    var userInputDto = new UserInput("Fulano Beltrano", "email@domain.com", cpf, "ABC-1234", true, false);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post("/api/signup")
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo("CPF inválido.");
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"123-1234", "ABC-DEFG", "ABC-D3FG"})
  void shouldReturnPlacaDeCarroInvalidaWhenIsDriverAndCarPlateIsInvalid(String carPlate) throws Exception {
    var userInputDto = new UserInput("Fulano Beltrano", "email@domain.com", "46768134221", carPlate, false, true);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post("/api/signup")
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo("Placa de carro inválida.");
  }

  @Test
  void shouldReturnUsuarioJaExisteWhenUserEmailAlreadyExists() throws Exception {
    var userInputDto = new UserInput("Fulano Tal", "fulano@email.com", "46768134221", "ABC-1234", true, false);
    var userInput = new UserInput(userInputDto.getName(), userInputDto.getEmail(), userInputDto.getCpf(),
        userInputDto.getCarPlate(), userInputDto.getIsPassenger(), userInputDto.getIsDriver());
    insertUser(userInput);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post("/api/signup")
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo("Usuário já existe.");
  }

  @Test
  void shouldSignUpNewAccountWhenAllAttributesAreCorrect() throws Exception {
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
}
