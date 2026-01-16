package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static final Logger log = LogManager.getLogger(ConfigLoader.class);

    static {
        try (InputStream input = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                log.error("No se pudo encontrar config.properties en el classpath");
                throw new RuntimeException("Archivo config.properties no encontrado");
            }
            properties.load(input);
            log.info("Archivo config.properties cargado exitosamente");
        } catch (IOException e) {
            log.error("Error al cargar config.properties", e);
            throw new RuntimeException("No fue posible cargar la configuración", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}