package com.accountant.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class PropertiesMaker {
    private static PropertiesMaker instance;
    private static String source;
    private final Properties props;
    private final static Logger log = LoggerFactory.getLogger(PropertiesMaker.class);

    private PropertiesMaker(String resourceFile) {
        source = resourceFile;
        props = new Properties();
        try(InputStream inputStream = getResourcesStream(resourceFile)) {
            props.load(inputStream);
        } catch(FileNotFoundException fe) {
            log.error(String.format("Can't find %s!", resourceFile), fe);
        } catch(IOException ioe) {
            log.error("IO error!", ioe);
        }
    }

    private InputStream getResourcesStream(String resourceFile) throws IOException {
        if(System.getProperty("catalina.base") != null) {
            String path = System.getProperty("catalina.base")
                    .concat(File.separator)
                    .concat("conf")
                    .concat(File.separator)
                    .concat(resourceFile);
            File file = new File(path);
            if(file.exists() & file.isFile() & file.canRead()) {
                return new FileInputStream(path);
            }
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(resourceFile);
    }

    public static Properties getProps(String resourceFile) {
        return getInstance(resourceFile).props;
    }

    private static PropertiesMaker getInstance(String resourceFile) {
        if(instance == null) {
            instance = new PropertiesMaker(resourceFile);
        } else if(!source.equals(resourceFile)) {
            instance = new PropertiesMaker(resourceFile);
        }
        return instance;
    }
}