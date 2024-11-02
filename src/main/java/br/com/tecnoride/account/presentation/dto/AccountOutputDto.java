package br.com.tecnoride.account.presentation.dto;

import br.com.tecnoride.account.domain.entity.Account;
import java.util.UUID;

public record AccountOutputDto(UUID id, String email, String name, String cpf, String carPlate, boolean isPassenger,
                               boolean isDriver) {
  public static AccountOutputDto from(Account account) {
    return new AccountOutputDto(account.getId(), account.getEmail(), account.getAccountName(), account.getCpf(),
        account.getCarPlate(), account.isPassenger(), account.isDriver());
  }
}
