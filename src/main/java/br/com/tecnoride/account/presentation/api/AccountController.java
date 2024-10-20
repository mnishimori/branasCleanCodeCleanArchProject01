package br.com.tecnoride.account.presentation.api;

import br.com.tecnoride.account.application.usecase.GetAccountByIdUseCase;
import br.com.tecnoride.account.presentation.dto.AccountOutputDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

  private final GetAccountByIdUseCase getAccountByIdUseCase;

  public AccountController(GetAccountByIdUseCase getAccountByIdUseCase) {
    this.getAccountByIdUseCase = getAccountByIdUseCase;
  }

  @GetMapping("/{id}")
  public AccountOutputDto getAccountById(@PathVariable String id) {
    var account = getAccountByIdUseCase.execute(id);
    return AccountOutputDto.from(account);
  }
}
