package com.wirecard.wms.report.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ReportExecutor {
    @Value("${report.thread.core-pool}")
    private int corePoolSize;

    @Value("${report.thread.max-pool}")
    private int maxPoolSize;

    @Value("${report.queue.capacity}")
    private int queueCapacity;

    @Value("${report.thread.timeout}")
    private int threadTimeout;

    @Bean
    @Qualifier("reportExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setKeepAliveSeconds(threadTimeout);

        return threadPoolTaskExecutor;
    }
}
