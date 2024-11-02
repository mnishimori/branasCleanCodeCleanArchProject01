package br.com.tecnoride.account.domain.valueobject;

import static br.com.tecnoride.account.domain.field.AccountDomainField.ACCOUNT_EMAIL_FIELD;
import static br.com.tecnoride.account.domain.message.AccountDomainMessage.EMAIL_IS_INVALID;

import br.com.tecnoride.account.domain.exception.BusinessException;
import java.util.regex.Pattern;
import org.springframework.validation.FieldError;

public record Email(String emailAddress) {

  public Email {
    validateIfEmailAddressIsNullOrEmpty(emailAddress);
    validateIfEmailPatternMatches(emailAddress);
  }

  private void validateIfEmailPatternMatches(String emailAddress) {
    var emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    var emailInvalid = Pattern.matches(emailRegex, emailAddress);
    if (!emailInvalid) {
      throw new BusinessException(
          new FieldError(this.getClass().getSimpleName(), ACCOUNT_EMAIL_FIELD,
              EMAIL_IS_INVALID.formatted(emailAddress)));
    }
  }

  private void validateIfEmailAddressIsNullOrEmpty(String emailAddress) {
    if (emailAddress == null || emailAddress.trim().isEmpty()) {
      throw new BusinessException(
          new FieldError(this.getClass().getSimpleName(), ACCOUNT_EMAIL_FIELD,
              EMAIL_IS_INVALID.formatted(emailAddress)));
    }
  }
}
