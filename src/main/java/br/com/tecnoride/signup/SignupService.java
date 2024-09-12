package br.com.tecnoride.signup;

import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

@Service
public class SignupService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public int signup(UserInput input) {

    String id = UUID.randomUUID().toString();

    // Verifica se o email já existe
    try {
      String query = "SELECT * FROM cccat15.account WHERE email = ?";
      boolean empty = jdbcTemplate.query(query,
              (rs, rowNum) -> new UserInput(rs.getString("name"), rs.getString("email"), rs.getString("cpf"),
                  rs.getString("car_plate"), rs.getBoolean("is_passenger"), rs.getBoolean("is_driver")), input.getEmail())
          .isEmpty();
      if (empty) {
        if (Pattern.matches("[A-Z][a-z]+ [A-Z][a-z]+", input.getName())) {
          if (Pattern.matches("^(.+)@(.+)$", input.getEmail())) {
            if (validateCpf(input.getCpf())) {
              if (input.isDriver()) {
                if (Pattern.matches("[A-Z]{3}[0-9]{4}", input.getCarPlate())) {
                  insertAccount(input);
                  return 1; // Sucesso
                } else {
                  return -5; // Placa de carro inválida
                }
              } else {
                insertAccount(input);
                return 1; // Sucesso
              }
            } else {
              return -1; // CPF inválido
            }
          } else {
            return -2; // Email inválido
          }
        } else {
          return -3; // Nome inválido
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  private void insertAccount(UserInput input) {
    String insertSQL = "INSERT INTO cccat15.account (name, email, cpf, car_plate, is_passenger, is_driver) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(insertSQL, input.getName(), input.getEmail(), input.getCpf(),
        input.getCarPlate(), input.isPassenger(), input.isDriver());
  }

  private Boolean validateCpf(String cpfValue) {
    var isValid = this.validateNotNullOf(cpfValue);
    isValid = isValid && this.validateNotEmptyOf(cpfValue);
    isValid = isValid && this.validateLengthOf(cpfValue);
    isValid = isValid && this.validate(cpfValue);
    return isValid;
  }

  private Boolean validate(String rawCpf) {
    String cpf = removeNonDigits(rawCpf);
    if (hasAllDigitsEqual(cpf)) {
      return false;
    }
    int digit1 = calculateDigit(cpf, 10);
    int digit2 = calculateDigit(cpf, 11);
    if (!extractDigit(cpf).equals(String.valueOf(digit1) + digit2)) {
      return false;
    }
    return true;
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

  private Boolean hasAllDigitsEqual(String cpf) {
    char firstCpfDigit = cpf.charAt(0);
    return cpf.chars().allMatch(digit -> digit == firstCpfDigit);
  }

  private String removeNonDigits(String cpf) {
    return cpf.replaceAll("\\D", "");
  }


  private Boolean validateLengthOf(String cpfValue) {
    return cpfValue.trim().length() == 11;
  }

  private Boolean validateNotEmptyOf(String cpfValue) {
    return !cpfValue.trim().isEmpty();
  }

  private Boolean validateNotNullOf(String cpfValue) {
    return cpfValue != null;
  }
}
