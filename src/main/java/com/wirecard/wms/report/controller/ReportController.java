package com.wirecard.wms.report.controller;

import com.wirecard.wms.report.service.ReportGeneratorService;
import com.wirecard.wms.report.vo.ReportData;
import com.wirecard.wms.report.vo.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @RequestMapping("/")
    public String index() throws Exception {
        return "Unknown Service";
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

    @RequestMapping(value = "/downloadReport", method = RequestMethod.POST)
    public Map downloadReport(@RequestBody ReportData reportData) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();
        CompletableFuture<Map> resultReport = reportGeneratorService.downloadReport(reportData);
        Map result = resultReport.get();
        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        return result;
    }
}