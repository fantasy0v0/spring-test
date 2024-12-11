package com.example.springtest.test;

import com.example.springtest.service.TestService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@Transactional
@SpringBootTest
public class SpringTest {

  @Autowired
  private TestService testService;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Test
  void test() {
    String name = jdbcTemplate.queryForObject(
      "select name from student where id = ?", String.class, 1);
    Assertions.assertEquals("UserA", name);
    try {
      testService.test(true);
      Assertions.fail();
    } catch (RuntimeException e) {
      Assertions.assertEquals("Test", e.getMessage());
    }
    name = jdbcTemplate.queryForObject(
  "select name from student where id = ?", String.class, 1);
    Assertions.assertEquals("UserA", name);

    try {
      testService.test(false);
    } catch (RuntimeException e) {
      Assertions.fail();
    }
    name = jdbcTemplate.queryForObject(
      "select name from student where id = ?", String.class, 1);
    Assertions.assertEquals("TestA", name);
  }

}
