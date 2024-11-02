package br.com.tecnoride.account.domain.valueobject;

import static br.com.tecnoride.account.domain.field.AccountDomainField.ACCOUNT_CAR_PLATE_FIELD;
import static br.com.tecnoride.account.domain.message.AccountDomainMessage.CAR_PLATE_IS_INVALID;

import br.com.tecnoride.account.domain.exception.BusinessException;
import java.util.regex.Pattern;
import org.springframework.validation.FieldError;

public record CarPlate(String carPlateNumber) {

  public CarPlate {
    validateIfCarPlateIsNullOrEmpty(carPlateNumber);
    validateIfCarPlatePatternMatches(carPlateNumber);
  }

  private void validateIfCarPlatePatternMatches(String carPlateNumber) {
    boolean carPlatePatternMatches = Pattern.matches("[A-Z]{3}-\\d{4}", carPlateNumber);
    if (!carPlatePatternMatches) {
      throw new BusinessException(new FieldError(this.getClass().getSimpleName(), ACCOUNT_CAR_PLATE_FIELD,
          CAR_PLATE_IS_INVALID.formatted(carPlateNumber)));
    }
  }

  private void validateIfCarPlateIsNullOrEmpty(String carPlateNumber) {
    if (carPlateNumber == null || carPlateNumber.trim().isEmpty()) {
      throw new BusinessException(new FieldError(this.getClass().getSimpleName(), ACCOUNT_CAR_PLATE_FIELD,
          CAR_PLATE_IS_INVALID.formatted(carPlateNumber)));
    }
  }
}
