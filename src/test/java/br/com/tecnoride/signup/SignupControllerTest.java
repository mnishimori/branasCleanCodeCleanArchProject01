package br.com.tecnoride.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.tecnoride.signup.shared.annotation.DatabaseTest;
import br.com.tecnoride.signup.shared.annotation.IntegrationTest;
import br.com.tecnoride.signup.shared.api.JsonUtil;
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

  public static final String FULANO_EMAIL = "fulano@email.com";
  public static final String CPF = "46768134221";
  public static final String CAR_PLATE = "ABC-1234";
  public static final boolean IS_PASSENGER = true;
  public static final boolean IS_DRIVER = false;
  public static final String FULANO_BELTRANO = "Fulano Beltrano";
  public static final String URL_API_SIGNUP = "/api/signup";
  public static final String NOME_INVALIDO = "Nome inválido.";
  public static final String EMAIL_INVALIDO = "Email inválido.";
  public static final String CPF_INVALIDO = "CPF inválido.";
  public static final String PLACA_DE_CARRO_INVALIDA = "Placa de carro inválida.";
  public static final String USUÁRIO_JA_EXISTE = "Usuário já existe.";
  public static final String CADASTRO_REALIZADO_COM_SUCESSO = "Cadastro realizado com sucesso!";
  private final MockMvc mockMvc;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  SignupControllerTest(MockMvc mockMvc, JdbcTemplate jdbcTemplate) {
    this.mockMvc = mockMvc;
    this.jdbcTemplate = jdbcTemplate;
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
    var userInputDto = new UserInput(name, FULANO_EMAIL, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post(URL_API_SIGNUP)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(NOME_INVALIDO);
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"email.domain.com", " email.domain.com", "@", "1"})
  void shouldReturnEmailInvalidoWhenUserEmailIsValid(String email) throws Exception {
    var userInputDto = new UserInput(FULANO_BELTRANO, email, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post(URL_API_SIGNUP)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(EMAIL_INVALIDO);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"72387289316", "18939181068", "12345678901", "11111111111", "1111111111a"})
  void shouldReturnCpfInvalidoWhenUserCpfIsValid(String cpf) throws Exception {
    var userInputDto = new UserInput(FULANO_BELTRANO, FULANO_EMAIL, cpf, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post(URL_API_SIGNUP)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(CPF_INVALIDO);
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"123-1234", "ABC-DEFG", "ABC-D3FG"})
  void shouldReturnPlacaDeCarroInvalidaWhenIsDriverAndCarPlateIsInvalid(String carPlate) throws Exception {
    var userInputDto = new UserInput(FULANO_BELTRANO, FULANO_EMAIL, CPF, carPlate, false, true);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post(URL_API_SIGNUP)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(PLACA_DE_CARRO_INVALIDA);
  }

  @Test
  void shouldReturnUsuarioJaExisteWhenUserEmailAlreadyExists() throws Exception {
    var userInputDto = new UserInput(FULANO_BELTRANO, FULANO_EMAIL, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userInput = new UserInput(userInputDto.getName(), userInputDto.getEmail(), userInputDto.getCpf(),
        userInputDto.getCarPlate(), userInputDto.getIsPassenger(), userInputDto.getIsDriver());
    insertUser(userInput);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post(URL_API_SIGNUP)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isBadRequest())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(USUÁRIO_JA_EXISTE);
  }

  @Test
  void shouldSignUpNewAccountWhenAllAttributesAreCorrect() throws Exception {
    var userInputDto = new UserInput(FULANO_BELTRANO, FULANO_EMAIL, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userJson = JsonUtil.toJson(userInputDto);

    var request = post(URL_API_SIGNUP)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request)
        .andExpect(status().isCreated())
        .andReturn().getResponse();

    assertThat(response).isNotNull();
    var contentAsString = response.getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(CADASTRO_REALIZADO_COM_SUCESSO);
  }
}
