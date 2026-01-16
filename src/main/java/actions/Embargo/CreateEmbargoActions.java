package actions.Embargo;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Embargo.CreateEmbargoPage;

import java.util.logging.Logger;

import static config.Browser.wait;

/**
 * Esta clase contiene las acciones que se pueden realizar en la página de Embargos.
 *
 * @author Steven Rufino
 * @version 1.0
 */
public class CreateEmbargoActions {
    private final CreateEmbargoPage consultPage = new CreateEmbargoPage();
    private static final Logger log = Logger.getLogger(CreateEmbargoActions.class.getName());
    public static String lastCreatedEmbargoType;
    public static String lastCreatedEmbargoIdentificationNumber;

    public void enterReference(String reference) throws InterruptedException {
        Thread.sleep(3000);
        WebElement optionInput = wait.until(ExpectedConditions.visibilityOf(consultPage.optionInput));
        optionInput.clear();
        optionInput.sendKeys(reference);
        optionInput.sendKeys(Keys.ENTER);
    }

    public void enterFunctionNumber(String functionNumber) {
        WebElement referenceInput = wait.until(ExpectedConditions.elementToBeClickable(consultPage.referenceInput));
        referenceInput.click();
        referenceInput.clear();
        referenceInput.sendKeys(functionNumber);
        referenceInput.sendKeys(Keys.ENTER);
    }

    public void createFunction() {
        wait.until(ExpectedConditions.visibilityOf(consultPage.btnCrear)).click();
    }

    public void selectOperationTypeAndIdentificationType(String operationType, String identificationType) {
        log.info("🛠️ Iniciando prueba con tipo de operación: " + operationType);
        WebElement operationInput = wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.visibilityOf(consultPage.operationTypeInput)));
        operationInput.clear();
        operationInput.sendKeys(operationType);
        lastCreatedEmbargoType = operationType;
        operationInput.sendKeys(Keys.ENTER);

        WebElement identificationInput = wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.visibilityOf(consultPage.identificationTypeInput)));
        identificationInput.clear();
        identificationInput.sendKeys(identificationType);
        identificationInput.sendKeys(Keys.ENTER);

        switch (operationType) {
            case "2" -> log.info("Procesando como una creación de RETENCIÓN DE EMBARGO...");
            case "6" -> log.info("Procesando como una creación de FIANZA...");
            case "8" -> log.info("Procesando como una creación de CARTA...");
            case "9" -> log.info("Procesando como una creación de CIERRE DE FIANZA...");
            case "10" -> log.info("Procesando como una creación de LEVANTAMIENTO DE FIANZA...");
            case "11" -> log.info("Procesando como una creación de CIERRE DE CARTA...");
            case "12" -> log.info("Procesando como una creación de LEVANTAMIENTO DE CARTA...");
            default -> log.warning("Tipo de operación desconocido: " + operationType);
        }
    }

    public void enterIdentificationNumberAndOffice(String identificationNumber, String office) throws InterruptedException {
        lastCreatedEmbargoIdentificationNumber = identificationNumber;
        Thread.sleep(3000);
        WebElement idInput = wait.until(ExpectedConditions.visibilityOf(consultPage.indentificationNumberInput));
        idInput.click();
        idInput.clear();
        idInput.sendKeys(identificationNumber);

        WebElement officeInput = wait.until(ExpectedConditions.visibilityOf(consultPage.officeInput));
        officeInput.click();
        officeInput.clear();
        officeInput.sendKeys(office);
    }

    public void enterEditOffice(String office) {
        WebElement officeInput = wait.until(ExpectedConditions.visibilityOf(consultPage.officeInput));
        officeInput.click();
        officeInput.clear();
        officeInput.sendKeys(office);
    }

    public void clickConfirmationButton() {
        wait.until(ExpectedConditions.visibilityOf(consultPage.okButton)).click();
        wait.until(ExpectedConditions.visibilityOf(consultPage.createEmbargoButton)).click();
    }

    public void clickConfirmButton() {
        wait.until(ExpectedConditions.visibilityOf(consultPage.okButton)).click();
    }
}
