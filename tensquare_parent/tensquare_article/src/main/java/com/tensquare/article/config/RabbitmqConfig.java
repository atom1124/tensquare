package com.tensquare.article.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 */
@Configuration
public class RabbitmqConfig {

    //交换机 让交换机 跟  用户队列 、 routingkey(作者id) 绑定
    public static  final String EX_ARTICLE = "article_subscribe";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public RabbitAdmin rabbitAdmin(){
        return new RabbitAdmin(rabbitTemplate.getConnectionFactory());
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EX_ARTICLE);
    }
}
