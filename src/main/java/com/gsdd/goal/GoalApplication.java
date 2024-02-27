package com.gsdd.goal;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@OpenAPIDefinition(
    info = @Info(title = "Goal API", version = "2.0", description = "REST with Spring-Boot & H2",
        contact = @Contact(email = "alex.galvis.sistemas@gmail.com")))
public class GoalApplication {

  public static final String BASE_PACKAGE = "com.gsdd.goal.";

  public static void main(String[] args) {
    SpringApplication.run(GoalApplication.class, args);
  }
}
