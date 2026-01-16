package config;

import actions.Authentication.LoginActions;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import fixtures.Browsers;
import fixtures.Environment;
import io.cucumber.java.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import utils.ConfigLoader;
import utils.RobustWebDriverWait;
import utils.WindowManager;

import java.time.Duration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import static fixtures.Browsers.*;
import static fixtures.Environment.DEV;
import static fixtures.Environment.QA1;
import static fixtures.Environment.UAT1;

@NoArgsConstructor
public class Browser {
    /**
     * En esta clase se encuentran las herramientas de arquitectura para todo el proyecto. Aquí puedes encontrar el servidor a utilizar en el proyecto, el directorio para las descargas que ocurren en las pruebas ejecutadas.
     * La arquitectura utilizada para abrir instancias del navegador y otras funciones se basa en "Singleton" para un mejor rendimiento.
     * Al ejecutar las pruebas, solo hay un navegador disponible.
     */

    private static WebDriver webDriver;
    public static RobustWebDriverWait wait;
    private static final Logger log = LogManager.getLogger(Browser.class);
    @Getter
    private static Environment environment = QA1;
    @Getter
    private static Browsers browsers = CHROME;

    public static WebDriver getWebDriver() {
        System.setProperty("webdriver.chrome.logfile", "../logs/");
        if (webDriver == null) {
            setBrowsers(browsers);
            if (browsers == CHROME) {
                webDriver = new ChromeDriver(getChromeOptions());
                webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            } else if (browsers == FIREFOX) {
                webDriver = new FirefoxDriver(getFirefoxOptions());
                webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            } else if (browsers == EDGE) {
                webDriver = new EdgeDriver(getEdgeOptions());
                webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            } else if (browsers == SAFARI) {
                webDriver = new SafariDriver();
                webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            }
        }
        return webDriver;
    }

