package actions.Embargo;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Embargo.ConsultEmbargoPage;

import java.util.logging.Logger;

import static config.Browser.wait;

public class ConsultEmbargoActions {
    private final ConsultEmbargoPage consultEmbargoPage = new ConsultEmbargoPage();
    private static final Logger log = Logger.getLogger(ConsultEmbargoActions.class.getName());

    private void enterTextAndSubmit(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).clear();
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(Keys.ENTER);
    }

    public void enterFunctionNumber(String functionNumber) {
        log.info("Ingresando número de función: " + functionNumber);
        wait.until(ExpectedConditions.visibilityOf(consultEmbargoPage.functionInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(consultEmbargoPage.functionInput)).sendKeys(functionNumber);
        wait.until(ExpectedConditions.visibilityOf(consultEmbargoPage.functionInput)).sendKeys(Keys.ENTER);
    }
}
