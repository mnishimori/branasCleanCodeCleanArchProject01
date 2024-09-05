package br.com.tecnoride.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class MainController {

    @Autowired
    private SignupService signupService;

    @PostMapping
    public String signup(@RequestBody UserInput input) {
        try {
            int result = signupService.signup(input);
            switch (result) {
                case 1: return "Cadastro realizado com sucesso!";
                case -1: return "CPF inválido.";
                case -2: return "Email inválido.";
                case -3: return "Nome inválido.";
                case -4: return "Usuário já existe.";
                case -5: return "Placa de carro inválida.";
                default: return "Erro desconhecido.";
            }
        } catch (Exception e) {
            return "Erro ao processar: " + e.getMessage();
        }
    }
}
