package com.automation.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Reads from: config.properties → System properties → Environment variables
 * Priority: EnvVar > SysProp > config.properties > default
 */
public class ConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
                log.info("Loaded config.properties");
            }
        } catch (Exception e) {
            log.warn("config.properties not found, using system/env properties only");
        }
    }

    public static String get(String key, String defaultValue) {
        // 1. Environment variable (CI/CD override)
        String envVal = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envVal != null) return envVal;
        // 2. System property (-Dbrowser=firefox)
        String sysProp = System.getProperty(key);
        if (sysProp != null) return sysProp;
        // 3. Properties file
        return props.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        try { return Integer.parseInt(get(key, String.valueOf(defaultValue))); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    public static boolean getBool(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }
}
