package com.dchealth.cro.common.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisTemplateConfiguration {
    @Bean
    public RedisTemplate<String, Object> template(
            RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> listTemplate = new RedisTemplate<>();
        listTemplate.setConnectionFactory(redisConnectionFactory);
        listTemplate.afterPropertiesSet();

        return listTemplate;
    }
}
