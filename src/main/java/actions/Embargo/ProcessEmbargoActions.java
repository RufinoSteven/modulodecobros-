package actions.Embargo;

import config.Browser;
import dataStorageModel.AccountBalanceId;
import dataStorageModel.Embargo.EmbargoPageData;
import lombok.Setter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Embargo.ProcessEmbargoPage;
import utils.ExtentReportManager;
import utils.Navigator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static config.Browser.wait;

/**
 * Esta clase proporciona acciones que se pueden realizar en la página de procesamiento de embargos.
 *
 * <p>Ejemplo de uso:</p>
 *
 * <pre>
 * ProcessEmbargoActions processActions = new ProcessEmbargoActions();
 * processActions.navigateToProcessEmbargoPage();
 * processActions.processEmbargo("12345");
 * </pre>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */
public class ProcessEmbargoActions {
    private final ProcessEmbargoPage processEmbargoPage = new ProcessEmbargoPage();
    private final ConsultEmbargoActions consultEmbargoActions = new ConsultEmbargoActions();
    public final EmbargoPageData embargoPageData = new EmbargoPageData();
    private static final Logger log = Logger.getLogger(ProcessEmbargoActions.class.getName());

    @Setter
    private AccountBalanceId accountBalanceId;

    public void pressSpecialKey(String key) {
        Actions actions = new Actions(Browser.getWebDriver());
        try {
            switch (key.toUpperCase()) {
                case "F12" -> actions.sendKeys(Keys.F12).perform();
                case "F14" -> {
                    Thread.sleep(3000);
                    actions.keyDown(Keys.SHIFT)
                            .keyDown(Keys.F2)
                            .keyUp(Keys.F2)
                            .keyUp(Keys.SHIFT)
                            .build()
                            .perform();
                }
                default -> actions.sendKeys(Keys.valueOf(key)).perform();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void navigateToProcessEmbargoPage() throws InterruptedException {
        Thread.sleep(2000);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@name='in_2515_60' and @type='text' and contains(@class, 'HATSINPUT')]")
            ));
            log.info("El input ya está visible. Continuando con el flujo normal.");
        } catch (TimeoutException e) {
            log.info("El input no está visible. Clic en F12 para mostrarlo...");
            pressSpecialKey("F12");
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@name='in_2515_60' and @type='text' and contains(@class, 'HATSINPUT')]")
            ));
        }
        WebElement input = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.transactionSelectionInput));
        input.click();
        input.clear();
        input.sendKeys("209465");
        input.sendKeys(Keys.ENTER);
    }

    public void positionOnEmbargo(String embargoNumber) {
        WebElement input = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.positionOperationInput));
        input.clear();
        input.sendKeys(embargoNumber);
        input.sendKeys(Keys.ENTER);
    }

    private void selectOption(String option) {
        WebElement input = wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.visibilityOf(processEmbargoPage.optionInput)
        ));
        input.click();
        input.clear();
        input.sendKeys(option);
        input.sendKeys(Keys.ENTER);
    }

    public void selectOption10() {
        selectOption("10");
    }

    public void selectOption1() {
        selectOption("1");
    }

    public void clickApply() throws InterruptedException {
        String valorOperacion = Objects.requireNonNull(
                wait.until(
                        ExpectedConditions.refreshed(
                                ExpectedConditions.visibilityOf(processEmbargoPage.valorOperacionInput)
                        )
                ).getText()
        ).trim();
        embargoPageData.setLastOperationAmount(valorOperacion);
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(processEmbargoPage.applyBtn)).click();
    }


    public void enterProcessOption() {
        String embargoNumber = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.embargoNumbervalue))
                .getText().trim();
        embargoPageData.setPreviousOperationNumberClose(embargoNumber);
        WebElement optionInput = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.processOptionInput));
        optionInput.clear();
        optionInput.sendKeys("1");
        optionInput.sendKeys(Keys.ENTER);
    }

    public void confirmationProcessOption() {
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtn)).click();
        pressSpecialKey("F14");
    }

    public double captureOperationValue() {
        String rawValue = processEmbargoPage.operationValueText.getText().trim();
        log.info("Texto capturado: " + rawValue);
        return Double.parseDouble(rawValue.replaceAll("[^0-9.]", ""));
    }


    public void selectValidAccount(String operationType, EmbargoPageData shareEmbargoPageData) {
        WebElement targetInput;
        String valueToSend;

        switch (operationType) {
            case "8", "6", "10", "12" -> {
                String accountNumber;
                try {
                    accountNumber = wait.until(ExpectedConditions.refreshed(
                            ExpectedConditions.visibilityOf(processEmbargoPage.getAccount)
                    )).getText().trim();
                } catch (Exception e) {
                    try {
                        accountNumber = wait.until(ExpectedConditions.refreshed(
                                ExpectedConditions.visibilityOf(processEmbargoPage.getAccountSecond)
                        )).getText().trim();
                    } catch (Exception ex) {
                        accountNumber = "Cuenta no encontrada";
                    }
                }
                embargoPageData.setAccountNumber(accountNumber);
                embargoPageData.setAccountType(wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(processEmbargoPage.getAccountType))).getText().trim());
                embargoPageData.setLastOperationAmount(wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(processEmbargoPage.getAmountOperation))).getText().trim());
                targetInput = processEmbargoPage.inputDescription2;
                valueToSend = "1";
                wait.until(ExpectedConditions.visibilityOf(targetInput)).click();
                wait.until(ExpectedConditions.visibilityOf(targetInput)).clear();
                wait.until(ExpectedConditions.visibilityOf(targetInput)).sendKeys(valueToSend);
            }
            case "11", "9" -> {
                embargoPageData.setAccountNumber(wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(processEmbargoPage.getAccountBasicEmbargo))).getText().trim());
                embargoPageData.setAccountType(wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(processEmbargoPage.getAccountType))).getText().trim());
                targetInput = processEmbargoPage.inputDescription;
                valueToSend = "TES";
                wait.until(ExpectedConditions.visibilityOf(targetInput)).click();
                wait.until(ExpectedConditions.visibilityOf(targetInput)).clear();
                wait.until(ExpectedConditions.visibilityOf(targetInput)).sendKeys(valueToSend);
            }
            case "4" -> {
                targetInput = processEmbargoPage.inputDescription2;
                valueToSend = "TES";
                wait.until(ExpectedConditions.visibilityOf(targetInput)).click();
                wait.until(ExpectedConditions.visibilityOf(targetInput)).clear();
                wait.until(ExpectedConditions.visibilityOf(targetInput)).sendKeys(valueToSend);
            }
            default -> {
                int filledCount = 0;
                List<WebElement> valorARetenerInputs = processEmbargoPage.inputDescription5Amount;
                for (WebElement input : valorARetenerInputs) {
                    input.clear();
                }
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td[normalize-space()=" + shareEmbargoPageData.getAccountNumberBefore() + "]/ancestor::tr[1]//input[@maxlength='18'])[1]"))).sendKeys(embargoPageData.getLastOperationAmount());
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td[normalize-space()=" + shareEmbargoPageData.getAccountNumberBefore() + "]/ancestor::tr[1]//input[@maxlength='3'])[1]"))).sendKeys("TES");
                log.info("Se llenaron " + filledCount + " inputs con 'TES'");
            }

        }
    }

    public void getAccount(EmbargoPageData sharedEmbargoPageData) {
        log.info("Tipo de cuenta recibido en getAccount: " + sharedEmbargoPageData.getAccountTypeBefore());
        String functionNumber = "1".equals(sharedEmbargoPageData.getAccountTypeBefore()) ? "201030" : "201020";
        Navigator.waitForLoad();
        consultEmbargoActions.enterFunctionNumber(functionNumber);
        WebElement accountInput = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.accountNumberInput));
        accountInput.clear();
        accountInput.sendKeys(sharedEmbargoPageData.getAccountNumberBefore());
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtn)).click();
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtnEnter)).click();
    }

    public void getAccountBefore(EmbargoPageData sharedEmbargoPageData) {
        String accountType = sharedEmbargoPageData.getAccountTypeBefore();
        String accountNumber = sharedEmbargoPageData.getAccountNumberBefore();

        if ("1".equals(accountType)) {
            accountType = "SV";
        } else {
            accountType = "DD";
        }

        ExtentReportManager.logStep("Tipo de cuenta: " + accountType, "info");
        ExtentReportManager.logStep("Numero de cuenta: " + accountNumber, "info");
        String functionNumber = switch (accountType) {
            case "SV" -> "201030";
            case "DD" -> "201020";
            default -> "";
        };

        Navigator.waitForLoad();
        consultEmbargoActions.enterFunctionNumber(functionNumber);
        WebElement accountInput = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.accountNumberInput));
        accountInput.clear();
        accountInput.sendKeys(accountNumber);
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtn)).click();
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtnEnter)).click();
    }

    public void cuentaSinSaldo() {
        Navigator.waitForLoad();
        consultEmbargoActions.enterFunctionNumber("201030");
        WebElement accountInput = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.accountNumberInput));
        accountInput.clear();
        accountInput.sendKeys("100000014");
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtn)).click();
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtnEnter)).click();
    }

    public void consultOperation(String operationNumber) {
        Navigator.waitForLoad();
        consultEmbargoActions.enterFunctionNumber("209470");

        WebElement consultReferenceInput = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.consultReferenceInput));
        consultReferenceInput.click();
        consultReferenceInput.clear();
        consultReferenceInput.sendKeys("1");
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtn)).click();

        WebElement operationNumberInput = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.operatioNumberInput));
        operationNumberInput.click();
        operationNumberInput.clear();
        operationNumberInput.sendKeys(operationNumber);
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtn)).click();

        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.confirmBtn)).click();

        WebElement referenceNumberInput = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.referenceNumberInput));
        referenceNumberInput.click();
        referenceNumberInput.clear();
        referenceNumberInput.sendKeys("5");
        wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.okBtn)).click();
    }

    public void confirmProcess() throws InterruptedException {
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(processEmbargoPage.okBtn)).click();
        wait.until(ExpectedConditions.elementToBeClickable(processEmbargoPage.confirmBtn)).click();

        try {
            if (processEmbargoPage.confirmOperationInput.isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(processEmbargoPage.confirmOperationInput)).click();
                processEmbargoPage.confirmOperationInput.sendKeys(Keys.ENTER);
                pressSpecialKey("F14");
            }
        } catch (NoSuchElementException | TimeoutException ignored) {
        }
    }

    public boolean verifyFinalStatus() {
        String statusText = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.finalOperationStatus)).getText().trim();
        return statusText.matches("Procesada Parcialmente|Completada");
    }

    public boolean verifyEmbargoAmount() {
        String expectedAmount = embargoPageData.getLastOperationAmount();
        log.info("🔍 Buscando monto en pantalla: " + expectedAmount);
        boolean found = processEmbargoPage.embargoAmountTable.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .peek(text -> System.out.println("➡️ Texto encontrado: " + text))
                .anyMatch(text -> text.equals(expectedAmount));
        log.info(found ? "✅ Monto encontrado." : "❌ Monto no encontrado.");
        return found;
    }

    public boolean verifyEmbargoNumber() {
        String expectedNumber = embargoPageData.getPreviousOperationNumberClose();
        log.info("🔍 Buscando número de embargo en pantalla: " + expectedNumber);
        boolean found = processEmbargoPage.embargoNumberTable.stream()
                .map(WebElement::getText)
                .map(text -> text.replaceAll("[^0-9]", ""))
                .filter(code -> !code.isEmpty())
                .peek(cleaned -> System.out.println("➡️ Texto limpio encontrado: " + cleaned))
                .anyMatch(code -> code.equals(expectedNumber));
        log.info(found ? "✅ Número de embargo encontrado." : "❌ Número de embargo no encontrado.");
        return found;
    }

    public boolean verifyEmbargoNumberByRefactor() {
        Navigator.waitForLoad();
        return processEmbargoPage.embargoNumberTable.stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .flatMap(text -> Arrays.stream(text.split("\\D+")))
                .filter(code -> !code.isEmpty())
                .anyMatch(code -> code.equals(embargoPageData.getPreviousOperationNumber()));
    }

    private boolean isDefaultOperation(String operationType) {
        return !operationType.equals("8") &&
                !operationType.equals("6") &&
                !operationType.equals("10") &&
                !operationType.equals("12") &&
                !operationType.equals("11") &&
                !operationType.equals("9") &&
                !operationType.equals("4");
    }


    public void processEmbargo(String operationType, EmbargoPageData shareEmbargoPageData) throws InterruptedException {
        log.info("Tipo de operación que se enviará a processEmbargo: " + operationType);
        clickApply();
        enterProcessOption();
        // Solo si estamos en un tipo de operación que cae en el "default"
        if (isDefaultOperation(operationType)) {
            String status;
            try {
                status = wait.until(ExpectedConditions.visibilityOf(processEmbargoPage.embargoStatus)).getText().trim();
                log.info("El estado del embargo es: " + status);
            } catch (TimeoutException | NoSuchElementException e) {
                log.warning("No se encontró el estado del embargo en pantalla. Se continuará con el proceso.");
                status = "Desconocido";
            }

            if (status.equalsIgnoreCase("Completada")) {
                log.info("Estado del embargo es 'Completado'. Se detiene el proceso aquí.");
                return;
            }
        }

        // Si no estaba completado, sigue con el flujo normal
        selectValidAccount(operationType, shareEmbargoPageData);
        confirmProcess();
    }

    public void processEmbargoSinSaldo() throws InterruptedException {
        clickApply();
        enterProcessOption();
    }

    public void processEmbargoAccountSelectionQuery(String operationType) throws InterruptedException {
        clickApply();
        enterProcessOption();
        double operationValue = captureOperationValue();
        selectValidAccountOfClients(operationValue, operationType);
        confirmProcess();
    }

    public void processEmbargoApplySelection() throws InterruptedException {
        clickApply();
    }

    public void processEmbargoEnterProcessOption() {
        enterProcessOption();
    }

    public void selectValidAccountOfClients(double operationValue, String operationType) {
        int numRows = processEmbargoPage.accountRows.size();
        for (int i = 0; i < numRows; i++) {
            WebElement valueInput = processEmbargoPage.getRetainValueInputByRow(i);
            if (valueInput != null) valueInput.clear();

            if (processEmbargoPage.getAccountStatusByRow(i).equals("Abiert")
                    && processEmbargoPage.getAccountNumberByRow(i).equals(accountBalanceId.getAccount_Number())) {
                assert valueInput != null;
                valueInput.sendKeys(String.valueOf(operationValue));
                embargoPageData.setPreviousOperationNumber(operationType.trim());
                embargoPageData.setLastOperationAmount(String.valueOf(operationValue));
                embargoPageData.setAccountNumber(processEmbargoPage.getAccountNumberByRow(i).trim());
                embargoPageData.setAccountType(processEmbargoPage.getAccountNumberByTypeAccount(i).trim());
                processEmbargoPage.getEntityInputByRow(i).clear();
                processEmbargoPage.getEntityInputByRow(i).sendKeys("TES");
                break;
            }
        }
    }
}
