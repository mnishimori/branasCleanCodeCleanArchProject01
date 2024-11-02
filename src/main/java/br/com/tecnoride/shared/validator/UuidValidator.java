package br.com.tecnoride.shared.validator;

import static br.com.tecnoride.shared.messages.SharedMessages.UUID_INVALID;

import br.com.tecnoride.account.infrastructure.exception.ValidatorException;
import br.com.tecnoride.shared.util.IsUUID;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

@Component
public class UuidValidator {

  public void validate(String uuid) {
    if (uuid == null || uuid.trim().isEmpty() || !IsUUID.isUUID().matches(uuid)) {
      throw new ValidatorException(new FieldError(this.getClass().getSimpleName(), "id", UUID_INVALID.formatted(uuid)));
    }
  }
}
