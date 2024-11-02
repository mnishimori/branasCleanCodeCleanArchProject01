package br.com.tecnoride.account.domain.entity;

import static br.com.tecnoride.account.domain.field.AccountDomainField.ACCOUNT_PASSENGER_OR_DRIVER_FIELD;
import static br.com.tecnoride.account.domain.message.AccountDomainMessage.ACCOUNT_PASSENGER_OR_DRIVER_WAS_NOT_FILLED_MESSAGE;
import static br.com.tecnoride.account.domain.message.AccountDomainMessage.USER_CANNOT_BE_PASSENGER_AND_DRIVER_AT_THE_SAME_TIME_MESSAGE;

import br.com.tecnoride.account.domain.exception.BusinessException;
import br.com.tecnoride.account.domain.valueobject.AccountName;
import br.com.tecnoride.account.domain.valueobject.CarPlate;
import br.com.tecnoride.account.domain.valueobject.Cpf;
import br.com.tecnoride.account.domain.valueobject.Email;
import java.util.UUID;
import org.springframework.validation.FieldError;

public class Account {

  private final UUID id;
  private final Email email;
  private final AccountName accountName;
  private final Cpf cpf;
  private final boolean isPassenger;
  private final boolean isDriver;
  private CarPlate carPlate;

  public Account(String emailAddress, String accountName, String cpfNumber, String carPlate, boolean isPassenger,
      boolean isDriver) {
    this(null, emailAddress, accountName, cpfNumber, carPlate, isPassenger, isDriver);
  }

  public Account(UUID id, String emailAddress, String name, String cpfNumber, String carPlateNumber,
      boolean isPassenger, boolean isDriver) {
    this.id = id;
    this.email = new Email(emailAddress);
    this.accountName = new AccountName(name);
    this.cpf = new Cpf(cpfNumber);
    this.isPassenger = isPassenger;
    this.isDriver = isDriver;
    verifyIfAccountPassengerOrDriverWasFilled();
    verifyIfAccountIsPassengerAndIsDriverAtTheSameTime();
    if (this.isDriver) {
      this.carPlate = new CarPlate(carPlateNumber);
    }
  }

  private void verifyIfAccountPassengerOrDriverWasFilled() {
    if (!this.isPassenger() && !this.isDriver()) {
      throw new BusinessException(new FieldError(this.getClass().getSimpleName(), ACCOUNT_PASSENGER_OR_DRIVER_FIELD,
          ACCOUNT_PASSENGER_OR_DRIVER_WAS_NOT_FILLED_MESSAGE));
    }
  }

  private void verifyIfAccountIsPassengerAndIsDriverAtTheSameTime() {
    if (this.isPassenger() && this.isDriver()) {
      throw new BusinessException(new FieldError(this.getClass().getSimpleName(), ACCOUNT_PASSENGER_OR_DRIVER_FIELD,
          USER_CANNOT_BE_PASSENGER_AND_DRIVER_AT_THE_SAME_TIME_MESSAGE));
    }
  }

  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email.emailAddress();
  }

  public String getAccountName() {
    return accountName.name();
  }

  public String getCpf() {
    return cpf.cpfNumber();
  }

  public boolean isPassenger() {
    return isPassenger;
  }

  public boolean isDriver() {
    return isDriver;
  }

  public String getCarPlate() {
    return carPlate != null ? carPlate.carPlateNumber() : null;
  }
}
