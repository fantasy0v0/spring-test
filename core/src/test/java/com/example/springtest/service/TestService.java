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

  @Autowired
  private Test1Service test1Service;

  @Transactional
  public void test(boolean throwError) {
    try {
      test1Service.test(throwError);
    } catch (Exception e) {
      e.printStackTrace();
    }
    int result = jdbcTemplate.update(
      "update student set name = ? where id = ?", "TestAA", 1);
    Assertions.assertEquals(1, result);
  }

}
