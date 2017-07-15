package com.vc.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages={"com.vc.model"})
@EnableJpaRepositories(basePackages={"com.vc.service", "com.vc.repository", "com.vc.service.controller", "com.vc.model"})
@ComponentScan(basePackages={"com.vc.service", "com.vc.repository", "com.vc.service.controller", "com.vc.model"})
public class VacationPlannerApp
{
  public static void main(String[] args)
  {
    SpringApplication.run(VacationPlannerApp.class, args);
  }
}
