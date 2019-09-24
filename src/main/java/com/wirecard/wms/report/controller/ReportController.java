package com.wirecard.wms.report.controller;

import com.wirecard.wms.report.WmsReportApplication;
import com.wirecard.wms.report.service.ReportGeneratorService;
import com.wirecard.wms.report.vo.ReportData;
import com.wirecard.wms.report.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @RequestMapping("/")
    public String index() throws Exception {
        forcefullyInstall(new com.wirecard.wms.report.data.URLStreamHandlerFactory());
        // WmsReportApplication.TestCompile();
        return "Unknown Service";
    }

    public static void forcefullyInstall(URLStreamHandlerFactory factory) {
        try {
            // Try doing it the normal way
            URL.setURLStreamHandlerFactory(factory);
        } catch (final Error e) {
            // Force it via reflection
            try {
                final Field factoryField = URL.class.getDeclaredField("factory");
                factoryField.setAccessible(true);
                factoryField.set(null, factory);
            } catch (NoSuchFieldException | IllegalAccessException e1) {
                throw new Error("Could not access factory field on URL class: {}", e);
            }
        }
    }

    @RequestMapping("/findUsers")
    public String findUsers() throws Exception {
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

        return "FindUsers";
    }

    @RequestMapping(value = "/downloadReport", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map downloadReport(@RequestBody ReportData reportData) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();
        CompletableFuture<Map> resultReport = reportGeneratorService.downloadReport(reportData);
        Map result = resultReport.get();
        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        return result;
    }
}