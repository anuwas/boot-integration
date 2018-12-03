package com.anu.bootflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.anu.bootflux")
public class BootfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootfluxApplication.class, args);
	}
}
