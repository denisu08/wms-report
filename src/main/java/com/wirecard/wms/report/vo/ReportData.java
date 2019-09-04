package com.wirecard.wms.report.vo;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReportData {
    private Map<String, Object> parameterReport = new HashMap<>();
    private JSONObject jsonData = new JSONObject();
    private JSONObject parameterJSON = new JSONObject();
    private boolean isImage = false;
    private byte[] reportBytes = null;

    public Map<String, Object> getParameterReport() {
        return parameterReport;
    }

    public void setParameterReport(Map<String, Object> parameterReport) {
        this.parameterReport = parameterReport;
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    public JSONObject getParameterJSON() {
        return parameterJSON;
    }

    public void setParameterJSON(JSONObject parameterJSON) {
        this.parameterJSON = parameterJSON;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public byte[] getReportBytes() {
        return reportBytes;
    }

    public void setReportBytes(byte[] reportBytes) {
        this.reportBytes = reportBytes;
    }

    @Override
    public String toString() {
        return "ReportData{" +
                "parameterReport=" + parameterReport +
                ", jsonData=" + jsonData +
                ", parameterJSON=" + parameterJSON +
                ", isImage=" + isImage +
                ", reportBytes=" + Arrays.toString(reportBytes) +
                '}';
    }
}
