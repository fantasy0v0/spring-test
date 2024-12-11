package com.example.springtest.service;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Transactional
  public void test(boolean throwError) {
    String name = jdbcTemplate.queryForObject(
"select name from student where id = ?", String.class, 1);
    Assertions.assertEquals("UserA", name);
    int result = jdbcTemplate.update(
"update student set name = ? where id = ?", "TestA", 1);
    Assertions.assertEquals(1, result);
    if (throwError) {
      throw new RuntimeException("Test");
    }
  }

}
