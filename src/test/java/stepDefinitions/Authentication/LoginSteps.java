package stepDefinitions.Authentication;

import actions.Authentication.LoginActions;
import com.banreservas.commons.properties.PropManager;
import config.Browser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import utils.ExtentReportManager;
import utils.Navigator;
import utils.WindowManager;

import java.util.logging.Logger;

/**
 * This class implements the steps for the Cucumber scenarios related to log in.
 *
 * <p>This class connects the scenarios defined in the .feature files
 * with the automation code, using the actions defined in LoginActions.</p>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */

public class LoginSteps {
    private final LoginActions loginActions = new LoginActions();
    private static final Logger log = Logger.getLogger(LoginSteps.class.getName());
    private static final String USER_CONFIG_FILE = "userConfig.properties";

    /**
     * Loads credentials from userConfig.properties.
     *
     * <p>Supports encrypted values using the same mechanism used for DB configs.</p>
     *
     * @return String array where [0] is username and [1] is password (empty strings if not found)
     */
    public static String[] loadCredentials() {
        try {
            PropManager pm = new PropManager(USER_CONFIG_FILE);
            String username = normalizeValue(getOrDecrypt(pm, "signature.username"));
            if (username.isBlank()) {
                username = normalizeValue(getOrDecrypt(pm, "signature.user"));
            }
            String password = normalizeValue(getOrDecrypt(pm, "signature.password"));
            return new String[]{username, password};
        } catch (Exception e) {
            log.severe("Error loading credentials: " + e.getMessage());
            return new String[]{"", ""};
        }
    }

    private static String getOrDecrypt(PropManager pm, String key) {
        String decrypted = pm.decryptProperty(key);
        if (decrypted != null && !decrypted.isBlank()) {
            return decrypted;
        }
        return pm.getProperty(key);
    }

    private static String normalizeValue(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("['\"]", "").trim();
    }

    @Given("^el usuario navega a la página de login$")
    public void navigateToLoginPage() {
        loginActions.navigateToLoginPage();
        Navigator.waitForLoad();
    }

    @When("el usuario inicia sesion con el usuario {string} y contraseña {string}")
    public void elUsuarioIniciaSesionConElYContraseña(String username, String password) {
        loginActions.navigateToLoginPage();
        Navigator.waitForLoad();
        loginActions.login(username, password);
        Navigator.waitForLoad();
    }

    @Then("El usuario clickea el boton de Salir")
    public void elUsuarioClickeaElBotonDeSalir() throws InterruptedException {
        Thread.sleep(3000);
        new Actions(Browser.getWebDriver()).sendKeys(Keys.F3).perform();
        Navigator.waitForLoad();
    }

    @Then("El usuario navega a la pagina principal")
    public void elUsuarioNavegaALaPrincipalConF3() {
        loginActions.navigateToMainPageWithF3();
        Navigator.waitForLoad();
    }

    @Then("el usuario regresa a la ventana principal")
    public void elUsuarioRegresaALaVentanaPrincipal() {
        WindowManager.switchToParentWindow();
        WindowManager.closeChildWindow();
    }

    @Given("El usuario se autentica con sus credenciales")
    public void elUsuarioSeAutenticaConSusCredenciales() {
        String[] credentials = LoginSteps.loadCredentials();
        loginActions.navigateToLoginPage();
        Navigator.waitForLoad();
        loginActions.login(credentials[0], credentials[1]);
        ExtentReportManager.logStep("Se ingreso a signature correctamente, El nombre de usuario utilizado: " + credentials[0], "pass");
        Navigator.waitForLoad();
    }
}
