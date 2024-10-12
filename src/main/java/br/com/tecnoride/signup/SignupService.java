package br.com.tecnoride.signup;

import java.util.regex.Pattern;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

  private final JdbcTemplate jdbcTemplate;

  public SignupService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void signup(UserInput input) {
    verifyIfUserExists(input);
    userNamePatternIsInvalid(input.getName());
    emailPatternIsInvalid(input.getEmail());
    cpfPatternIsInvalid(input.getCpf());
    userIsDriverAndCarPlatePatternIsInvalid(input);
    insertAccount(input);
  }

  private void userIsDriverAndCarPlatePatternIsInvalid(UserInput input) {
    var userDriverCarPlateInvalid = input.getIsDriver() && !Pattern.matches("[A-Z]{3}[0-9]{4}", input.getCarPlate());
    if (userDriverCarPlateInvalid) {
      throw new RuntimeException("Placa de carro inválida.");
    }
  }

  private void emailPatternIsInvalid(String email) {
    var emailInvalid = !Pattern.matches("^(.+)@(.+)$", email);
    if (emailInvalid) {
      throw new RuntimeException("Email inválido.");
    }
  }

  private void userNamePatternIsInvalid(String name) {
    var userNameInvalid = !Pattern.matches("[A-Z][a-z]+ [A-Z][a-z]+", name);
    if (userNameInvalid) {
      throw new RuntimeException("Nome inválido.");
    }
  }

  private void verifyIfUserExists(UserInput input) {
    String query = "SELECT * FROM cccat15.account WHERE email = ?";
    var userExists = !jdbcTemplate.query(query,
            (rs, rowNum) -> new UserInput(rs.getString("name"), rs.getString("email"), rs.getString("cpf"),
                rs.getString("car_plate"), rs.getBoolean("is_passenger"), rs.getBoolean("is_driver")), input.getEmail())
        .isEmpty();
    if (userExists) {
      throw new RuntimeException("Usuário já existe.");
    }
  }

  private void insertAccount(UserInput input) {
    String insertSQL = "INSERT INTO cccat15.account (name, email, cpf, car_plate, is_passenger, is_driver) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(insertSQL, input.getName(), input.getEmail(), input.getCpf(),
        input.getCarPlate(), input.getIsPassenger(), input.getIsDriver());
  }

  private void cpfPatternIsInvalid(String cpfValue) {
    var cpfValid = this.validateNotNullOf(cpfValue);
    cpfValid = cpfValid && this.validateNotEmptyOf(cpfValue);
    cpfValid = cpfValid && this.validateLengthOf(cpfValue);
    cpfValid = cpfValid && this.validate(cpfValue);
    if (!cpfValid) {
      throw new RuntimeException("CPF inválido.");
    }
  }

  private boolean validate(String rawCpf) {
    String cpf = removeNonDigits(rawCpf);
    if (hasAllDigitsEqual(cpf)) {
      return false;
    }
    int digit1 = calculateDigit(cpf, 10);
    int digit2 = calculateDigit(cpf, 11);
    return extractDigit(cpf).equals(String.valueOf(digit1) + digit2);
  }

  private String extractDigit(String cpf) {
    return cpf.substring(9);
  }

  private int calculateDigit(String cpf, int factor) {
    int total = 0;
    for (char digit : cpf.toCharArray()) {
      if (factor > 1) {
        total += Character.getNumericValue(digit) * factor--;
      }
    }
    int rest = total % 11;
    return (rest < 2) ? 0 : 11 - rest;
  }

  private boolean hasAllDigitsEqual(String cpf) {
    char firstCpfDigit = cpf.charAt(0);
    return cpf.chars().allMatch(digit -> digit == firstCpfDigit);
  }

  private String removeNonDigits(String cpf) {
    return cpf.replaceAll("\\D", "");
  }

  private boolean validateLengthOf(String cpfValue) {
    return cpfValue.trim().length() == 11;
  }

  private boolean validateNotEmptyOf(String cpfValue) {
    return !cpfValue.trim().isEmpty();
  }

  private boolean validateNotNullOf(String cpfValue) {
    return cpfValue != null;
  }
}
