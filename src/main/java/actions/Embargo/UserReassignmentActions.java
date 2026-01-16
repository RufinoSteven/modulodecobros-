package actions.Embargo;

import lombok.Getter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Embargo.UserReassignmentPage;

import java.util.logging.Logger;

import static config.Browser.wait;

public class UserReassignmentActions {
    @Getter
    private static String storedUserName;
    private static final Logger log = Logger.getLogger(UserReassignmentActions.class.getName());
    private final UserReassignmentPage userReassignmentPage = new UserReassignmentPage();

    public String captureUserName() {
        wait.until(ExpectedConditions.visibilityOf(userReassignmentPage.userNameField));
        storedUserName = userReassignmentPage.getUsernameFromTable();
        log.info("Nombre de usuario capturado: " + storedUserName);
        return storedUserName;
    }

    // todo: Método auxiliar para enviar texto a un input y presionar ENTER
    private void sendKeysAndEnter(org.openqa.selenium.WebElement element, String text) throws InterruptedException {
        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(element)).clear();
        element.sendKeys(text);
        element.sendKeys(Keys.ENTER);
    }

    public void enterReferenceReassignment(String reference) throws InterruptedException {
        sendKeysAndEnter(userReassignmentPage.optionInput, reference);
    }

    public void enterFunctionNumberReassignment(String functionNumber) throws InterruptedException {
        sendKeysAndEnter(userReassignmentPage.referenceInput, functionNumber);
    }

    public void enterOperationNumberInput(String operationNumberInput) {
        var element = wait.until(ExpectedConditions.visibilityOf(userReassignmentPage.operationNumberInput));
        element.click();
        element.clear();
        element.sendKeys(operationNumberInput);
        element.sendKeys(Keys.ENTER);
    }

    public void clickConfirmAction() throws InterruptedException {
        var okButton = wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(userReassignmentPage.okButton)));
        okButton.click();
        Thread.sleep(1000);
        okButton.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(userReassignmentPage.confirmationButton))).click();
    }

    public void enterReference2(String reference2) throws InterruptedException {
        sendKeysAndEnter(userReassignmentPage.option2Input, reference2);
    }

    public void assignUserIDtoEmbargo() {
        var userInput = wait.until(ExpectedConditions.visibilityOf(userReassignmentPage.userNameInput));
        userInput.click();
        userInput.clear();

        if (storedUserName != null && !storedUserName.isEmpty()) {
            log.info("El nombre de usuario capturado es: " + storedUserName);
            userInput.sendKeys(storedUserName);
            userInput.sendKeys(Keys.ENTER);
        } else {
            log.warning("No hay un nombre de usuario guardado para escribir.");
        }
    }

    public void confirmFinalAction() throws InterruptedException {
        var okButton = wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(userReassignmentPage.okButton)));
        okButton.click();
        Thread.sleep(1000);
        okButton.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(userReassignmentPage.confirmationButton))).click();
        log.info("Embargo reasignado exitosamente");
    }
}
