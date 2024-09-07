package br.com.tecnoride.signup;

public class UserInput {

  private String name;
  private String email;
  private String cpf;
  private String carPlate;
  private boolean isPassenger;
  private boolean isDriver;

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

  public boolean isPassenger() {
    return isPassenger;
  }

  public boolean isDriver() {
    return isDriver;
  }
}
