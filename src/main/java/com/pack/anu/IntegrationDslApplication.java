package com.pack.anu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.integration.annotation.IntegrationComponentScan;




@IntegrationComponentScan
@SpringBootApplication
public class IntegrationDslApplication extends SpringBootServletInitializer{    
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(IntegrationDslApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(IntegrationDslApplication.class, args);
        
	}

}
