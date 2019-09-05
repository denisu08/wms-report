package com.wirecard.wms.report.config;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

public class MapResourceBundle extends ResourceBundle implements Serializable {

    private Map<String, Object> map;
    public MapResourceBundle(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    protected Object handleGetObject(String key) {
        return map.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(map.keySet());
    }

}