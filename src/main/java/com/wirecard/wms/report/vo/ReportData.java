package com.wirecard.wms.report.vo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ReportData implements Serializable {

    private String languageGson;
    private String jsonData;
    private String parameterJSON;
    private String isImage = "N";
    private String reportBytes;

    public String getLanguageGson() {
        return languageGson;
    }

    public void setLanguageGson(String languageGson) {
        this.languageGson = languageGson;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getParameterJSON() {
        return parameterJSON;
    }

    public void setParameterJSON(String parameterJSON) {
        this.parameterJSON = parameterJSON;
    }

    public String getIsImage() {
        return isImage;
    }

    public void setIsImage(String isImage) {
        this.isImage = isImage;
    }

    public String getReportBytes() {
        return reportBytes;
    }

    public void setReportBytes(String reportBytes) {
        this.reportBytes = reportBytes;
    }

    public Map<String, Object> getLanguageGsonValue() {
        Map<String, Object> result = new Gson().fromJson(
                this.languageGson, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        return result;
    }

    public Map<String, Object> getJsonDataValue() {
        Map<String, Object> result = new Gson().fromJson(
                this.jsonData, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        return result;
    }

    public Map<String, Object> getParameterJSONValue() {
        Map<String, Object> result = new Gson().fromJson(
                this.parameterJSON, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        return result;
    }

    public boolean isImageValue() {
        return "Y".equalsIgnoreCase(this.isImage);
    }

    public byte[] getReportBytesValue() {
        return Base64.getDecoder().decode(this.reportBytes);
    }

    @Override
    public String toString() {
        return "ReportData{" +
                "languageGson=" + languageGson +
                ", jsonData=" + jsonData +
                ", parameterJSON=" + parameterJSON +
                ", isImage=" + isImage +
                ", reportBytes=" + String.valueOf(reportBytes) +
                '}';
    }
}
