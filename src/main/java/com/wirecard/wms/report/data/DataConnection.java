package com.wirecard.wms.report.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.bind.DatatypeConverter;

public class DataConnection extends URLConnection implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(DataConnection.class);

    public DataConnection(URL u) {
        super(u);
    }

    @Override
    public void connect() throws IOException {
        connected = true;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        String data = url.toString();
        data = data.replaceFirst("^.*;base64,", "");
        logger.info("DataConnection is parsed");
        byte[] bytes = DatatypeConverter.parseBase64Binary(data);
        return new ByteArrayInputStream(bytes);
    }

}