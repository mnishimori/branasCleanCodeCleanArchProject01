package br.com.tecnoride.account.presentation.api;

import static br.com.tecnoride.account.domain.message.AccountDomainMessage.CAR_PLATE_IS_INVALID;
import static br.com.tecnoride.account.domain.message.AccountDomainMessage.CPF_IS_INVALID;
import static br.com.tecnoride.account.domain.message.AccountDomainMessage.EMAIL_IS_INVALID;
import static br.com.tecnoride.account.domain.message.AccountDomainMessage.NAME_IS_INVALID_MESSAGE;
import static br.com.tecnoride.account.infrastructure.message.AccountInfrastructureMessage.EMAIL_ALREADY_EXISTS;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.CAR_PLATE;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.CPF;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_BELTRANO;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.FULANO_EMAIL;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_DRIVER;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.IS_PASSENGER;
import static br.com.tecnoride.account.shared.testdata.AccountTestData.createAccountInputDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.tecnoride.account.presentation.dto.AccountInputDto;
import br.com.tecnoride.shared.annotation.DatabaseTest;
import br.com.tecnoride.shared.annotation.IntegrationTest;
import br.com.tecnoride.shared.api.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@DatabaseTest
class PostAccountApiTest {

  private static final String URL_ACCOUNTS = "/api/accounts";
  private final MockMvc mockMvc;
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  PostAccountApiTest(MockMvc mockMvc, JdbcTemplate jdbcTemplate) {
    this.mockMvc = mockMvc;
    this.jdbcTemplate = jdbcTemplate;
  }

  private void insertUser(AccountInputDto accountInputDto) {
    String insertSQL = "INSERT INTO cccat15.account (name, email, cpf, car_plate, is_passenger, is_driver) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(insertSQL, accountInputDto.name(), accountInputDto.email(), accountInputDto.cpf(),
        accountInputDto.carPlate(), accountInputDto.isPassenger(), accountInputDto.isDriver());
  }

  @ParameterizedTest
  @ValueSource(strings = {" ", "fulano", "Fulano", "FULANO", "Fulano beltrano", "Fulano BELTRANO",
      "Fulano Beltrano Ciclano"})
  void shouldReturnNameIsInvalidWhenUserNameDoesNotHaveTwoWordsWithFirstWordCharUpperCaseAndTheRestAreNotLowerCaseChars(
      String name) throws Exception {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, name, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isBadRequest());
    var contentAsString = response.andReturn().getResponse().getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(NAME_IS_INVALID_MESSAGE.formatted(name));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldReturnEmailIsInvalidWhenUserEmailNullOrEmpty(String email) throws Exception {
    var accountInputDto = createAccountInputDto(email, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @ValueSource(strings = {"email.domain.com", " email.domain.com", "@", "1"})
  void shouldReturnEmailIsInvalidWhenUserEmailIsValid(String email) throws Exception {
    var accountInputDto = createAccountInputDto(email, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isBadRequest());
    var contentAsString = response.andReturn().getResponse().getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(EMAIL_IS_INVALID.formatted(email));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"72387289316", "18939181068", "12345678901", "11111111111", "1111111111a"})
  void shouldReturnCpfIsInvalidWhenUserCpfIsValid(String cpf) throws Exception {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, cpf, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isBadRequest());
    var contentAsString = response.andReturn().getResponse().getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(CPF_IS_INVALID.formatted(cpf));
  }

  @Test
  void shouldReturnBadRequestWhenUserIsDriverAndPassengerWasNotFilled() throws Exception {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, false, false);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestWhenUserIsDriverAndPassengerAtTheSameTime() throws Exception {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, true, true);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestWhenUserIsDriverAndCarPlateIsInvalid() throws Exception {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, " ", false, true);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @ValueSource(strings = {"123-1234", "ABC-DEFG", "ABC-D3FG"})
  void shouldReturnCarPlateIsInvalidWhenUserIsDriverAndCarPlateIsInvalid(String carPlate) throws Exception {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, carPlate, false, true);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isBadRequest());
    var contentAsString = response.andReturn().getResponse().getContentAsString();
    assertThat(contentAsString).isNotNull().contains(CAR_PLATE_IS_INVALID.formatted(carPlate));
  }

  @Test
  void shouldReturnEmailAlreadyExistsWhenUserAccountEmailWasFound() throws Exception {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
    var userInput = new AccountInputDto(accountInputDto.email(), accountInputDto.name(), accountInputDto.cpf(),
        accountInputDto.carPlate(), accountInputDto.isPassenger(), accountInputDto.isDriver());
    insertUser(userInput);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isConflict());
    var contentAsString = response.andReturn().getResponse().getContentAsString();
    assertThat(contentAsString).isNotNull().isEqualTo(EMAIL_ALREADY_EXISTS.formatted(accountInputDto.email()));
  }

  @Test
  void shouldSignUpNewAccountWhenUserIsPassengerAndAllAttributesAreCorrect() throws Exception {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, null, IS_PASSENGER, IS_DRIVER);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isCreated());
    response.andExpect(jsonPath("$.id", notNullValue()));
    response.andExpect(jsonPath("$.email", equalTo(accountInputDto.email())));
    response.andExpect(jsonPath("$.name", equalTo(accountInputDto.name())));
    response.andExpect(jsonPath("$.cpf", equalTo(accountInputDto.cpf())));
    response.andExpect(jsonPath("$.carPlate", equalTo(accountInputDto.carPlate())));
    response.andExpect(jsonPath("$.isPassenger", equalTo(accountInputDto.isPassenger())));
    response.andExpect(jsonPath("$.isDriver", equalTo(accountInputDto.isDriver())));
  }

  @Test
  void shouldSignUpNewAccountWhenUserIsDriverAndHasCarPlateAndAllAttributesAreCorrect() throws Exception {
    var accountInputDto = createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, false, true);
    var userJson = JsonUtil.toJson(accountInputDto);

    var request = post(URL_ACCOUNTS)
        .contentType(APPLICATION_JSON)
        .content(userJson);
    var response = mockMvc.perform(request);

    assertThat(response).isNotNull();
    response.andExpect(status().isCreated());
    response.andExpect(jsonPath("$.id", notNullValue()));
    response.andExpect(jsonPath("$.email", equalTo(accountInputDto.email())));
    response.andExpect(jsonPath("$.name", equalTo(accountInputDto.name())));
    response.andExpect(jsonPath("$.cpf", equalTo(accountInputDto.cpf())));
    response.andExpect(jsonPath("$.carPlate", equalTo(accountInputDto.carPlate())));
    response.andExpect(jsonPath("$.isPassenger", equalTo(accountInputDto.isPassenger())));
    response.andExpect(jsonPath("$.isDriver", equalTo(accountInputDto.isDriver())));
  }
}
