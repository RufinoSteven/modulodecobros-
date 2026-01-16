package dbConnections;

import com.banreservas.commons.properties.PropManager;
import lombok.Getter;

@Getter
public class DBConfig {

    private static final String CONFIG_FILE = "dbsettings.properties";
    private final String server;
    private final int port;
    private final String username;
    private final String password;

    private static final DBConfig INSTANCE = new DBConfig();

    public static DBConfig getInstance() {
        return INSTANCE;
    }

    /**
     * Construye la instancia de DBConfig utilizando valores de dbsettings.properties.
     * <p>
     * Para cada campo requerido (host, usuario, contraseña, puerto), lee el valor
     * del archivo de propiedades y, si es necesario, lo desencripta usando {@link #getOrDecrypt(PropManager, String)}.
     * Los valores finales se almacenan en los campos inmutables de esta configuración.
     */
    private DBConfig() {
        PropManager pm = new PropManager(CONFIG_FILE);
        this.server   = getOrDecrypt(pm, "signature.host");
        this.username = getOrDecrypt(pm, "signature.user");
        this.password = getOrDecrypt(pm, "signature.password");
        String portStr = getOrDecrypt(pm, "signature.port");
        this.port = Integer.parseInt(portStr);

    }

    /**
     * Devuelve el valor de una propiedad en texto plano.
     * <p>
     * Primero intenta desencriptar el valor de la propiedad (para entradas encriptadas).
     * Si la desencriptación no es posible o devuelve un valor en blanco, recurre al
     * valor en crudo almacenado en el archivo de propiedades.
     *
     * @param pm  manejador de propiedades utilizado para acceder al archivo
     * @param key clave de la propiedad a leer (por ejemplo, "signature.user")
     * @return el valor desencriptado si está disponible, de lo contrario, el valor original
     */
    private String getOrDecrypt(PropManager pm, String key) {
        String decrypted = pm.decryptProperty(key);
        if (decrypted != null && !decrypted.isBlank()) {
            return decrypted;
        }
        return pm.getProperty(key);
    }
}
