package com.example.springtest.service;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class TestService {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private Test1Service test1Service;

  @Transactional
  public void test(boolean throwError) throws SQLException {
    Connection connection = DataSourceUtils.doGetConnection(dataSource);
    Assertions.assertFalse(connection.getAutoCommit());
    try {
      test1Service.test(throwError);
    } catch (Exception e) {
      e.printStackTrace();
    }
    int result = jdbcTemplate.update(
      "update student set name = ? where id = ?", "TestAA", 1);
    Assertions.assertEquals(1, result);
  }

  @Transactional
  public void testNew(boolean throwError) {
    try {
      test1Service.testNew(throwError);
    } catch (Exception e) {
      e.printStackTrace();
    }
    int result = jdbcTemplate.update(
      "update student set name = ? where id = ?", "TestAA", 1);
    Assertions.assertEquals(1, result);
  }

}
