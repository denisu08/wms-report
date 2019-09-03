package com.wirecard.wms.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;

@EnableAsync
@SpringBootApplication
public class WmsReportApplication {

	private static final Logger logger = LogManager.getLogger(WmsReportApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WmsReportApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		if(logger.isDebugEnabled()) {
			return args -> {
				logger.debug("Let's inspect the beans provided by Spring Boot:");
				String[] beanNames = ctx.getBeanDefinitionNames();
				Arrays.sort(beanNames);
				for (String beanName : beanNames) {
					logger.debug(beanName);
				}
			};
		}
		return null;
	}

}
