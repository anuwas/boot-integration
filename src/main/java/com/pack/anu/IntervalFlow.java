package com.pack.anu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@EnableIntegration
@Configuration
public class IntervalFlow {
	
	
	@Bean
    public IntegrationFlow jmsInboundFlow() {
        return IntegrationFlows
        		.from(Jms.inboundAdapter(cachingConnectionFactory())
        		//.from(Jms.inboundAdapter(this.cachingConnectionFactory)
        		
                        .destination("jmsInbound"),
                        e -> e.poller(p -> p.fixedDelay(10000)))
                .<String, String>transform(String::toUpperCase)
                //.channel(jmsOutboundInboundReplyChannel())
                .channel("jmsOutboundInboundReplyChannel")
                .get();
    }

}
