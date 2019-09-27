package com.wirecard.wms.report.service;


import com.google.gson.Gson;
import com.wirecard.wms.report.config.MapResourceBundle;
import com.wirecard.wms.report.vo.ReportData;
import com.wirecard.wms.report.vo.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ReportGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(ReportGeneratorService.class);
    private final RestTemplate restTemplate;

    @Autowired
    JRFileVirtualizer fv;

    @Autowired
    JRSwapFileVirtualizer sfv;

    public ReportGeneratorService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async("reportExecutor")
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        logger.info("Looking up " + user);
        String url = String.format("https://api.github.com/users/%s", user);
        User results = restTemplate.getForObject(url, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(results);
    }

    @Async("reportExecutor")
    public CompletableFuture<Map> downloadReport(ReportData reportData) throws Exception {
        JasperDesign design = null;
        JasperPrint jasperPrint = null;
        boolean isJasperMain = false;
        String jasperReportBase64 = null;
        JasperReport mainJasperReport = null;

        String languageGsonValue = reportData.getLanguageGson();
        Map parameterJSONValue = reportData.getParameterJSONValue();
        Map parameterReportValue = new HashMap();
        parameterReportValue.put(JRParameter.REPORT_VIRTUALIZER, fv);
        parameterReportValue.put("net.sf.jasperreports.awt.ignore.missing.font", "true");
        parameterReportValue.put("net.sf.jasperreports.default.font.name", "Open Sans");
        if(StringUtils.hasText(languageGsonValue)) {
            Map languageGson = new Gson().fromJson(languageGsonValue, Map.class);
            parameterReportValue.put("REPORT_RESOURCE_BUNDLE", new MapResourceBundle(languageGson));
        }

        if (reportData.isImageValue()) {
            design = JRXmlLoader.load(new ByteArrayInputStream(reportData.getReportBytesValue()));
        } else {
            List<Map> reportFiles = (List<Map>) parameterJSONValue.get("reportFiles");
            for (Map item : reportFiles) {
                if (item.get("jasper") != null) {
                    byte[] jasperTmpBytes = Base64.getDecoder().decode((String) item.get("jasper"));
                    if ((Boolean) item.getOrDefault("isMain", false)) {
                        isJasperMain = true;
                        jasperReportBase64 = (String) item.get("jasper");
                        mainJasperReport = (JasperReport) JRLoader.loadObject(new ByteArrayInputStream(jasperTmpBytes));
                    } else {
                        parameterReportValue.put((String) item.get("keySubReport"), (JasperReport) JRLoader.loadObject(new ByteArrayInputStream(jasperTmpBytes)));
                    }
                } else {
                    byte[] reportTmpBytes = Base64.getDecoder().decode((String) item.get("reportData"));
                    if ((Boolean) item.getOrDefault("isMain", false)) {
                        design = JRXmlLoader.load(new ByteArrayInputStream(reportTmpBytes));
                    } else {
                        parameterReportValue.put((String) item.get("keySubReport"), JasperCompileManager.compileReport(JRXmlLoader.load(new ByteArrayInputStream(reportTmpBytes))));
                    }
                }
            }
        }

        JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(reportData.getJsonData().toString().getBytes()));
        if (isJasperMain) {
            jasperPrint = JasperFillManager.fillReport(mainJasperReport, parameterReportValue, jsonDataSource);
        } else {
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            JasperCompileManager.compileReportToStream(design, bais);
            jasperReportBase64 = Base64.getEncoder().encodeToString(bais.toByteArray());
            jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(bais.toByteArray()), parameterReportValue, jsonDataSource);
        }

        JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (reportData.isImageValue()) {
            BufferedImage rendered_image = (BufferedImage) printManager.printPageToImage(jasperPrint, 0, 1.6f);
            ImageIO.write(rendered_image, "png", outputStream);
        } else {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();
        }

        Map results = new HashMap();
        results.put("report", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        results.put("jasper", jasperReportBase64);

        return CompletableFuture.completedFuture(results);
    }
}