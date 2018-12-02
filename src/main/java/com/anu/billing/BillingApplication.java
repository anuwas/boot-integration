package com.anu.billing;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.anu.billing")
public class BillingApplication {

	public static void main(String[] args) {
		//SpringApplication.run(BillingApplication.class, args);
		ConfigurableApplicationContext context = new SpringApplicationBuilder(BillingApplication.class)

		        .properties("spring.config.name:application")
		        .build().run(args);
	}
}
