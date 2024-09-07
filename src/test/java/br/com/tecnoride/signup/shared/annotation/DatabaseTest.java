package br.com.tecnoride.signup.shared.annotation;


import br.com.tecnoride.signup.shared.postgres.PostgresContainer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntegrationTest
@Testcontainers
@Transactional
@ContextConfiguration(initializers = {PostgresContainer.Initializer.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseTest {

}
