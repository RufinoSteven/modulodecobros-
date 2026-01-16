package utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ExternalURLs {

    private static final String CONFIG_FILE = "userConfig.properties";
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ExternalURLs.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (in == null) {
                throw new IllegalStateException(CONFIG_FILE + " no encontrado en el classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Falló al cargar " + CONFIG_FILE, e);
        }
    }

    private ExternalURLs() {
        // clase de utilidad
    }

    public static String getRegionalAccountUrl() {
        return PROPS.getProperty("regional.account.url");
    }

}
