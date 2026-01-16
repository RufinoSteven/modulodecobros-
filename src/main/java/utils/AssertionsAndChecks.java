package utils;

import config.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
                wait.until(ExpectedConditions.elementToBeClickable(webElement));
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public static boolean isVisible(WebElement webElement) {
            try {
                wait.until(ExpectedConditions.visibilityOf(webElement));
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public static boolean isSelected(WebElement webElement) {
            try {
                wait.until(ExpectedConditions.elementToBeSelected(webElement));
                return false;
            } catch (Exception e) {
                return true;
            }
        }

        public static boolean isAlertPresent() {
            try {
                wait.until(ExpectedConditions.alertIsPresent());
                Browser.getWebDriver().switchTo().alert();
                return true;
            } catch (Exception Ex) {
                return false;
            }
        }
    }

}
