package com.dchealth.cro.common.config;

import com.dchealth.cro.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 */
@Configuration
@EnableAsync
@Slf4j
@ConfigurationProperties("spring.executor")
public class ExecutorConfig {

    /**
     * 核心线程数
     */
    private Integer corPollSize;

    /**
     * 最大线程数
     */
    private Integer maxPoolSize;

    /**
     * 队列大小
     */
    private Integer queueCapacity;

    /**
     * 线程名前缀
     */
    private String threadNamePrefix;

    public Integer getCorPollSize() {
        return corPollSize;
    }

    public void setCorPollSize(Integer corPollSize) {
        this.corPollSize = corPollSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    @Bean
    public Executor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        corPollSize = corPollSize == null ? Runtime.getRuntime().availableProcessors() : corPollSize;
        executor.setCorePoolSize(corPollSize);
        executor.setMaxPoolSize(maxPoolSize == null ? corPollSize * 2 : maxPoolSize);
        executor.setQueueCapacity(queueCapacity == null ? Integer.MAX_VALUE : queueCapacity);
        executor.setThreadNamePrefix(StringUtils.isNotBlankAndNotEmpty(threadNamePrefix) ? threadNamePrefix : "task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();//初始化
        return executor;
    }

}
