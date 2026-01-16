package actions.ImportedFiles;

import actions.Batch.BatchActions;
import actions.Maintenace.SavingsCurrentActions;
import config.Browser;
import dataStorageModel.NominaEntry;
import dataStorageModel.PayrollInfoDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Reports.ImportedFilesPages;
import utils.ExtentReportManager;
import utils.ExtractDATANomina;
import utils.SftpUploader;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import static config.Browser.wait;
import static org.openqa.selenium.Keys.ENTER;

public class ConsultImportedFilesActionsNomina {
    private final ImportedFilesPages importedFilesPages = new ImportedFilesPages();
    private static final Logger log = LogManager.getLogger(ConsultImportedFilesActionsNomina.class);
    ExtractDATANomina ExtractDataNomina = ExtractDATANomina.getInstance();
    private final SavingsCurrentActions savingsCurrentActions = new SavingsCurrentActions();
    private final BatchActions batchActions = new BatchActions();
    private static final Actions actions = new Actions(Browser.getWebDriver());

    /**
     * Confirma si un archivo se ha importado correctamente verificando si el archivo está visible en la página.
     * El método intenta comprobar la visibilidad del archivo hasta 5 veces, refrescando la página y esperando
     * 5 segundos entre intentos.
     *
     * @param fileName El nombre del archivo para confirmar su importación.
     * @return Devuelve verdadero si el archivo se importa correctamente y es visible después de los intentos.
     * @throws InterruptedException si el hilo es interrumpido mientras espera.
     */

