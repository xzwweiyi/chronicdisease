package com.dchealth.redis.service.impl;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Configuration
public class RedisMessageContainerConfig  {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * Redis消息监听器容器
     * 这个容器加载了RedisConnectionFactory和消息监听器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     *
     * @param redisConnectionFactory 连接工厂
     * @return redis消息监听容器
     */
    @Bean
    public RedisMessageListenerContainer container2(RedisMessageListener listener) {

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPort(this.port);
        jedisConnectionFactory.setPassword(this.password);
        jedisConnectionFactory.setHostName(this.host);
        jedisConnectionFactory.setDatabase(1);
        jedisConnectionFactory.afterPropertiesSet();

        final String TOPIC_NAME1 = "followTree"; // 订阅主题
        final String TOPIC_NAME2 = "patientCard"; // 订阅主题
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 监听所有库的key过期事件
        container.setConnectionFactory(jedisConnectionFactory);
        // 所有的订阅消息，都需要在这里进行注册绑定,new PatternTopic(TOPIC_NAME1)表示发布的主题信息
        // 可以添加多个 messageListener，配置不同的通道
        container.addMessageListener(listener, new PatternTopic(TOPIC_NAME1));
        container.addMessageListener(listener, new PatternTopic(TOPIC_NAME2));
//        container.addMessageListener(adapter, new PatternTopic(TOPIC_NAME2));

        /**
         * 设置序列化对象
         * 特别注意：1. 发布的时候需要设置序列化；订阅方也需要设置序列化
         *         2. 设置序列化对象必须放在[加入消息监听器]这一步后面，否则会导致接收器接收不到消息
         */
        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        container.setTopicSerializer(seria);

        return container;
    }
}