    private static ChromeOptions getChromeOptions() {
        Map<String, Object> preferences = new Hashtable<>();
        preferences.put("profile.default_content_settings.popups", 0);
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        // Ocultar y trabajar sin interfaz gráfica (GUI)
        if (System.getProperty("runPipeline", "false").equals("true")) {
            log.info("¡Ejecutando Chrome en modo headless!");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-default-apps");
        }
        options.addArguments("--log-level=3");
        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--incognito");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-infobars");
        options.setExperimentalOption("prefs", preferences);
        return options;
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--ignore-certificate-errors", "--ignore-ssl-errors");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setAcceptInsecureCerts(true);
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        if (System.getProperty("runPipeline", "false").equals("true")) {
            log.info("¡Ejecutando Firefox en modo headless!");
            options.addArguments("--headless");
            options.addArguments("--profile-root");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-setuid-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
        }
        return options;
    }

    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        if (System.getProperty("runPipeline", "false").equals("true")) {
            options.setCapability("headless", "headless");
            options.setCapability("disable-gpu", "disable-gpu");
        }
        options.setCapability("acceptInsecureCerts", true);
        options.setCapability("pageLoadStrategy", "normal");
        options.setCapability("ignoreZoomSetting", true);
        return options;
    }

    public static void setEnvironment(Environment environment) {
        Browser.environment = environment;
    }

    public static void setBrowsers(Browsers browser) {
        browsers = browser;
        switch (browser) {
            case CHROME -> WebDriverManager.chromedriver().setup();
            case FIREFOX -> WebDriverManager.firefoxdriver().setup();
            case EDGE -> WebDriverManager.edgedriver().setup();
            case SAFARI -> WebDriverManager.safaridriver().setup();
        }
    }

    public static String getSignatureServer() {
        String key = switch (environment) {
            case QA1 -> "signature.server.url.qa1";
            case UAT1 -> "signature.server.url.uat1";
            case DEV -> "signature.server.url.dev";
            default -> throw new IllegalStateException(
                    "No se ha configurado una clave de URL para el entorno: " + environment
            );
        };

        String url = ConfigLoader.getProperty(key);
        if (url == null || url.isBlank()) {
            throw new IllegalStateException(
                    "No se encontró la URL del servidor de firma para el entorno: " + environment +
                            " (clave esperada: " + key + ")"
            );
        }

        return url;
    }

    /**
     * Configura el entorno y navegador para las pruebas basándose en propiedades del sistema.
     *
     * <p><b>Propiedades requeridas:</b></p>
     * <ul>
     *   <li>{@code -DEnvironment=QA1|QA|UAT|STG|DEV|DES} - Entorno de ejecución</li>
     *   <li>{@code -DBrowser=CHROME|FIREFOX|EDGE|SAFARI} - Navegador a usar</li>
     *   <li>{@code -DrunPipeline=true} - (Opcional) Modo CI/CD</li>
     * </ul>
     *
     * <p><b>Ejemplo:</b> {@code mvn test -DEnvironment=QA1 -DBrowser=CHROME}</p>
     *
     * <p>En modo pipeline ({@code -DrunPipeline=true}), las propiedades son obligatorias.
     * En desarrollo local, usa valores los por defecto: QA1 y CHROME.</p>
     *
     * @throws IllegalStateException si las propiedades son inválidas o faltan en modo pipeline
     */
    @BeforeAll
    public static void wrapperSetup() {
        // Detecta si se está en pipeline o desarrollo local
        boolean isPipeline = "true".equals(System.getProperty("runPipeline", "false"));

        String environmentProperty = System.getProperty("Environment");
        if (environmentProperty == null) {
            if (isPipeline) {
                throw new IllegalStateException("La propiedad 'Environment' no está configurada en CI/CD.");
            } else {
                log.warn("Environment no configurado, usando valor por defecto: {}", environment);
                environmentProperty = environment.name(); // Usa QA1 por defecto
            }
        }

        switch (environmentProperty) {
            case "QA1", "QA" -> setEnvironment(QA1);
            case "UAT", "STG" -> setEnvironment(UAT1);
            case "DEV", "DES" -> setEnvironment(DEV);
            default -> throw new IllegalStateException("Valor para 'Environment' no válido: " + environmentProperty);
        }

        String browserProperty = System.getProperty("Browser");
        if (browserProperty == null) {
            if (isPipeline) {
                throw new IllegalStateException("La propiedad 'Browser' no está configurada en CI/CD.");
            } else {
                log.warn("Browser no configurado, usando valor por defecto: {}", browsers);
                browserProperty = browsers.name(); // Usa CHROME por defecto
            }
        }

        switch (browserProperty) {
            case "CHROME" -> setBrowsers(CHROME);
            case "FIREFOX" -> setBrowsers(FIREFOX);
            case "EDGE" -> setBrowsers(EDGE);
            case "SAFARI" -> setBrowsers(SAFARI);
            default -> throw new IllegalStateException("Valor para 'Browser' no válido: " + browserProperty);
        }

        log.info("Navegador configurado a: {}", Browser.getBrowsers());
        log.info("Entorno configurado a: {}", Browser.getEnvironment());
        wait = new RobustWebDriverWait(getWebDriver(), Duration.ofSeconds(10));
    }

    @Before
    public void before(Scenario scenario) {
        log.info("Ejecutando escenario actualmente: {}", scenario.getName());
    }

    @AfterStep
    public static void reportContentLogger(Scenario scenario) {
        int index = 1;

        for (utils.ScreenshotBus.Shot shot : utils.ScreenshotBus.drain()) {
            // Usar la descripción por imagen como el título; en caso contrario, usar "Captura N"
            String caption = (shot.description() != null && !shot.description().isBlank())
                    ? shot.description()
                    : "Captura de pantalla " + index;

            // Adjuntar nativamente en Cucumber (Spark muestra el título si base64image=true)
            scenario.attach(shot.bytes(), "image/png", "📸 " + caption);

            // también guardar en disco y agregar a Extent por ruta (miniaturas confiables)
            try {
                java.nio.file.Path dir = java.nio.file.Paths.get("target", "screenshots");
                java.nio.file.Files.createDirectories(dir);
                String safe = scenario.getName().replaceAll("[^a-zA-Z0-9._-]", "_");
                java.nio.file.Path f = dir.resolve(safe + "_" + System.nanoTime() + ".png");
                java.nio.file.Files.write(f, shot.bytes());

                // Adjuntar la imagen al paso actual en Extent (basado en ruta)
                ExtentCucumberAdapter.addTestStepScreenCaptureFromPath(f.toString());
            } catch (Exception ignore) {  }

            index++;
        }
    }

    @After
    public static void restartDriver() {
        if (Objects.requireNonNull(Browser.getWebDriver().getCurrentUrl()).contains(getSignatureServer()) || !Objects.requireNonNull(Browser.getWebDriver().getCurrentUrl()).contains("about:blank")) {
            LoginActions.logout();
        }
        if (browsers == CHROME) {
            webDriver.switchTo().newWindow(WindowType.WINDOW);
            WindowManager.closeParentWindow();
        }
    }

    @AfterAll
    public static void tearDown() {
        if ("true".equals(System.getProperty("runPipeline", "false"))) {
            log.info("Ejecutando en pipeline, no se cerrará el navegador");
            return;
        }
        if (webDriver != null) {
            if (Browser.getWebDriver().getWindowHandles().toArray(new String[0]).length > 1) {
                WindowManager.switchToParentWindow();
                WindowManager.closeChildWindow();
            }
            if (Objects.requireNonNull(webDriver.getCurrentUrl()).contains(getSignatureServer())) {
                LoginActions.logout();
            } else {
                webDriver.quit();
                log.info("Teardown completed");
            }
        }
    }
}
