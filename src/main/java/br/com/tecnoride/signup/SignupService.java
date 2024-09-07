package br.com.tecnoride.signup;

import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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

  private boolean validateCpf(String cpf) {
    // Implementação da validação de CPF
    return true;
  }
}
