package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_PATH = "src/test/resources/config.properties";
    private static final String HEALENIUM_CONFIG_PATH = "src/test/resources/healenium.properties";
    private static Properties config;

    private static Properties loadConfig() {
        if (config == null) {
            config = new Properties();
            
            try (FileInputStream file = new FileInputStream(CONFIG_PATH)) {
                config.load(file);
                logger.info("Configs loaded: {}", CONFIG_PATH);
            } catch (IOException e) {
                logger.error("Couldn't loaded: {}", e.getMessage());
            }
            
            try (FileInputStream fis = new FileInputStream(HEALENIUM_CONFIG_PATH)) {
                Properties healeniumConfig = new Properties();
                healeniumConfig.load(fis);
                config.putAll(healeniumConfig);
                logger.info("Healenium configuration loaded successfully: {}", HEALENIUM_CONFIG_PATH);
            } catch (IOException e) {
                logger.warn("Couldn't load healenium.properties: {}", e.getMessage());
            }
        }
        return config;
    }


    public static String getProperty(String key) {
        String value = loadConfig().getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            logger.error("Config property is empty: {}", key);
        }
        return value.trim();
    }

} 