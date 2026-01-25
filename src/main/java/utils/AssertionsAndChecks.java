package utils;

import config.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.ExtentReportManager;

import java.util.Objects;

import static config.Browser.wait;

public class AssertionsAndChecks {
    private static final Logger log = LogManager.getLogger(AssertionsAndChecks.class);

    public static void acceptAlert() {
        try {
            Browser.getWebDriver().switchTo().alert().accept();
        } catch (NoAlertPresentException Ex) {
            log.info("No hay ninguna alerta presente");
        }
    }

    public static class Checks {
        public static boolean isClickable(WebElement webElement) {
            try {
                Objects.requireNonNull(webElement, "Elemento nulo en validacion de clickabilidad");
                wait.until(ExpectedConditions.elementToBeClickable(webElement));
                return true;
            } catch (Exception e) {
                ExtentReportManager.logValidationFailure("clickabilidad", webElement, e);
                return false;
            }
        }

        public static boolean isVisible(WebElement webElement) {
            try {
                Objects.requireNonNull(webElement, "Elemento nulo en validacion de visibilidad");
                wait.until(ExpectedConditions.visibilityOf(webElement));
                return true;
            } catch (Exception e) {
                ExtentReportManager.logValidationFailure("visibilidad", webElement, e);
                return false;
            }
        }

        public static boolean isSelected(WebElement webElement) {
            try {
                Objects.requireNonNull(webElement, "Elemento nulo en validacion de seleccion");
                wait.until(ExpectedConditions.elementToBeSelected(webElement));
                return true;
            } catch (Exception e) {
                ExtentReportManager.logValidationFailure("seleccion", webElement, e);
                return false;
            }
        }

        public static boolean isAlertPresent() {
            try {
                wait.until(ExpectedConditions.alertIsPresent());
                Browser.getWebDriver().switchTo().alert();
                return true;
            } catch (Exception Ex) {
                ExtentReportManager.logValidationFailure("alerta presente", null, Ex);
                return false;
            }
        }
    }

}
