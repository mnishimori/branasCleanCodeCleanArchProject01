package br.com.tecnoride.shared.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UuidValidatorTest {

  @Spy
  private UuidValidator uuidValidator;

  @Test
  void shouldValidateUuid() {
    var uuid = UUID.randomUUID();
    assertDoesNotThrow(() -> uuidValidator.validate(uuid.toString()));
  }

  @Test
  void shouldThrowExceptionWhenUuidIsInvalid() {
    var invalidUuid = "abc";
    assertThrows(RuntimeException.class, () -> uuidValidator.validate(invalidUuid));
  }
}