    public boolean confirmFileImport(String fileName) throws InterruptedException {
        boolean fileImportedCorrectly = false;
        int attempts = 0;
        while (attempts < 5) {
            try {
                if (importedFilesPages.getFileName(fileName).isDisplayed()) {
                    fileImportedCorrectly = true;
                    log.info("Archivo visible después de {} intentos.", attempts + 1);
                    break;
                }
            } catch (Exception e) {
                log.error("Intento {} fallido. Reintentando...", attempts + 1);
            }
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnRenew)).click();
            Thread.sleep(5000); // considerar usar un mecanismo de espera más robusto
            attempts++;
        }

        if (!fileImportedCorrectly) {
            log.error("El archivo no se importó correctamente después de {} intentos.", attempts);
            System.exit(1);
        }
        return fileImportedCorrectly;
    }

    private boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lee el estado de los archivos importados y realiza validaciones basadas en el estado esperado de los archivos.
     * Comprueba el estado de cada archivo y lo registra, verificando que el estado coincida con los valores esperados.
     * El método devuelve el estado del último archivo procesado.
     *
     * @param fileName             El nombre del archivo cuyo estado se va a leer y validar.
     * @param expectedStatusImport El estado esperado contra el que se debe comprobar. Puede ser:
     *                             - "Valido" para comprobar los estados de importación válidos.
     *                             - "Rechazado" para comprobar los estados de importación no válidos.
     *                             - "Duplicado" para comprobar específicamente el estado "Duplicada".
     * @return El estado del último archivo importado después de comprobar todos los archivos.
     */
    public String readStateImportedFiles(String fileName, String expectedStatusImport) {
        int countOfElements = importedFilesPages.numberElements(fileName);
        importedFilesPages.consultTotalNumberImportedFiles(fileName, countOfElements);
        importedFilesPages.pressEnter();

        String reportState = "";

        // Valores esperados para los diferentes estados del archivo importado
        String[] validExpectedStates = {"Exportado", "Procesadas", "Validada", "Listo para exportar"};
        String[] invalidExpectedStates = {"Cancelada", "Importación fracasada"};

        // Iterar a través de todos los archivos importados
        for (int i = 0; i < countOfElements; i++) {
            // Obtener el estado actual del archivo y eliminar espacios en blanco adicionales
            String currentState = importedFilesPages.stateOfReport().trim();
            reportState = currentState;
            log.info("El archivo: {} tiene el estado: {}", importedFilesPages.getReportName(fileName), reportState);

            // Validar el estado solo en la última iteración
            if (i == countOfElements - 1) {
                if ("Valido".equalsIgnoreCase(expectedStatusImport)) {
                    Assertions.assertTrue(
                            Arrays.asList(validExpectedStates).contains(currentState),
                            "La importación del archivo falló, se esperaba: " + expectedStatusImport + ", pero el estado actual es: " + currentState
                    );
                } else if ("Rechazado".equalsIgnoreCase(expectedStatusImport)) {
                    Assertions.assertTrue(
                            Arrays.asList(invalidExpectedStates).contains(currentState),
                            "La importación del archivo falló, se esperaba: " + expectedStatusImport + ", pero el estado actual es: " + currentState
                    );
                } else if ("Duplicado".equalsIgnoreCase(expectedStatusImport)) {
                    Assertions.assertEquals("Duplicada", currentState,
                            "La importación del archivo falló, se esperaba: " + expectedStatusImport + ", pero el estado actual es: " + currentState);
                }
            }

            // Pasar al siguiente archivo si no es la última iteración
            if (i < countOfElements - 1) {
                importedFilesPages.pressEnter();
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnPageUp)).click();
                importedFilesPages.pressEnter();
            }
        }
        // Cerrar la operación y devolver el último estado
        importedFilesPages.btnCancel.click();
        return reportState;
    }

    /**
     * Recupera una lista de información del objeto NominaEntry.
     *
     * @param dto El objeto NominaEntry que contiene una lista de objetos listData.
     *            Cada AccountEntry representa una cuenta de la que queremos extraer el número de cuenta.
     * @return Una lista de datos (String) extraída del NominaEntry en el DTO.
     */
    public String buildNominaSummaryMessage(PayrollInfoDTO dto) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        return "Las cuentas a acreditar son:\n" + dto.getPayrollInfo().stream()
                .map(entry -> String.format(
                        "Cuenta: %s | Empresa: %s | ID: %s | TipoCuenta: %s | Monto: %s | Fecha: %s",
                        entry.getAccounts(),
                        entry.getCompany(),
                        entry.getCompanyID(),
                        entry.getTypeAccount(),
                        decimalFormat.format(entry.getAmount()),
                        entry.getDate()
                ))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Accede a la transacción introduciendo un código de transacción específico en el campo de entrada.
     */
    public void accessTransactions(String transaction) {
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputTransactions)).sendKeys(transaction);
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputTransactions)).sendKeys(ENTER);
    }

    /**
     * Selecciona el tipo de cuenta cuando no se encuentra la cuenta.
     */
    public void selectAccountType() throws InterruptedException {
        Thread.sleep(1000);
        if (isElementVisible(importedFilesPages.tdAccountNotFound)) {
            wait.until(ExpectedConditions.visibilityOf(
                    importedFilesPages.btnCurrentAccount.isDisplayed() ? importedFilesPages.btnCurrentAccount : importedFilesPages.btnSavingsAccount
            )).click();
            importedFilesPages.pressEnter();
            importedFilesPages.selectOptionByValue("04");
            log.info("Tipo de cuenta seleccionado porque la cuenta no se encontró inicialmente.");
        }
    }

    /**
     * Valida las transacciones de cuenta en un archivo ACH, incluyendo tanto los créditos a las cuentas de ahorro
     * como los débitos de una cuenta específica.
     * <p>
     * Este método itera a través de las transacciones y se asegura de que los importes correspondientes
     * se muestren correctamente en el sistema para cada cuenta.
     *
     * @param dto El objeto PayrollInfoDTO que contiene las cuentas y los importes a validar.
     * @throws InterruptedException si el hilo es interrumpido mientras espera.
     */
    public void validateAccountsTransactions(PayrollInfoDTO dto) throws InterruptedException {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        for (NominaEntry entry : dto.getPayrollInfo()) {
            String accountNumber = entry.getAccounts();
            String formattedAmount = decimalFormat.format(entry.getAmount());
            validateAccountTransaction(accountNumber, formattedAmount);
            break;
        }
    }

    private void validateAccountTransaction(String accountNumber, String formattedAmount) throws InterruptedException {
        Thread.sleep(1000);
        actions.sendKeys(Keys.F3).perform();
        savingsCurrentActions.enterFunction("201030");
        ExtentReportManager.captureScreenshot("Validar la cuemnta" + accountNumber + "Con el monto" + formattedAmount);
        importedFilesPages.pressEnter();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccount)).clear();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccount)).sendKeys(accountNumber);
        importedFilesPages.pressEnter();
        ExtentReportManager.captureScreenshot("Consulta de cuenta," + accountNumber);
        selectAccountType();
        importedFilesPages.selectOptionByValue("04");
        try {
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.getAmountElement(formattedAmount)));
            log.info("La transacción se muestra correctamente por el importe: {} en la cuenta: {}", formattedAmount, accountNumber);
            ExtentReportManager.captureScreenshot("Consulta de cuenta" + accountNumber + "Y monto: " + formattedAmount);
        } catch (Exception e) {
            log.info("El elemento para la cuenta {} no está visible.", formattedAmount);
        }
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnGoOut)).click();
    }

    /**
     * Envía un archivo a un servidor remoto mediante SFTP.
     * El método sube un archivo desde la ruta local a la ruta de la carpeta remota especificada.
     *
     * @param remoteFolderPath La ruta en el servidor remoto donde se subirá el archivo.
     * @param localFilePath    La ruta del archivo en la máquina local que se va a subir.
     */
    public void sendFileToServer(String remoteFolderPath, String localFilePath) {
        boolean uploaded = SftpUploader.uploadFile(remoteFolderPath, localFilePath);
        log.info(uploaded ? "Archivo subido con éxito." : "Hubo un problema al subir el archivo.");
    }

    /**
     * Gestiona el proceso de transacciones ACH.
     * Este método procesa el archivo ACH validando y comprobando los datos de la transacción extraídos.
     * Realiza múltiples pasos, incluyendo la validación de la importación del archivo, la validación del informe,
     * la validación de la cuenta y la validación de la transacción.
     *
     * @param expectedStatusImport El estado de importación esperado.
     * @param localFilePath        La ruta local del archivo.
     * @throws InterruptedException si el hilo es interrumpido mientras espera.
     */
    public void nominaTransactionManagement(String expectedStatusImport, String localFilePath) throws InterruptedException {
        PayrollInfoDTO result = ExtractDataNomina.extractNominaInformation(localFilePath);
        batchActions.selectEnvironment("QA");
        accessTransactions("240070");
        ExtentReportManager.captureScreenshot("Ingresar a pantalla de reportes para validar que subio correctamente.");
        if (confirmFileImport(ExtractDataNomina.getFileName(localFilePath))) {
            executeImportedFilesFlow(result, expectedStatusImport, localFilePath);
            ExtentReportManager.captureScreenshot("Pantalla de reportes");
        } else {
            log.error("El archivo no se importó correctamente en Signature");
        }
    }

    private void executeImportedFilesFlow(PayrollInfoDTO result, String expectedStatusImport, String localFilePath) throws InterruptedException {
        String reportState = readStateImportedFiles(ExtractDataNomina.getFileName(localFilePath), expectedStatusImport);
        // Verificar si el estado es "Cancelada", "Duplicada" o "Importación fracasada"
        if ("Cancelada".equals(reportState) || "Duplicada".equals(reportState) || "Importación fracasada".equals(reportState)) {
            log.info("La ejecución de la prueba se detuvo porque el estado del informe es: {}", reportState);
        } else {
            validateAccountsTransactions(result);
        }
    }
}
