package com.pack.anu;

import java.io.File;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.messaging.MessageHandler;


@Configuration
@EnableIntegration
@IntegrationComponentScan
@SpringBootApplication
public class IntegrationDslApplication {
	
    public static final String INPUT_DIR = "/home/anupam/development/gitrepo/boot-integration/inputD";
    public static final String OUTPUT_DIR = "/home/anupam/development/gitrepo/boot-integration/outputD";
    
	@Bean
    public MessageSource<File> sourceDirectory() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(INPUT_DIR));
        return messageSource;
    }
	
	@Bean
    public MessageHandler targetDirectory() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUTPUT_DIR));
        handler.setExpectReply(false); // end of pipeline, reply not needed
        return handler;
    }
	
	@Bean
    public IntegrationFlow fileMoverWithLambda() {
        return IntegrationFlows.from(sourceDirectory(), configurer -> configurer.poller(Pollers.fixedDelay(1000)))
            .filter(message -> ((File) message).getName()
                .endsWith(".jpg"))
            .handle(targetDirectory())
            .get();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(IntegrationDslApplication.class, args);
        
	}

}
