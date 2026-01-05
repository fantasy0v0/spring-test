package com.example.springtest.test;

import com.example.springtest.BaseTest;
import com.example.springtest.service.TestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

// @Transactional
@SpringBootTest
public class SpringTest extends BaseTest {

  @Autowired
  private TestService testService;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void test() {
    /*
     问题一
     问：为什么Test1抛出了异常，仍然能在Test中查到修改后的数据？
     答：如果Test1没有设置REQUIRES_NEW，那么它将会和Test共用一个事务，
     虽然Test1抛出了异常，并且在Test中捕获了异常，但是Spring并没有立即回滚，
     所以Test中可以查到修改后的数据。
     但此时事务已经被标记为仅回滚，所以当Test方法结束后Spring会抛出异常并回滚事务。
    */
    try {
      testService.test(true);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
    String name = jdbcTemplate.queryForObject(
      "select name from student where id = ?", String.class, 1);
    Assertions.assertEquals("UserA", name);
    /*
     问题二
     如果Test1设置了REQUIRES_NEW，那么在抛出异常后会立即回滚，
     所以Test中查询不到修改后的数据。
    */
    try {
      testService.testNew(true);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
    name = jdbcTemplate.queryForObject(
      "select name from student where id = ?", String.class, 1);
    Assertions.assertEquals("TestAA", name);
  }

}
