package com.upc.pre.peaceapp.messagebroker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    // Exchanges y colas reales que usas
    public static final String EXCHANGE_REPORT = "report.exchange";
    public static final String QUEUE_REPORT_CREATED = "report.created.queue";
    public static final String QUEUE_REPORT_DELETED = "report.deleted.queue";
    public static final String ROUTING_KEY_REPORT_CREATED = "report.created";
    public static final String ROUTING_KEY_REPORT_DELETED = "report.deleted";

    @Bean
    public TopicExchange reportExchange() {
        return new TopicExchange(EXCHANGE_REPORT);
    }

    @Bean
    public Queue reportCreatedQueue() {
        return new Queue(QUEUE_REPORT_CREATED, true);
    }

    @Bean
    public Queue reportDeletedQueue() {
        return new Queue(QUEUE_REPORT_DELETED, true);
    }

    @Bean
    public Binding bindingReportCreated(Queue reportCreatedQueue, TopicExchange reportExchange) {
        return BindingBuilder.bind(reportCreatedQueue)
                .to(reportExchange)
                .with(ROUTING_KEY_REPORT_CREATED);
    }

    @Bean
    public Binding bindingReportDeleted(Queue reportDeletedQueue, TopicExchange reportExchange) {
        return BindingBuilder.bind(reportDeletedQueue)
                .to(reportExchange)
                .with(ROUTING_KEY_REPORT_DELETED);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(returned ->
                log.error("❗Message NOT routed: Exchange={} RoutingKey={} Body={}",
                        returned.getExchange(),
                        returned.getRoutingKey(),
                        new String(returned.getMessage().getBody()))
        );
        return rabbitTemplate;
    }
}
