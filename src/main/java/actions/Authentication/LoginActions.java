package actions.Authentication;

import config.Browser;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Authentication.LoginPage;
import utils.AssertionsAndChecks;
import utils.Navigator;

import java.util.Arrays;
import java.util.List;

import static config.Browser.wait;

/**
 * Esta clase proporciona las acciones que se pueden realizar en la página de inicio de sesión.
 *
 * <p>Ejemplo de uso:</p>
 *
 * <pre>
 * LoginActions loginActions = new LoginActions();
 * loginActions.navigateToLoginPage();
 * loginActions.login("usuario", "contraseña");
 * </pre>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */

public class LoginActions {
    private static final LoginPage loginPage = new LoginPage();

    public void navigateToLoginPage() {
        Browser.getWebDriver().get(Browser.getSignatureServer());
    }

    public static void performLogout() {
        if (AssertionsAndChecks.Checks.isVisible(loginPage.getBackButton())) {
            wait.until(ExpectedConditions.visibilityOf(loginPage.getBackButton())).click();
            Navigator.waitForLoad();
        } else if (!AssertionsAndChecks.Checks.isVisible(loginPage.getBackButton())) {
            Actions action = new Actions(Browser.getWebDriver());
            action.sendKeys(Keys.F3).perform();
        }
    }

    public static void logout() {
        while (!isOnMainPage()) {
            new Actions(Browser.getWebDriver()).sendKeys(Keys.F3).perform();
        }
        wait.until(ExpectedConditions.elementToBeClickable(loginPage.exitBtn1)).click();
        wait.until(ExpectedConditions.elementToBeClickable(loginPage.outOkBtn)).click();
    }

    public void login(String username, String password) {
        navigateToLoginPage();
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.visibilityOf(loginPage.usernameInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(loginPage.usernameInput)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOf(loginPage.passwordInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(loginPage.passwordInput)).sendKeys(password);
        wait.until(ExpectedConditions.visibilityOf(loginPage.signInBtn)).click();
    }

    public void backToMainMenu() {
        wait.until(ExpectedConditions.visibilityOf(loginPage.getBackButton())).click();
    }

    public void navigateToMainPage() {
        List<WebElement> buttons = Arrays.asList(loginPage.cancelButton, loginPage.exitBtn1);
        for (WebElement button : buttons) {
            try {
                if (wait.until(ExpectedConditions.elementToBeClickable(button)) != null) {
                    button.click();
                    if (isOnMainPage()) {
                        return;
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void navigateToMainPageWithF3() {
        for (int i = 0; i < 5; i++) {
            if (isOnMainPage()) {
                return;
            }
            new Actions(Browser.getWebDriver()).sendKeys(Keys.F3).perform();
        }
    }

    private static boolean isOnMainPage() {
        try {
            return loginPage.mainOperationInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
