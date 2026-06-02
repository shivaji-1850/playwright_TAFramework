package com.automation.framework.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central logger. Uses SLF4J + Logback.
 * Add logback.xml to src/main/resources for file/console appenders.
 */
public class FrameworkLogger {
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
