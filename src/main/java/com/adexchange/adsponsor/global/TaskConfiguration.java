package com.adexchange.adsponsor.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class TaskConfiguration {
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        taskExecutor.setCorePoolSize(16);
        // 设置最大线程数
        taskExecutor.setMaxPoolSize(500);
        // 设置队列容量
        taskExecutor.setQueueCapacity(50);
        // 设置线程活跃时间（秒）
        taskExecutor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        taskExecutor.setThreadNamePrefix("asyncTask");
        // 设置拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //加载
        taskExecutor.initialize();
        return taskExecutor;
    }
}
