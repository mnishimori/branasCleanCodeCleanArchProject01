package br.com.tecnoride.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signup")
public class SignupController {

  @Autowired
  private SignupService signupService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String signup(@RequestBody UserInput input) {
    signupService.signup(input);
    return "Cadastro realizado com sucesso!";
  }
}
