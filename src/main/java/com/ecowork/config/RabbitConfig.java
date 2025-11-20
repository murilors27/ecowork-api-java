package com.ecowork.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE = "registro-consumo.queue";
    public static final String EXCHANGE = "consumo.direct";
    public static final String ROUTING_KEY = "consumo.novo";

    @Bean
    public Queue consumoQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public DirectExchange consumoExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding consumoBinding(Queue consumoQueue, DirectExchange consumoExchange) {
        return BindingBuilder
                .bind(consumoQueue)
                .to(consumoExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}