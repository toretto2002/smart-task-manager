package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_PATH = "config/config.properties";
    private static Properties props = new Properties();

    static {
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {
            props.load(input);
        } catch (IOException e) {
            System.err.println("Errore nel caricamento di config.properties: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
