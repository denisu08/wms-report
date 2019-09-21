package com.wirecard.wms.report;

import com.wirecard.wms.report.data.Handler;
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

	public static void main(String[] args) throws Exception {
		Handler.install(); // Install Joop's protocol handler
		SpringApplication.run(WmsReportApplication.class, args);

		// TestCompile()
	}

	/*public static void TestCompile() {
		//Compile report and fill, no datasource needed
		JasperReport report = JasperCompileManager.compileReport("htmlComponentBase64.jrxml");
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, new HashMap<String, Object>());

		//Export to pdf
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("pdf/htmlcomponentbase64.pdf"));
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}*/

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
