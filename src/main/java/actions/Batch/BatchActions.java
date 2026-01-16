package actions.Batch;

import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Lots.BatchPage;
import utils.ExtentReportManager;

import static config.Browser.wait;
import static org.openqa.selenium.Keys.ENTER;

/**
 * Esta clase proporciona acciones que se pueden realizar en una transacción de lote, como agregar un lote, realizar una transacción y postearla.
 *
 * <p>Ejemplo de uso:</p>
 *
 * <pre>
 * BatchActions batchActions = new BatchActions();
 * batchActions.addTransactionToBatch();
 * </pre>
 *
 * @author Edgar Rodríguez
 * @version 1.0
 */


public class BatchActions {
    private final BatchPage batchPage = new BatchPage();

    // Método para seleccionar el entorno de trabajo en la pantalla principal de Signature
    public void selectEnvironment(String environment) {
        if (environment.equalsIgnoreCase("QA")) {
            wait.until(ExpectedConditions.visibilityOf(batchPage.inputQAEnvironment)).sendKeys("1");
            wait.until(ExpectedConditions.visibilityOf(batchPage.inputQAEnvironment)).sendKeys(ENTER);
        } else if (environment.equalsIgnoreCase("GL")) {
            wait.until(ExpectedConditions.visibilityOf(batchPage.inputGLEnvironment)).sendKeys("1");
            wait.until(ExpectedConditions.visibilityOf(batchPage.inputGLEnvironment)).sendKeys(ENTER);
        }
    }

    // Método para acceder a la pantalla de Transacciones de Lote
    public void accessBatchTransaction() {
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputTransactions)).clear();
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputTransactions)).sendKeys("102010");
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputTransactions)).sendKeys(ENTER);
    }

    // Método para crear una transacción de lote
    public void createBatchTransaction(String batchName, String batchType) {
        wait.until(ExpectedConditions.elementToBeClickable(batchPage.btnCreate)).click();
        wait.until(ExpectedConditions.visibilityOf(batchPage.batchName)).sendKeys(batchName);
        wait.until(ExpectedConditions.visibilityOf(batchPage.batchType)).clear();
        wait.until(ExpectedConditions.visibilityOf(batchPage.batchType)).sendKeys(batchType);
        ExtentReportManager.captureScreenshot("Datos del Lote Creado");
        wait.until(ExpectedConditions.elementToBeClickable(batchPage.btnOK)).click();
        wait.until(ExpectedConditions.visibilityOf(batchPage.btnCancel)).click();
        searchBatch(batchName);
    }

    // Método para buscar un lote y confirmar la búsqueda
    public void searchBatch(String batchName) {
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputFindBatch)).sendKeys(batchName);
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputFindBatch)).sendKeys(ENTER);
        wait.until(driver -> batchPage.getConfirmLot(batchName).isDisplayed());
    }

    // Método para ingresar a la pantalla de transacciones del lote
    public void enterTransactionScreen(String addTransactionOption) {
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputFirstBatchFound)).sendKeys(addTransactionOption);
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputFirstBatchFound)).sendKeys(ENTER);
    }

    // Método para crear un nuevo ítem desde un lote
    public void createNewItemFromBatch(String accountTransaction, String transactionCode, String transactionAmount, String currencyCode, String transactionCostCenter) {
        wait.until(ExpectedConditions.visibilityOf(batchPage.btnCreate)).click();
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputAccount)).sendKeys(accountTransaction);
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputtransaccionCode)).sendKeys(transactionCode);
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputTransaccionAmount)).sendKeys(transactionAmount);
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputCurrencyCode)).clear();
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputCurrencyCode)).sendKeys(currencyCode);
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputCostCenter)).sendKeys(transactionCostCenter);
        ExtentReportManager.captureScreenshot("Se completan los datos requeridos para el crédito a la cuenta " + accountTransaction);
        wait.until(ExpectedConditions.elementToBeClickable(batchPage.btnOK)).click();
        confirmAddedTransaction(accountTransaction);
        ExtentReportManager.captureScreenshot("Es realizado el crédito a la cuenta " + accountTransaction);
    }

    // Método para cambiar el estado de un lote a completado
    public void completeBatch(String batchName) {
        searchBatch(batchName);
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputFirstBatchFound)).sendKeys("3");
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputFirstBatchFound)).sendKeys(ENTER);
        wait.until(ExpectedConditions.visibilityOf(batchPage.firstBatchConditionComplete));
        ExtentReportManager.captureScreenshot("El lote " + batchName + " se muestra correctamente como completado");
    }

    // Método para confirmar que se ha añadido una nueva transacción al lote
    public void confirmAddedTransaction(String accountTransaction) {
        wait.until(ExpectedConditions.visibilityOf(batchPage.getConfirmTransaction(accountTransaction)));
    }

    // Método para cambiar el estado de un lote a posteado
    public void postBatch(String batchName) {
        searchBatch(batchName);
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputFirstBatchFound)).sendKeys("10");
        wait.until(ExpectedConditions.visibilityOf(batchPage.inputFirstBatchFound)).sendKeys(ENTER);
        wait.until(ExpectedConditions.visibilityOf(batchPage.btnConfirm)).click();
        wait.until(ExpectedConditions.visibilityOf(batchPage.btnRenew)).click();
        wait.until(ExpectedConditions.visibilityOf(batchPage.btnRenew)).click();
        wait.until(ExpectedConditions.visibilityOf(batchPage.firstBatchConditionPost));
        ExtentReportManager.captureScreenshot("El lote " + batchName + " es posteado correctamente.");
    }

    // Método para navegar a la pantalla de transacciones de lote
    public void navigateBatchTransaction(String environment) {
        selectEnvironment(environment);
        accessBatchTransaction();
    }

    // Método para crear un lote
    public void createBatch(String batchName, String batchType) {
        createBatchTransaction(batchName, batchType);
        searchBatch(batchName);
    }

    // Método general que incluye todas las acciones para añadir una transacción a un lote y postearlo.
    public void addTransactionToBatch(String batchName, String accountTransaction, String transactionCode,
                                      String transactionAmount, String currencyCode, String transactionCostCenter) {
        enterTransactionScreen("12");
        createNewItemFromBatch(accountTransaction, transactionCode, transactionAmount, currencyCode, transactionCostCenter);
        wait.until(ExpectedConditions.visibilityOf(batchPage.btnCancelAddTransaction)).click();
        wait.until(ExpectedConditions.visibilityOf(batchPage.btnCancel)).click();
        completeBatch(batchName);
        postBatch(batchName);
    }
}
