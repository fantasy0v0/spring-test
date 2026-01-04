package com.example.springtest.test;

import com.example.springtest.service.TestService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
// @Transactional
@SpringBootTest
public class SpringTest {

  @Autowired
  private TestService testService;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

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
    /*
     问题二
     如果Test1设置了REQUIRES_NEW，那么在抛出异常后会立即回滚，
     所以Test中查询不到修改后的数据。
    */
    testService.test(true);
    String name = jdbcTemplate.queryForObject(
      "select name from student where id = ?", String.class, 1);
    Assertions.assertEquals("UserA", name);
  }

}
