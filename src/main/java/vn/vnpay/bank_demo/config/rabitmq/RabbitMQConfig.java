package vn.vnpay.bank_demo.config.rabitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.support.GenericWebApplicationContext;

@Configuration
@EnableRabbit
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
@RequiredArgsConstructor
public class RabbitMQConfig {
    private GenericWebApplicationContext context;

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUserName;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value("${spring.rabbitmq.virtual-host}")
    private String rabbitVirtualHost;

    public static final String QUEUE_NAME = "myQueue";

    @Bean("cachingConnectionFactory")
    public ConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(this.rabbitHost, this.rabbitPort);
        cachingConnectionFactory.setUsername(this.rabbitUserName);
        cachingConnectionFactory.setPassword(this.rabbitPassword);
        cachingConnectionFactory.setVirtualHost(this.rabbitVirtualHost);
        cachingConnectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        return cachingConnectionFactory;
    }
//
//    @Primary
//    @Bean
//    public AmqpAdmin amqpAdmin(ConnectionFactory cachingConnectionFactory) {
//        return new RabbitAdmin(cachingConnectionFactory);
//    }
//
    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cachingConnectionFactory) {
        return new RabbitTemplate(cachingConnectionFactory);
    }

//    @Primary
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = this.getCachingConnectionFactory();
//        return connectionFactory;
//    }

    @Bean
    public Queue myQueue() {
        return new Queue(QUEUE_NAME, false);
    }

//    @Bean
//    public Jackson2JsonMessageConverter simpleMessageConverter() {
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//        converter.setClassMapper(trustedClassMapper());
//        return converter;
//    }
//
//    @Bean
//    public DefaultClassMapper trustedClassMapper() {
//        DefaultClassMapper classMapper = new DefaultClassMapper();
//        classMapper.setTrustedPackages("*");
//        return classMapper;
//    }
}
