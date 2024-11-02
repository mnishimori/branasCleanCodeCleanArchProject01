package br.com.tecnoride.account.domain.message;

public final class AccountDomainMessage {

  public static final String ACCOUNT_PASSENGER_OR_DRIVER_WAS_NOT_FILLED_MESSAGE = "It's necessary to indicate if account is for passenger or driver.";
  public static final String USER_CANNOT_BE_PASSENGER_AND_DRIVER_AT_THE_SAME_TIME_MESSAGE = "User cannot be passenger and driver at the same time.";
  public static final String NAME_IS_INVALID_MESSAGE = "Name is invalid. %s";
  public static final String CAR_PLATE_IS_INVALID = "Car plate is invalid. %s";
  public static final String CPF_IS_INVALID = "CPF is invalid. %s";
  public static final String EMAIL_IS_INVALID = "Email is invalid. %s";

  private AccountDomainMessage() {
  }
}
