package com.pack.anu;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.messaging.MessageHandler;

@EnableIntegration
@Configuration
public class IntegrationDsIntegrationFlow {

	@Value( "${file.input}" )
	private String INPUT_DIR;
	
	@Value( "${file.output}" )
	private String OUTPUT_DIR;
	
	@PostConstruct
	public void deleteAllPlayedFile() {
		File[] files = new File(INPUT_DIR).listFiles();
		for (File file : files) {
		    if (file.isFile()) {
		    	if(file.getName().matches("(.*)Played(.*)")) {
		    		file.delete();
		    	}
		    }
		}
	}
	    
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
                .endsWith(".txt"))
            .handle(targetDirectory())
            .get();
    }
}
