package br.com.tecnoride.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class SignupService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int signup(UserInput input) throws SQLException {

        String id = UUID.randomUUID().toString();

        // Verifica se o email já existe
        String query = "SELECT * FROM account WHERE email = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForRowSet(query, new Object[]{input.getEmail()});

        if (!rs.next()) {
            if (Pattern.matches("[a-zA-Z] [a-zA-Z]+", input.getName())) {
                if (Pattern.matches("^(.+)@(.+)$", input.getEmail())) {
                    if (validateCpf(input.getCpf())) {
                        if (input.isDriver()) {
                            if (Pattern.matches("[A-Z]{3}[0-9]{4}", input.getCarPlate())) {
                                insertAccount(id, input);
                                return 1; // Sucesso
                            } else {
                                return -5; // Placa de carro inválida
                            }
                        } else {
                            insertAccount(id, input);
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
        } else {
            return -4; // Usuário já existe
        }
    }

    private void insertAccount(String id, UserInput input) {
        String insertSQL = "INSERT INTO account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertSQL, id, input.getName(), input.getEmail(), input.getCpf(),
                            input.getCarPlate(), input.isPassenger(), input.isDriver());
    }

    private boolean validateCpf(String cpf) {
        // Implementação da validação de CPF
        return true;
    }
}
