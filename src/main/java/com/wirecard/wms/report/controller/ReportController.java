package com.wirecard.wms.report.controller;

import com.wirecard.wms.report.service.ReportGeneratorService;
import com.wirecard.wms.report.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

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

    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @RequestMapping("/")
    public String index() throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();
        // Kick of multiple, asynchronous lookups
        CompletableFuture<User> page1 = reportGeneratorService.findUser("PivotalSoftware");
        CompletableFuture<User> page2 = reportGeneratorService.findUser("CloudFoundry");
        CompletableFuture<User> page3 = reportGeneratorService.findUser("Spring-Projects");
        CompletableFuture<User> page4 = reportGeneratorService.findUser("RameshMF");

        // Wait until they are all done
        CompletableFuture.allOf(page1, page2, page3, page4).join();

        // Print results, including elapsed time
        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        logger.info("--> " + page1.get());
        logger.info("--> " + page2.get());
        logger.info("--> " + page3.get());
        logger.info("--> " + page4.get());

        return "Greetings from Spring Boot!";
    }

}