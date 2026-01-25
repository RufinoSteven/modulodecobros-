package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import config.Browser;
import io.cucumber.java.Scenario;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Esta clase gestiona la integración entre las pruebas de Cucumber/Selenium
 * y ExtentReports. Centraliza todo lo necesario para:
 * - Inicializar y configurar el informe HTML
 * - Crear nodos para Features, Scenarios y Steps
 * - Registrar resultados con estado (pass, fail, skip, warning)
 * - Capturar y adjuntar capturas de pantalla
 * <p>
 * Se utiliza ThreadLocal para que cada prueba/hilo tenga su propio contexto,
 * evitando conflictos al ejecutar en paralelo.
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> FEATURE_TL = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> SCENARIO_TL = new ThreadLocal<>();
    private static final ThreadLocal<Scenario> SCENARIO_CTX_TL = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> STEP_TL = new ThreadLocal<>();
    private static final ThreadLocal<Throwable> LAST_ERROR_TL = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> FAILURE_SCREENSHOT_TL =
            ThreadLocal.withInitial(() -> false);

    // Mantiene la compatibilidad con los hooks existentes
    public static volatile String logMessage;
    public static volatile String logStatus;

    /**
     * Inicializa ExtentReports solo una vez.
     * Crea la carpeta "reports" y configura el archivo de salida HTML.
     * También carga la configuración XML opcional y agrega información del sistema (SO, versión de Java).
     */
    public static synchronized ExtentReports getExtent() {
        if (extent == null) {
            String baseDir = "reports";
            File outDir = new File(baseDir);
            if (!outDir.exists() && !outDir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + outDir.getAbsolutePath());
            }
            String reportPath = baseDir + File.separator + "SparkReport.html";
            extent = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Reporte de Automatización");
            spark.config().setReportName("Test Automation Report");

            // Configuración XML
            try {
                URL xml = ExtentReportManager.class.getClassLoader().getResource("ExtentConfig/ExtentReport.xml");
                if (xml != null) spark.loadXMLConfig(new File(xml.toURI()));
            } catch (Exception ignored) {
            }

            extent.attachReporter(spark);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extent;
    }

    /**
     * Crea (o recupera si ya existe) un nodo de Feature en el informe.
     * Un "Feature" agrupa múltiples Scenarios, al igual que en Cucumber.
     */
    public static ExtentTest createFeature(Scenario scenario) {
        String featureName = extractFeatureName(scenario);
        ExtentTest feature = FEATURE_TL.get();
        if (feature == null || !featureName.equals(feature.getModel().getName())) {
            feature = getExtent().createTest(featureName);
            FEATURE_TL.set(feature);
        }
        return feature;
    }

    /**
     * Crea un nodo de Scenario bajo el Feature correspondiente.
     * Cada Scenario contiene sus propios Steps.
     */
    public static void createScenario(Scenario scenario) {
        ExtentTest feature = createFeature(scenario);
        ExtentTest scenarioNode = feature.createNode(scenario.getName());
        SCENARIO_TL.set(scenarioNode);
        SCENARIO_CTX_TL.set(scenario);
    }

    /**
     * Crea un nodo de Step con su palabra clave Gherkin (Given, When, Then, And...).
     * Esto hace que el informe sea más legible y coincida con los pasos de Cucumber.
     */
    public static void createStep(String keyword, String stepText) {
        ExtentTest scenario = SCENARIO_TL.get();
        if (scenario == null) return;

        try {
            ExtentTest stepNode = scenario.createNode(new GherkinKeyword(keyword.trim()), stepText);
            STEP_TL.set(stepNode);
        } catch (ClassNotFoundException e) {
            ExtentTest stepNode = scenario.createNode(keyword.trim() + ": " + stepText);
            STEP_TL.set(stepNode);
        }
    }

    // Registra un mensaje en el Step o Scenario actual.
    public static void logStep(String message, String status) {
        logMessage = message;
        logStatus = status;

        ExtentTest node = getActiveNode();
        if (node == null) return;

        switch (status.toLowerCase()) {
            case "pass":
                node.pass(message);
                break;
            case "fail":
                node.fail(message);
                break;
            case "warn":
            case "warning":
                node.warning(message);
                break;
            case "skip":
                node.skip(message);
                break;
            default:
                node.info(message);
                break;
        }
    }

    /**
     * Registra un fallo de validacion con descripcion y causa probable estandarizada.
     */
    public static void logValidationFailure(String validation, WebElement element, Throwable error) {
        ExtentTest node = getActiveNode();
        if (node == null) return;

        FailureContext context = buildFailureContext(error);
        String elementInfo = describeElement(element);
        StringBuilder message = new StringBuilder();
        message.append("<b>Validacion fallida:</b> ").append(validation);
        if (elementInfo != null && !elementInfo.isBlank()) {
            message.append("<br><b>Elemento:</b> ").append(elementInfo);
        }
        message.append("<br><b>Causa probable:</b> ").append(context.probableCause);
        if (context.errorSummary != null && !context.errorSummary.isBlank()) {
            message.append("<br><b>Detalle:</b> ").append(context.errorSummary);
        }
        node.warning(message.toString());
    }

    /**
     * Método Principal: Captura una captura de pantalla del navegador y la adjunta al informe.
     * También la adjunta al escenario de Cucumber para que aparezca en ambos lugares.
     */
    public static void captureScreenshot(String description) {
        attachScreenshot(description);
    }

    /**
     * Captura de pantalla tolerante a errores (para escenarios fallidos).
     */
    public static boolean captureScreenshotOnFailure(String description) {
        try {
            return attachScreenshot(description);
        } catch (Exception e) {
            logStep("No fue posible capturar la evidencia visual: " + e.getMessage(), "warning");
            return false;
        }
    }

    private static boolean attachScreenshot(String description) {
        ExtentTest node = getActiveNode();
        if (node == null) return false;

        byte[] screenshot = takeScreenshot();
        if (screenshot == null) return false;

        String base64 = Base64.getEncoder().encodeToString(screenshot);
        String message = "📸" + ((description != null && !description.isEmpty()) ? description : "Screenshot");
        node.info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());

        // Attach to Cucumber Scenario so the Extent Adapter HTML also shows it
        Scenario sc = SCENARIO_CTX_TL.get();
        if (sc != null) {
            try {
                sc.attach(screenshot, "image/png", message);
            } catch (Exception ignored) {
            }
        }
        return true;
    }

    // Ayudante interno: realmente toma los bytes de la captura de pantalla desde WebDriver.
    private static byte[] takeScreenshot() {
        try {
            if (Browser.getWebDriver() instanceof TakesScreenshot) {
                return ((TakesScreenshot) Browser.getWebDriver()).getScreenshotAs(OutputType.BYTES);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Marca el Step actual como finalizado con el estado dado.
     * Útil para escenarios donde un paso falla o se omite.
     */
    public static void finishStep(String status, Throwable error) {
        ExtentTest step = STEP_TL.get();
        if (step == null) return;

        switch (status.toUpperCase()) {
            case "FAILED":
                LAST_ERROR_TL.set(error);
                logFailureContext("Paso", error);
                if (!Boolean.TRUE.equals(FAILURE_SCREENSHOT_TL.get())) {
                    boolean captured = captureScreenshotOnFailure("Captura on-failure del paso");
                    if (captured) FAILURE_SCREENSHOT_TL.set(true);
                }
                if (error != null) {
                    step.fail(error);
                } else {
                    step.fail("Paso fallido");
                }
                break;
            case "SKIPPED":
                step.skip("Paso omitido");
                break;
            case "PENDING":
            case "UNDEFINED":
                step.warning("Estado del paso: " + status);
                break;
        }
    }

    // Marca el Scenario como finalizado con el estado adecuado.
    public static void finishScenario(Scenario scenario) {
        ExtentTest scenarioNode = SCENARIO_TL.get();
        if (scenarioNode == null) return;

        switch (scenario.getStatus()) {
            case FAILED:
                logFailureContext("Escenario", LAST_ERROR_TL.get());
                if (!Boolean.TRUE.equals(FAILURE_SCREENSHOT_TL.get())) {
                    boolean captured = captureScreenshotOnFailure("Captura on-failure del escenario");
                    if (captured) FAILURE_SCREENSHOT_TL.set(true);
                }
                scenarioNode.fail("Escenario fallido");
                break;
            case SKIPPED:
                scenarioNode.skip("Escenario omitido");
                break;
        }
    }


    // Extrae el nombre del archivo de feature (ejemplo: "Login.feature") de la URI.
    private static String extractFeatureName(Scenario scenario) {
        if (scenario.getUri() == null) return "Feature";

        String uri = scenario.getUri().toString();
        try {
            uri = java.net.URLDecoder.decode(uri, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int lastSeparator = Math.max(uri.lastIndexOf('/'), uri.lastIndexOf('\\'));
        return (lastSeparator >= 0 && lastSeparator + 1 < uri.length())
                ? uri.substring(lastSeparator + 1) : "Feature";
    }

    /**
     * Adjunta registros de texto plano al informe de Cucumber.
     * Esto es útil cuando se desea guardar información de depuración adicional.
     */
    public static void attachLog(Scenario scenario) {
        if (scenario != null && logMessage != null && !logMessage.isEmpty()) {
            scenario.attach(logMessage.getBytes(), "text/plain", "Log - " + scenario.getName());
        }
    }

    // Helpers para obtener, establecer o limpiar el contexto actual del paso/escenario.
    public static ExtentTest getCurrentStep() {
        return STEP_TL.get();
    }

    public static void setCurrentStep(ExtentTest step) {
        STEP_TL.set(step);
    }

    public static ExtentTest getScenarioTest() {
        return SCENARIO_TL.get();
    }

    public static void clearCurrentStep() {
        STEP_TL.remove();
    }

    public static void clearScenario() {
        STEP_TL.remove();
        SCENARIO_TL.remove();
        SCENARIO_CTX_TL.remove();
        LAST_ERROR_TL.remove();
        FAILURE_SCREENSHOT_TL.remove();
    }

    /**
     * Vuelca el informe, escribiendo todo lo recopilado en el archivo HTML.
     * Debe llamarse al final de la ejecución de la prueba.
     */
    public static synchronized void flush() {
        if (extent != null) extent.flush();
    }

    private static ExtentTest getActiveNode() {
        ExtentTest node = STEP_TL.get();
        if (node == null) node = SCENARIO_TL.get();
        return node;
    }

    private static void logFailureContext(String scope, Throwable error) {
        ExtentTest node = getActiveNode();
        if (node == null) return;

        FailureContext context = buildFailureContext(error);
        StringBuilder message = new StringBuilder();
        message.append("<b>Contexto de fallo</b>");
        if (scope != null && !scope.isBlank()) {
            message.append(" (").append(scope).append(")");
        }
        message.append("<br><b>Tipo de fallo:</b> ").append(context.failureType);
        message.append("<br><b>Impacto negocio:</b> ").append(context.businessImpact);
        message.append("<br><b>Causa probable:</b> ").append(context.probableCause);
        if (context.errorSummary != null && !context.errorSummary.isBlank()) {
            message.append("<br><b>Detalle:</b> ").append(context.errorSummary);
        }
        node.info(message.toString());
    }

    private static FailureContext buildFailureContext(Throwable error) {
        Throwable root = rootCause(error);
        String failureType = inferFailureType(root);
        String businessImpact = inferBusinessImpact(failureType);
        String probableCause = inferProbableCause(root);
        String errorSummary = summarizeError(root);
        return new FailureContext(failureType, businessImpact, probableCause, errorSummary);
    }

    private static String inferFailureType(Throwable root) {
        if (root instanceof AssertionError) return "Funcional";
        return "Tecnico";
    }

    private static String inferBusinessImpact(String failureType) {
        if ("Funcional".equalsIgnoreCase(failureType)) {
            return "La validacion de negocio no cumple con lo esperado.";
        }
        return "No se pudo completar la validacion de negocio por un error tecnico.";
    }

    private static String inferProbableCause(Throwable root) {
        if (root == null) return "Fallo sin detalle de excepcion.";
        if (root instanceof StaleElementReferenceException) {
            return "El DOM se actualizo y el elemento quedo obsoleto.";
        }
        if (root instanceof TimeoutException) {
            return "Tiempo de espera excedido para localizar o interactuar con el elemento.";
        }
        if (root instanceof NoSuchElementException) {
            return "Elemento no encontrado o locator desactualizado.";
        }
        if (root instanceof ElementClickInterceptedException) {
            return "Elemento cubierto por otro componente o no disponible para click.";
        }
        if (root instanceof ElementNotInteractableException) {
            return "Elemento no interactuable (oculto o deshabilitado).";
        }
        if (root instanceof InvalidElementStateException) {
            return "Elemento en estado invalido para la accion.";
        }
        if (root instanceof NoSuchWindowException) {
            return "La ventana se cerro o el contexto cambio.";
        }
        if (root instanceof NoSuchFrameException) {
            return "El frame no esta disponible o cambio el contexto.";
        }
        if (root instanceof AssertionError) {
            return "La validacion funcional no cumplio el resultado esperado.";
        }
        if (root instanceof NullPointerException) {
            return "Elemento o dependencia nula no inicializada.";
        }
        if (root instanceof WebDriverException) {
            return "Fallo del WebDriver o del navegador durante la ejecucion.";
        }
        return "Causa no clasificada.";
    }

    private static String summarizeError(Throwable root) {
        if (root == null) return null;
        String message = root.getMessage();
        if (message == null || message.isBlank()) return root.getClass().getSimpleName();
        return root.getClass().getSimpleName() + ": " + message;
    }

    private static Throwable rootCause(Throwable error) {
        Throwable current = error;
        while (current != null && current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current;
    }

    private static String describeElement(WebElement element) {
        if (element == null) return null;
        String raw = element.toString();
        int arrow = raw.indexOf("->");
        if (arrow >= 0) {
            String cut = raw.substring(arrow + 2).trim();
            if (cut.endsWith("]")) {
                cut = cut.substring(0, cut.length() - 1).trim();
            }
            return cut;
        }
        return raw;
    }

    private record FailureContext(String failureType, String businessImpact, String probableCause, String errorSummary) {
    }
}