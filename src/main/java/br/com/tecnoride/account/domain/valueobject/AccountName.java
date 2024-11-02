package br.com.tecnoride.account.domain.valueobject;

import static br.com.tecnoride.account.domain.field.AccountDomainField.ACCOUNT_NAME_FIELD;
import static br.com.tecnoride.account.domain.message.AccountDomainMessage.NAME_IS_INVALID_MESSAGE;

import br.com.tecnoride.account.domain.exception.BusinessException;
import java.util.regex.Pattern;
import org.springframework.validation.FieldError;

public record AccountName(String name) {

  public AccountName {
    validateIfNameIsNullOrEmpty(name);
    validateIfNameHasInvalidPattern(name);
  }

  private void validateIfNameHasInvalidPattern(String name) {
    var userNameInvalid = Pattern.matches("[A-Z][a-z]+ [A-Z][a-z]+", name);
    if (!userNameInvalid) {
      throw new BusinessException(
          new FieldError(this.getClass().getSimpleName(), ACCOUNT_NAME_FIELD, NAME_IS_INVALID_MESSAGE.formatted(name)));
    }
  }

  private void validateIfNameIsNullOrEmpty(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new BusinessException(
          new FieldError(this.getClass().getSimpleName(), ACCOUNT_NAME_FIELD, NAME_IS_INVALID_MESSAGE.formatted(name)));
    }
  }
}
