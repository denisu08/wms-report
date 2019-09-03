package com.wirecard.wms.report.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class ReportController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}