package com.wirecard.wms.report;

import com.wirecard.wms.report.data.Handler;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@EnableAsync
@SpringBootApplication
public class WmsReportApplication {

	private static final Logger logger = LogManager.getLogger(WmsReportApplication.class);

	public static void main(String[] args) throws Exception {
		Handler.install(); // Install Joop's protocol handler
		SpringApplication.run(WmsReportApplication.class, args);

		// TestCompile();
	}

	public static void TestCompile() throws Exception {
		//Compile report and fill, no datasource needed
		JasperReport report = JasperCompileManager.compileReport("htmlComponentBase64.jrxml");
		Map parameters = new HashMap<>();
		String paramPDF = new String(Files.readAllBytes(Paths.get("test.txt")));
		parameters.put("TEST_PDF", paramPDF);
		parameters.put("net.sf.jasperreports.awt.ignore.missing.font",  "true");
		parameters.put("net.sf.jasperreports.default.font.name", "Open Sans");
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters);

		//Export to pdf
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("pdf/htmlcomponentbase64.pdf"));
		// SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		// exporter.setConfiguration(configuration);
		exporter.exportReport();
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
