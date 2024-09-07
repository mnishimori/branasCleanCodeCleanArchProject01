package br.com.tecnoride.signup;

import br.com.tecnoride.signup.shared.annotation.DatabaseTest;
import br.com.tecnoride.signup.shared.annotation.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@DatabaseTest
class GetAccountControllerTest {

  private final MockMvc mockMvc;

  @Autowired
  GetAccountControllerTest(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void shouldReturnAccount() throws Exception {
    Assertions.fail("Not yet implemented");
  }
}
