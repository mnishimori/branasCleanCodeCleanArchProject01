package br.com.tecnoride.signup;

public class UserInput {

  private final String email;
  private final String name;
  private final String cpf;
  private final String carPlate;
  private final boolean isPassenger;
  private final boolean isDriver;

  public UserInput(String name, String email, String cpf, String carPlate, boolean isPassenger, boolean isDriver) {
    this.name = name;
    this.email = email;
    this.cpf = cpf;
    this.carPlate = carPlate;
    this.isPassenger = isPassenger;
    this.isDriver = isDriver;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getCpf() {
    return cpf;
  }

  public String getCarPlate() {
    return carPlate;
  }

  public boolean getIsPassenger() {
    return isPassenger;
  }

  public boolean getIsDriver() {
    return isDriver;
  }
}
