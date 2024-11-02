package br.com.tecnoride.account.shared.testdata;

import br.com.tecnoride.account.domain.entity.Account;
import br.com.tecnoride.account.presentation.dto.AccountInputDto;
import java.util.UUID;

public final class AccountTestData {

  public static final String FULANO_EMAIL = "fulano.beltrano@email.com";
  public static final String CPF = "46768134221";
  public static final String CAR_PLATE = "ABC-1234";
  public static final boolean IS_PASSENGER = true;
  public static final boolean IS_DRIVER = false;
  public static final String FULANO_BELTRANO = "Fulano Beltrano";

  public static AccountInputDto createAccountInputDto() {
    return createAccountInputDto(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
  }

  public static AccountInputDto createAccountInputDto(String email, String name, String cpf, String carPlate,
      boolean isPassenger, boolean isDriver) {
    return new AccountInputDto(email, name, cpf, carPlate, isPassenger, isDriver);
  }

  public static Account createAccount() {
    return createAccount(FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
  }

  public static Account createAccountSaved(UUID id) {
    return createAccount(id, FULANO_EMAIL, FULANO_BELTRANO, CPF, CAR_PLATE, IS_PASSENGER, IS_DRIVER);
  }

  public static Account createAccount(String email, String name, String cpf, String carPlate,
      boolean isPassenger, boolean isDriver) {
    return new Account(UUID.randomUUID(), email, name, cpf, carPlate, isPassenger, isDriver);
  }

  public static Account createAccount(UUID uuid, String email, String name, String cpf, String carPlate,
      boolean isPassenger, boolean isDriver) {
    return new Account(uuid, email, name, cpf, carPlate, isPassenger, isDriver);
  }
}
