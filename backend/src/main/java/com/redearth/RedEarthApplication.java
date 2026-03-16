package com.redearth;

import com.redearth.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class RedEarthApplication {
  public static void main(String[] args) {
    SpringApplication.run(RedEarthApplication.class, args);
  }
}
