package com.anu.integrationDSL;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericSelector;
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

    public static final String INPUT_DIR = "/home/anupam/development/java/photon/works/integrationDSL/inputD";
    public static final String OUTPUT_DIR = "/home/anupam/development/java/photon/works/integrationDSL/outputD";
    public static final String OUTPUT_DIR2 = "/home/anupam/development/java/photon/works/integrationDSL/outputD2";

    @Bean
    public MessageSource<File> sourceDirectory() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(INPUT_DIR));
        return messageSource;
    }

    @Bean
    public GenericSelector<File> onlyJpgs() {
        return new GenericSelector<File>() {

            @Override
            public boolean accept(File source) {
              return source.getName()
                .endsWith(".jpg");
            }
        };
    }

    @Bean
    public MessageHandler targetDirectory() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUTPUT_DIR));
        handler.setExpectReply(false); // end of pipeline, reply not needed
        return handler;
    }
    
    @Bean
    public IntegrationFlow fileMover() {
        return IntegrationFlows.from(sourceDirectory(), configurer -> configurer.poller(Pollers.fixedDelay(10000)))
            .filter(onlyJpgs())
            .handle(targetDirectory())
            .get();
    }

    // @Bean
    public IntegrationFlow fileMoverWithLambda() {
        return IntegrationFlows.from(sourceDirectory(), configurer -> configurer.poller(Pollers.fixedDelay(10000)))
            .filter(message -> ((File) message).getName()
                .endsWith(".jpg"))
            .handle(targetDirectory())
            .get();
    }

    @Bean
    public PriorityChannel alphabetically() {
        return new PriorityChannel(1000, (left, right) -> ((File) left.getPayload()).getName()
            .compareTo(((File) right.getPayload()).getName()));
    }

    // @Bean
    public IntegrationFlow fileMoverWithPriorityChannel() {
        return IntegrationFlows.from(sourceDirectory())
            .filter(onlyJpgs())
            .channel("alphabetically")
            .handle(targetDirectory())
            .get();
    }

    @Bean
    public MessageHandler anotherTargetDirectory() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(OUTPUT_DIR2));
        handler.setExpectReply(false); // end of pipeline, reply not needed
        return handler;
    }

    // @Bean
    public IntegrationFlow fileReader() {
        return IntegrationFlows.from(sourceDirectory())
            .filter(onlyJpgs())
            .channel("holdingTank")
            .get();
    }

    // @Bean
    public IntegrationFlow fileWriter() {
        return IntegrationFlows.from("holdingTank")
            .bridge(e -> e.poller(Pollers.fixedRate(1, TimeUnit.SECONDS, 20)))
            .handle(targetDirectory())
            .get();
    }

    // @Bean
    public IntegrationFlow anotherFileWriter() {
        return IntegrationFlows.from("holdingTank")
            .bridge(e -> e.poller(Pollers.fixedRate(2, TimeUnit.SECONDS, 10)))
            .handle(anotherTargetDirectory())
            .get();
    }
    
	public static void main(final String... args) {
		SpringApplication.run(IntegrationDslApplication.class, args);
		final AbstractApplicationContext context = new AnnotationConfigApplicationContext(IntegrationDslApplication.class);
        context.registerShutdownHook();
        final Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter a string and press <enter>: ");
        while (true) {
            final String input = scanner.nextLine();
            if ("q".equals(input.trim())) {
                context.close();
                scanner.close();
                break;
            }
        }
        System.exit(0);
	}

}

