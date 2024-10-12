package br.com.tecnoride.shared.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class IsUUIDTest {

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"a", "1", "a1"})
  void shouldThrowExceptionWhenIdIsInvalidUuid(String id) {
    boolean matches = IsUUID.isUUID().matches(id);

    assertThat(matches).isFalse();
  }

  @Test
  void shouldValidateStringIdWhenStringIdIsValidUuid() {
    var id = UUID.randomUUID();
    boolean matches = IsUUID.isUUID().matches(id.toString());

    assertThat(matches).isTrue();
  }
}
