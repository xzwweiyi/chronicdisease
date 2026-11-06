package com.dchealth.redis.service.impl;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
     * Only created when Redis is reachable
     */
    @Bean
    public RedisMessageListenerContainer container2(RedisMessageListener listener) {
        // Quick check if Redis is reachable
        java.net.Socket socket = null;
        try {
            socket = new java.net.Socket();
            socket.connect(new java.net.InetSocketAddress(this.host, this.port), 2000);
        } catch (Exception e) {
            log.warn("Redis not available at {}:{}, message listener disabled: {}", this.host, this.port, e.getMessage());
            return null;
        } finally {
            try { if (socket != null) socket.close(); } catch (Exception ignored) {}
        }

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPort(this.port);
        jedisConnectionFactory.setPassword(this.password);
        jedisConnectionFactory.setHostName(this.host);
        jedisConnectionFactory.setDatabase(1);
        jedisConnectionFactory.setTimeout(2000);
        jedisConnectionFactory.afterPropertiesSet();

        final String TOPIC_NAME1 = "followTree";
        final String TOPIC_NAME2 = "patientCard";
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(listener, new PatternTopic(TOPIC_NAME1));
        container.addMessageListener(listener, new PatternTopic(TOPIC_NAME2));

        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        container.setTopicSerializer(seria);

        return container;
    }
}
