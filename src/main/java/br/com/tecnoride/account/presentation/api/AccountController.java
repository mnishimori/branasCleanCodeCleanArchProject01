package br.com.tecnoride.account.presentation.api;

import br.com.tecnoride.account.application.usecase.CreateAccountUseCase;
import br.com.tecnoride.account.application.usecase.GetAccountByIdUseCase;
import br.com.tecnoride.account.presentation.dto.AccountInputDto;
import br.com.tecnoride.account.presentation.dto.AccountOutputDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

  private final GetAccountByIdUseCase getAccountByIdUseCase;
  private final CreateAccountUseCase createAccountUseCase;

  public AccountController(GetAccountByIdUseCase getAccountByIdUseCase, CreateAccountUseCase createAccountUseCase) {
    this.getAccountByIdUseCase = getAccountByIdUseCase;
    this.createAccountUseCase = createAccountUseCase;
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public AccountOutputDto getAccountById(@PathVariable String id) {
    var account = getAccountByIdUseCase.execute(id);
    return AccountOutputDto.from(account);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AccountOutputDto postAccount(@RequestBody AccountInputDto accountInputDto) {
    var account = createAccountUseCase.execute(accountInputDto);
    return AccountOutputDto.from(account);
  }
}
