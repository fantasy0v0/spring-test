package com.example.springtest;

import org.springframework.boot.SpringApplication;

public class TestSpringTestApplication {

  static void main(String[] args) {
    SpringApplication
      .from(SpringTestApplication::main)
      .with(TestcontainersConfiguration.class)
      .run(args);
  }

}
