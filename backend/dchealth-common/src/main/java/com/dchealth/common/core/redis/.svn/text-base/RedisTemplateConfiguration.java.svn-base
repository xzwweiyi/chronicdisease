package com.dchealth.common.core.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfiguration {


    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedisTemplate<String, Object> redisTemplateSelect1() {

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPort(this.port);
        jedisConnectionFactory.setPassword(this.password);
        jedisConnectionFactory.setHostName(this.host);
        jedisConnectionFactory.setDatabase(1);
        jedisConnectionFactory.afterPropertiesSet();

        RedisTemplate<String, Object> listTemplate = new RedisTemplate<>();
        listTemplate.setKeySerializer(new StringRedisSerializer());
        listTemplate.setValueSerializer(new StringRedisSerializer());
        listTemplate.setHashKeySerializer(new StringRedisSerializer());
//        listTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        listTemplate.setConnectionFactory(jedisConnectionFactory);
        listTemplate.afterPropertiesSet();

        return listTemplate;
    }
}
