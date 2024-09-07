package br.com.tecnoride.signup.shared.postgres;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class PostgresContainer {

  @Container
  public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
      "postgres:latest")
      .withDatabaseName("tecnoride-db")
      .withUsername("tecnoride-postgres-user")
      .withPassword("tecnoride-postgres-pwd");

  static {
    postgreSQLContainer.start();
  }

  public static class Initializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertyValues values = TestPropertyValues.of(
          "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
          "spring.datasource.password=" + postgreSQLContainer.getPassword(),
          "spring.datasource.username=" + postgreSQLContainer.getUsername()
      );
      values.applyTo(applicationContext);
    }
  }
}
