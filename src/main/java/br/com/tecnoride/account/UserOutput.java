package br.com.tecnoride.account;

import java.util.UUID;

public class UserOutput {

  private final UUID id;
  private final String email;
  private final String name;
  private final String cpf;
  private final String carPlate;
  private final boolean isPassenger;
  private final boolean isDriver;

  public UserOutput(UUID id, String email, String name, String cpf, String carPlate, boolean isPassenger,
      boolean isDriver) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.cpf = cpf;
    this.carPlate = carPlate;
    this.isPassenger = isPassenger;
    this.isDriver = isDriver;
  }

  public static UserOutput from(Account account) {
    return new UserOutput(account.getId(), account.getEmail(), account.getName(), account.getCpf(),
        account.getCarPlate(), account.isPassenger(), account.isDriver());
  }

  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getCpf() {
    return cpf;
  }

  public String getCarPlate() {
    return carPlate;
  }

  public boolean isPassenger() {
    return isPassenger;
  }

  public boolean isDriver() {
    return isDriver;
  }
}
