package br.com.tecnoride.account.domain.entity;

import java.util.UUID;

public class Account {

  private final UUID id;
  private final String email;
  private final String name;
  private final String cpf;
  private final String carPlate;
  private final boolean isPassenger;
  private final boolean isDriver;

  public Account(UUID id, String email, String name, String cpf, String carPlate, boolean isPassenger, boolean isDriver) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.cpf = cpf;
    this.carPlate = carPlate;
    this.isPassenger = isPassenger;
    this.isDriver = isDriver;
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
