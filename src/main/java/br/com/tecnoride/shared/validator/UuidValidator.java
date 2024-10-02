package br.com.tecnoride.shared.validator;

import static br.com.tecnoride.shared.messages.SharedMessages.UUID_INVALID;

import br.com.tecnoride.shared.util.IsUUID;
import org.springframework.stereotype.Component;

@Component
public class UuidValidator {

  public void validate(String uuid) {
    if (uuid == null || uuid.trim().isEmpty() || !IsUUID.isUUID().matches(uuid)) {
      throw new RuntimeException(UUID_INVALID.formatted(uuid));
    }
  }
}
