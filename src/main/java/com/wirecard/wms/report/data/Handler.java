package com.wirecard.wms.report.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new DataConnection(u);
    }

    public static void install() {
        logger.info("install");
        String pkgName = Handler.class.getPackage().getName();
        String pkg = pkgName.substring(0, pkgName.lastIndexOf('.'));

        String protocolHandlers = System.getProperty("java.protocol.handler.pkgs", "");
        if (!protocolHandlers.contains(pkg)) {
            logger.info("!protocolHandlers.contains: " + pkg);
            if (!protocolHandlers.isEmpty()) {
                protocolHandlers += "|";
            }
            protocolHandlers += pkg;
            System.setProperty("java.protocol.handler.pkgs", protocolHandlers);
        }
    }
}