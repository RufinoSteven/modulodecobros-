package actions.ImportedFiles;

import actions.Batch.BatchActions;
import config.Browser;
import dataStorageModel.ACHData;
import dataStorageModel.AccountEntry;
import dataStorageModel.AmountEntry;
import dataStorageModel.RegionalAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Reports.ImportedFilesPages;
import utils.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static config.Browser.wait;
import static java.lang.Thread.sleep;
import static org.openqa.selenium.Keys.*;

public class ConsultImportedFilesActions {
    private final ImportedFilesPages importedFilesPages = new ImportedFilesPages();
    public static final Logger log = LogManager.getLogger(ConsultImportedFilesActions.class);
    ACHExtractDATA achExtractDATA = ACHExtractDATA.getInstance();
    private final BatchActions batchActions = new BatchActions();

    /**
     * Confirma si un archivo se ha importado correctamente verificando si el archivo está visible en la página.
     * El método intenta comprobar la visibilidad del archivo hasta 5 veces, refrescando la página y esperando
     * 5 segundos entre intentos.
     *
     * @return Devuelve verdadero si el archivo se importa correctamente y es visible después de los intentos
     * @throws InterruptedException si el hilo es interrumpido mientras espera
     */
    public boolean confirmFileImport() throws InterruptedException {
        String achFileName = getAchFileName();
        boolean fileImportedCorrectly = false;
        int attempts = 0;
        while (attempts < 5) {
            try {
                if (importedFilesPages.getFileName(achFileName).isDisplayed()) {
                    fileImportedCorrectly = true;
                    log.info("Archivo visible después de {} intentos.", attempts + 1);
                    break;
                }
            } catch (Exception e) {
                log.error("Intento {} fallido. Reintentando...", attempts + 1);
            }
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnRenew)).click();
            sleep(5000);
            attempts++;
        }

        if (!fileImportedCorrectly) {
            log.error("El archivo no se importó correctamente después de {} intentos.", attempts);
            System.exit(1);
        }
        return fileImportedCorrectly;
    }

    /**
     * Lee el estado de los archivos importados y realiza validaciones basadas en el estado esperado de los archivos.
     * Comprueba el estado de cada archivo y lo registra, verificando que el estado coincida con los valores esperados.
     * El método devuelve el estado del último archivo procesado.
     *
     * @param typeProcess          El tipo de proceso a ejecutar.
     * @param expectedStatusImport El estado esperado contra el que se debe comprobar. Puede ser:
     *                             - "Valido" para comprobar los estados de importación válidos.
     *                             - "Rechazado" para comprobar los estados de importación no válidos.
     *                             - "Duplicado" para comprobar específicamente el estado "Duplicada".
     * @return El estado del último archivo importado después de comprobar todos los archivos.
     */
    public String readStateImportedFiles(String typeProcess, String expectedStatusImport) {
        String achFileName = getAchFileName();
        int countOfElements;
        if (expectedStatusImport.equalsIgnoreCase("Duplicado") || expectedStatusImport.equalsIgnoreCase("Rechazado")) {
            countOfElements = 1;
        } else {
            countOfElements = importedFilesPages.numberElements(achFileName);
        }
        importedFilesPages.consultTotalNumberImportedFiles(achFileName, countOfElements);
        importedFilesPages.pressEnter();

        String reportState = "";
        String[] validExpectedStates;
        String[] invalidExpectedStates;
        if (typeProcess.equalsIgnoreCase("Tesoreria")) {
            // Valores esperados para los diferentes estados del archivo importado
            validExpectedStates = new String[]{"Exportado", "Validada", "Procesadas", "Listo para exportar"};
            invalidExpectedStates = new String[]{"Cancelada", "Importación fracasada", "Exportación fracasó"};
        } else {
            validExpectedStates = new String[]{"Exportado", "Validada", "Listo para exportar"};
            invalidExpectedStates = new String[]{"Cancelada", "Procesadas", "Importación fracasada", "Exportación fracasó"};
        }
        // Iterar a través de todos los archivos importados
        for (int i = 0; i < countOfElements; i++) {
            // Obtener el estado actual del archivo y eliminar cualquier espacio en blanco sobrante
            String currentState = importedFilesPages.stateOfReport().trim();
            reportState = currentState;
            // Validar el estado en la última iteración
            if (i == countOfElements - 1) {
                if (expectedStatusImport.equalsIgnoreCase("Valido")) {
                    Assertions.assertTrue(
                            Arrays.asList(validExpectedStates).contains(currentState),
                            "La importación del archivo falló, se esperaba el estado: " + expectedStatusImport + " y se obtuvo el estado: " + currentState
                    );
                } else if (expectedStatusImport.equalsIgnoreCase("Rechazado")) {
                    Assertions.assertTrue(
                            Arrays.asList(invalidExpectedStates).contains(currentState),
                            "La importación del archivo falló, se esperaba el estado: " + expectedStatusImport + " y se obtuvo el estado: " + currentState
                    );
                } else if (expectedStatusImport.equalsIgnoreCase("Duplicado")) {
                    Assertions.assertEquals("Duplicada", currentState,
                            "La importación del archivo falló, se esperaba el estado: " + expectedStatusImport + " y se obtuvo el estado: " + currentState);
                }
            }
            // Navegar a la página siguiente para comprobar el siguiente archivo
            importedFilesPages.pressEnter();
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnPageUp)).click();
            importedFilesPages.pressEnter();
        }
        // Cerrar la operación y devolver el último estado.
        importedFilesPages.btnCancel.click();
        return reportState;
    }

    public String readStateImportedFilesAffiliatePayment(String expectedStatusImport, String typeProcess) {
        String reportState = "";
        // Valores esperados para los diferentes estados del archivo importado
        String[] validExpectedStates = {"Exportado", "Procesadas", "Validada", "Listo para exportar"};
        String[] invalidExpectedStates = {"Cancelada", "Importación fracasada"};
        int countOfElements = 0;
        boolean allFound = true;
        String[] reportsName;
        if (expectedStatusImport.equalsIgnoreCase("Valido")) {
            if (typeProcess.equalsIgnoreCase("Afiliados")) {
                reportsName = new String[]{"ACH_OK", "ACH_R", "ACH005R3F", "ACH005R4F", "ACH005PF3"};
            } else {
                reportsName = new String[]{"CTA140PF03.TXT", "CTA140PF07.TXT", "CTA140PF10.TXT", "CTA140PF11.TXT", "CTA140PF12.TXT"};
            }
            for (String report : reportsName) {
                try {
                    String reportText = String.valueOf(importedFilesPages.getFileName(report));
                    log.info("Found report Text: {}", reportText);
                } catch (NoSuchElementException e) {
                    log.error("Report not found: {}", report);
                    allFound = false;
                }
            }
            countOfElements = 6;
            importedFilesPages.selectFirst6Report();
            importedFilesPages.pressEnter();
        } else if (expectedStatusImport.equalsIgnoreCase("Duplicado")) {
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputOption1)).sendKeys("5");
            importedFilesPages.pressEnter();
            countOfElements = 1;
        }

        if (allFound) {
            // Iterar a través de todos los archivos importados
            for (int i = 0; i < countOfElements; i++) {
                // Obtener el estado actual del archivo y eliminar cualquier espacio en blanco sobrante
                String currentState = importedFilesPages.stateOfReport().trim();
                reportState = currentState;
                log.info("El archivo tiene el estado: {}", reportState);

                // Validar el estado en la última iteración
                if (i == countOfElements - 1) {
                    if (expectedStatusImport.equalsIgnoreCase("Valido")) {
                        Assertions.assertTrue(
                                Arrays.asList(validExpectedStates).contains(currentState),
                                "La importación del archivo falló, se esperaba el estado: " + expectedStatusImport + " y se obtuvo el estado: " + currentState
                        );
                    } else if (expectedStatusImport.equalsIgnoreCase("Duplicado")) {
                        Assertions.assertTrue(
                                Arrays.asList(invalidExpectedStates).contains(currentState),
                                "La importación del archivo falló, se esperaba el estado: " + expectedStatusImport + " y se obtuvo el estado: " + currentState
                        );
                    }
                }

                // Navegar a la página siguiente para comprobar el siguiente archivo
                importedFilesPages.pressEnter();
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnPageUp)).click();
                importedFilesPages.pressEnter();
            }
        } else {
            log.error("No se generaron todos los reportes a partir de los archivos de pago de afiliados importados");
        }
        // Cerrar la operación y devolver el último estado
        importedFilesPages.btnCancel.click();
        return reportState;
    }


    /**
     * Accede a la transacción introduciendo un código de transacción específico en el campo de entrada.
     *
     * @param transaction El código de la transacción a la que se quiere acceder.
     */
    public void accessTransactions(String transaction) {
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputTransactions)).sendKeys(transaction);
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputTransactions)).sendKeys(ENTER);
    }


    /**
     * Accede a los archivos de la cola de trabajos (spooled files) introduciendo los comandos y el nombre del proceso correspondientes.
     *
     * @param process El nombre o identificador del proceso utilizado para buscar los archivos en cola.
     */
    public void accessWorkSpooledFiles(String process) {
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputParametersOrCommand)).sendKeys("wrksplf");
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputParametersOrCommand)).sendKeys(F4);
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputSpooledFileUser)).clear();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputSpooledFileUser)).sendKeys("*ALL");
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputSpooledFile)).clear();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputSpooledFile)).sendKeys(process + "*");
        importedFilesPages.pressEnter();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputOption1)).click();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputOption1)).sendKeys(F11);
        importedFilesPages.pressShiftAndF6();
    }


    /**
     * Recupera una lista de números de cuenta del objeto ACHData.
     *
     * @param dto El objeto ACHData que contiene una lista de objetos AccountEntry.
     *            Cada AccountEntry representa una cuenta de la que se quiere extraer el número de cuenta.
     * @return Una lista de números de cuenta (String) extraídos de los AccountEntries en el DTO.
     */
    public List<String> getAccountsFromDto(ACHData dto) {
        return dto.getAccounts().stream()
                .map(AccountEntry::getAccountNumber)
                .collect(Collectors.toList());
    }

    /**
     * Recupera el importe total de la transacción del objeto ACHData, formateado como una cadena con comas y dos decimales.
     *
     * @param dto El objeto ACHData que contiene los totales de la transacción.
     * @return El importe total de la transacción formateado como una cadena (por ejemplo, "1,234.56").
     */
    public String getFormattedTotalFromDto(ACHData dto) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        return decimalFormat.format(dto.getTotal());
    }

    /**
     * Valida si el importe total de la transacción del archivo es visible en el informe.
     *
     * @param dto El objeto ACHData que contiene el importe total de la transacción.
     */
    public void validateTotalFromReport(ACHData dto) {
        String formattedTotal = getFormattedTotalFromDto(dto);

        try {
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.getAmountElement(formattedTotal)));
            log.info("El monto de la transacción: {} es visible en el reporte", formattedTotal);
        } catch (TimeoutException | NoSuchElementException e) {
            log.warn("El monto de la transacción: {} NO es visible en el reporte", formattedTotal);
        }
    }

    /**
     * Recupera una lista de cuentas regionales del objeto ACHData.
     *
     * @param dto El objeto ACHData que contiene una lista de objetos RegionalAccount.
     *            Cada RegionalAccount representa una cuenta regional de la que se quiere extraer el número de cuenta.
     * @return Una lista de cuentas regionales (String) extraídas de los RegionalAccount en el DTO.
     */
    public List<String> getRegionalAccountsFromDTO(ACHData dto) {
        return dto.getRegionalAccounts().stream()
                .map(RegionalAccount::getRegionalAccountNumbers)
                .collect(Collectors.toList());
    }

    /**
     * Recupera una lista de montos de transacciones del objeto ACHData.
     *
     * @param dto El objeto ACHData que contiene una lista de objetos AmountEntry.
     *            Cada AmountEntry representa una transacción con un monto asociado.
     * @return Una lista de montos de transacciones (Double) extraídos de los AmountEntries en el DTO.
     */
    public List<String> getAmountsFromDto(ACHData dto) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        return dto.getTransactionAmount().stream()
                .map(AmountEntry::getTransactionAmounts)
                .map(decimalFormat::format)
                .collect(Collectors.toList());
    }


    /**
     * Valida si las cuentas de un archivo son visibles en el informe.
     *
     * @param dto El objeto ACHData que contiene una lista de objetos AccountEntry.
     *            Cada AccountEntry representa una cuenta a validar.
     */
    public void validateAccountsFromReport(ACHData dto) {
        // Consultar Cuentas
        getAccountsFromDto(dto).forEach(accountNumber -> {
            try {
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.getAccountsElement(accountNumber)));
                log.info("La cuenta {} es visible en el reporte", accountNumber);
            } catch (TimeoutException | NoSuchElementException e) {
                log.warn("La cuenta {} NO es visible en el reporte. Continuando con la siguiente cuenta.", accountNumber);
            }
        });
    }

    /**
     * Valida si los montos de las transacciones de un archivo son visibles en el informe.
     *
     * @param dto El objeto ACHData que contiene una lista de objetos AmountEntry.
     *            Cada AmountEntry representa un monto de transacción a validar.
     */
    public void validateAmountsFromReport(ACHData dto) {
        // Consultar Montos de Transacción
        getAmountsFromDto(dto).forEach(formattedAmount -> {
            try {
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.getAmountElement(formattedAmount)));
                log.info("La transacción con monto: {} es visible en el reporte", formattedAmount);
            } catch (TimeoutException | NoSuchElementException e) {
                log.warn("La transacción con monto: {} NO es visible en el reporte. Continuando con el siguiente monto.", formattedAmount);
            }
        });
    }

    public void validateReportsReverseACH(ACHData result, String expectedImport) {
        String reportName;
        String[] rejectionMessages = {"Cuenta Invalida", "Monto de la transaccion invalido", "Lote rechazado completo, ver reporte rechazo",
                "Cuenta Maestra no tiene fondos disponibles", "Cuenta no Existe en el Mae"};
        if (expectedImport.equalsIgnoreCase("Valido")) {
            importedFilesPages.selectLastXReport(2);
            ExtentReportManager.captureScreenshot("Pantalla de la reportería CTA140 en el SpoolFile");
            importedFilesPages.pressEnter();
            reportName = importedFilesPages.nameOfReportReverseACH();
            for (int i = 0; i < 2; i++) {
                log.info("Report Name{}", reportName);
                if (reportName != null) {
                    switch (reportName.trim()) {
                        case "PD0816P1":
                            //validateAccountsFromReport(result);
                            //validateAmountsFromReport(result);
                            break;
                        case "PD0646P1":
                            validateAccountsFromReport(result);
                            validateAmountsFromReport(result);
                            break;
                    }
                }
                ExtentReportManager.captureScreenshot("Reporte: " + reportName);
                importedFilesPages.pressEnter();
            }
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
            validateAccountsTransactions(result, "ReversosACH", expectedImport);
        } else if (expectedImport.equalsIgnoreCase("Duplicado") || expectedImport.equalsIgnoreCase("Rechazado")) {
            importedFilesPages.selectLastXReport(1);
            importedFilesPages.pressEnter();
            reportName = importedFilesPages.nameOfDuplicateReportReverseACH();
            ExtentReportManager.captureScreenshot("Reporte: " + reportName);
            log.info("Report Name{}", reportName);
            if (reportName != null) {
                switch (reportName.trim()) {
                    case "CTA1011R":
                        if (expectedImport.equalsIgnoreCase("Duplicado")) {
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.noData));
                        } else if (expectedImport.equalsIgnoreCase("Rechazado")) {
                            validateRejectionMessages(rejectionMessages);
                        }
                        break;
                    case "CTA1011R0":
                        if (expectedImport.equalsIgnoreCase("Duplicado")) {
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.duplicateProcessCapitalLetter));
                        } else if (expectedImport.equalsIgnoreCase("Rechazado")) {
                            validateRejectionMessages(rejectionMessages);
                        }
                        break;
                }
            }
            importedFilesPages.pressEnter();
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
        }
    }


    /**
     * Valida el informe de pagos duplicados a afiliados usando los datos ACH proporcionados.
     *
     * @param result Datos ACH utilizados para la validación.
     */
    public void transactionDCReportValidations(ACHData result, String reportType) {
        String[] reportsName = new String[0];
        String[] rejectionMessages = {"Proceso Duplicado", "Cuenta Inactiv", "Cuenta Invalida", "Cuenta no Exist",
                "Monto de la transaccion invalido", "Lote rechazado completo, ver reporte rechazo", "Cuenta Maestra no tiene fondos disponibles"};
        String reportName = "";

        if (reportType.equalsIgnoreCase("Valido")) {
            reportName = importedFilesPages.nameOfTransDC();
            reportsName = new String[]{"CTA062R1", "CTA062R2", "CTA062R3", "CTA062R4"};
        } else if (reportType.equalsIgnoreCase("Duplicado")) {
            reportName = importedFilesPages.nameOfTransDC();
            reportsName = new String[]{"CTA062CLR0"};
        } else if (reportType.equalsIgnoreCase("Rechazado")) {
            reportsName = new String[]{"CTA062R1", "CTA062R2", "CTA062R3", "CTA062R4"};
            reportName = importedFilesPages.nameOfTransDC();
        }
        importedFilesPages.selectLastXReport(reportsName.length);
        importedFilesPages.pressEnter();
        if (reportType.equalsIgnoreCase("Valido")) {
            for (String s : reportsName) {
                log.info("Report Name{}", reportName);
                ExtentReportManager.captureScreenshot("Reporte: " + s);
                if (reportName != null || AssertionsAndChecks.Checks.isVisible(importedFilesPages.getFileName(s))) {
                    switch (Objects.requireNonNull(s).trim()) {
                        case "CTA062R1":
                            validateAccountsFromReport(result);
                            validateAmountsFromReport(result);
                        case "CTA062R2":
                        case "CTA062R3":
                            break;
                        case "CTA062R4":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.tdReportWithoutRecords));
                            break;
                        default:
                            log.warn("Nombre de reporte no reconocido: {}", s);
                            break; // No acción, pero evitamos fallos
                    }
                } else {
                    log.warn("Se encontró un nombre de reporte nulo.");
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                importedFilesPages.pressEnter(); // Siempre se ejecuta para avanzar
            }
        }
        if (reportType.equalsIgnoreCase("Rechazado") || reportType.equalsIgnoreCase("Duplicado")) {
            for (String s : reportsName) {
                log.info("Report Name{}", s);
                if (reportName != null || AssertionsAndChecks.Checks.isVisible(importedFilesPages.getFileName(s))) {
                    ExtentReportManager.captureScreenshot("Reporte: " + s);
                    switch (Objects.requireNonNull(s).trim()) {
                        case "CTA062R1":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.tdReportWithoutRecords));
                            break;
                        case "CTA062R2":
                        case "CTA062R3":
                            break;
                        case "CTA062R4":
                            validateAccountsFromReport(result);
                            validateAmountsFromReport(result);
                            validateRejectionMessages(rejectionMessages);
                            break;
                        case "CTA062CLR0":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.duplicateProcessCapitalLetter));
                            break;
                        default:
                            log.warn("Nombre de reporte no reconocido: {}", reportName);
                            break;
                    }
                } else {
                    log.warn("Se encontró un nombre de reporte nulo.");
                }
                importedFilesPages.pressEnter();
            }
        }
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
    }


    /**
     * Valida los informes de transacciones ACH utilizando los datos proporcionados.
     *
     * @param result     Datos ACH utilizados para la validación.
     * @param reportType El tipo de reporte a validar (por ejemplo, "Valido", "Rechazado").
     */
    public void transactionACHReportValidations(ACHData result, String reportType) {
        String[] reportsName = new String[0];
        String[] rejectionMessages = {"Proceso Duplicado", "Cuenta Invalida", "Cuenta no Exist",
                "Monto de la transaccion invalido", "Lote rechazado completo, ver reporte rechazo", "Cuenta Maestra no tiene fondos disponibles"};
        String reportName = "";

        if (reportType.equalsIgnoreCase("Valido")) {
            reportName = importedFilesPages.nameOfTransACH();
            reportsName = new String[]{"CTA140PT17", "CTA140PT16", "CTA140PT01", "CTA140PT"};
        } else if (reportType.equalsIgnoreCase("Duplicado")) {
            reportName = importedFilesPages.nameOfTransACH();
            reportsName = new String[]{"CTA140R0"};
        } else if (reportType.equalsIgnoreCase("Rechazado")) {
            reportsName = new String[]{"CTA140PT17", "CTA140PT16", "CTA140PT01", "CTA140PT02", "CTA140PT"};
            reportName = importedFilesPages.nameOfTransACH();
        }

        importedFilesPages.selectLastXReport(reportsName.length);
        importedFilesPages.pressEnter();
        if (reportType.equalsIgnoreCase("Valido")) {
            for (String s : reportsName) {
                log.info("Report Name{}", reportName);
                ExtentReportManager.captureScreenshot("Reporte: " + s);
                if (reportName != null || AssertionsAndChecks.Checks.isVisible(importedFilesPages.getFileName(s))) {
                    switch (Objects.requireNonNull(s).trim()) {
                        case "CTA140PT17":
                        case "CTA140PT16":
                            break;
                        case "CTA140PT01":
                            validateTotalFromReport(result);
                            break;
                        case "CTA140PT":
                            validateAccountsFromReport(result);
                            validateAmountsFromReport(result);
                            break;
                        default:
                            log.warn("Nombre de reporte no reconocido: {}", s);
                            break;
                    }
                } else {
                    log.warn("Se encontró un nombre de reporte nulo.");
                }
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                importedFilesPages.pressEnter();
            }
        }
        if (reportType.equalsIgnoreCase("Rechazado") || reportType.equalsIgnoreCase("Duplicado")) {
            for (String s : reportsName) {
                log.info("Report Name{}", s);
                if (reportName != null || AssertionsAndChecks.Checks.isVisible(importedFilesPages.getFileName(s))) {
                    ExtentReportManager.captureScreenshot("Reporte: " + s);
                    switch (Objects.requireNonNull(s).trim()) {
                        case "CTA140R0":
                            validateRejectionMessages(rejectionMessages);
                            break;
                        case "CTA140PT":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.tdReportWithoutRecordsAndWithoutAsterisk));
                            break;
                        case "CTA140PT17":
                        case "CTA140PT16":
                        case "CTA140PT01":
                            break;
                        case "CTA140PT02":
                            validateRejectionMessages(rejectionMessages);
                            break;
                        default:
                            log.warn("Nombre de reporte no reconocido: {}", reportName);
                            break;
                    }
                } else {
                    log.warn("Se encontró un nombre de reporte nulo.");
                }
                importedFilesPages.pressEnter();
            }
        }
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
    }


    /**
     * Realiza validaciones en los reportes de pagos a afiliados.
     *
     * @param result     Datos ACH que se utilizarán para las validaciones.
     * @param reportType Tipo de reporte a validar ("Valido", "Duplicado", "Rechazado").
     */
    public void affiliateReportValidations(ACHData result, String reportType) {
        String[] reportsName = new String[0];
        String[] rejectionMessages = {"Cuenta Invalida", "Monto de la transaccion invalido", "Lote rechazado completo, ver reporte rechazo",
                "Cuenta Maestra no tiene fondos disponibles"};
        String reportName = "";

        if (reportType.equalsIgnoreCase("Valido")) {
            reportName = importedFilesPages.nameOfReportAffiliatePayment();
            reportsName = new String[]{"ACH0005R0", "ACH0005PT0", "ACH0005R", "ACHP0005R0"};
        } else if (reportType.equalsIgnoreCase("Duplicado")) {
            reportName = importedFilesPages.nameOfDuplicateReportAffiliatePayment();
            reportsName = new String[]{"ACH005R1", "ACH005R2", "ACH005R4"};
        } else if (reportType.equalsIgnoreCase("Rechazado")) {
            reportsName = new String[]{"ACH0005R0", "ACH0005PT0", "ACH0005R", "ACHP0005R0"};
            reportName = importedFilesPages.nameOfDuplicateReportAffiliatePayment();
        }

        importedFilesPages.selectLastXReport(reportsName.length);
        importedFilesPages.pressEnter();
        if (reportType.equalsIgnoreCase("Valido") || reportType.equalsIgnoreCase("Duplicado")) {
            for (String s : reportsName) {
                log.info("Report Name{}", reportName);
                ExtentReportManager.captureScreenshot("Reporte: " + reportName);
                if (reportName != null || AssertionsAndChecks.Checks.isVisible(importedFilesPages.getFileName(s))) {
                    switch (Objects.requireNonNull(reportName).trim()) {
                        case "ACH0005R":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.unfundedAccount));
                            validateAmountsFromReport(result);
                            break;
                        case "ACH0005PT0":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.unfundedAccount));
                            break;
                        case "ACH0005R0":
                        case "ACHP0005R0":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.duplicateProcess));
                            break;
                        case "ACH005R1":
                            validateAccountsFromReport(result);
                            validateAmountsFromReport(result);
                            break;
                        case "ACH005R2", "ACH005R4":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.tdReportWithoutRecords));
                            break;
                        case "ACH005R3":
                            break;
                        default:
                            log.warn("Nombre de reporte no reconocido: {}", reportName);
                            break; // No acción, pero evitamos fallos
                    }
                } else {
                    log.warn("Se encontró un nombre de reporte nulo.");
                }
                importedFilesPages.pressEnter();
            }
        }
        if (reportType.equalsIgnoreCase("Rechazado")) {
            for (int i = 0; i < reportsName.length; i++) {
                log.info("Report Name{}", reportName);
                ExtentReportManager.captureScreenshot("Reporte: " + reportName);
                if (reportName != null) {
                    switch (reportName.trim()) {
                        case "ACH0005R":
                            validateAmountsFromReport(result);
                            validateRejectionMessages(rejectionMessages);
                            break;
                        case "ACH0005PT0":
                            validateRejectionMessages(rejectionMessages);
                            break;
                        case "ACH0005R0":
                        case "ACHP0005R0":
                            break;
                        default:
                            log.warn("Nombre de reporte no reconocido: {}", reportName);
                            break;
                    }
                } else {
                    log.warn("Se encontró un nombre de reporte nulo.");
                }
                importedFilesPages.pressEnter();
            }
        }
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
    }

    /**
     * Valida el informe de domiciliación duplicado utilizando los datos ACH proporcionados.
     *
     * @param result     Datos ACH utilizados para la validación.
     * @param reportType Indica si el tipo de informe es válido o rechazado.
     */
    public void domiciliationReportValidations(ACHData result, String reportType) {
        String[] reportsName = new String[1];
        String[] rejectionMessages = {"Proceso Duplicado", "Cuenta Invalida", "Monto de la transaccion invalido", "Lote rechazado completo, ver reporte rechazo",
                "Cuenta Maestra no tiene fondos disponibles"};
        String reportName = "";

        if (reportType.equalsIgnoreCase("Valido")) {
            reportName = importedFilesPages.nameOfReportDomiciliation();
            reportsName = new String[]{"CTA710R1", "CTA710R2", "CTA710R3", "CTA710R4"};
        } else if (reportType.equalsIgnoreCase("Duplicado")) {
            reportName = importedFilesPages.nameOfReportDomiciliation();
            reportsName = new String[]{"CTA710R0"};
        } else if (reportType.equalsIgnoreCase("Rechazado")) {
            reportsName = new String[]{"CTA710R0"};
            reportName = importedFilesPages.nameOfReportDomiciliation();
        }

        importedFilesPages.selectLastXReport(reportsName.length);
        importedFilesPages.pressEnter();
        if (reportType.equalsIgnoreCase("Valido") || reportType.equalsIgnoreCase("Duplicado")) {
            for (String report : reportsName) {
                String currentReportName = importedFilesPages.nameOfReportDomiciliation();
                log.info("Report Name: {}", currentReportName);
                ExtentReportManager.captureScreenshot("Reporte: " + currentReportName);
                if (currentReportName != null && AssertionsAndChecks.Checks.isVisible(importedFilesPages.getFileName(report))) {
                    switch (currentReportName.trim()) {
                        case "CTA710R1":
                            try {
                                validateAccountsFromReport(result);
                                validateAmountsFromReport(result);
                            } catch (Exception e) {
                                log.error("Error while validating report {}: {}", currentReportName, e.getMessage(), e);
                            }
                            break;
                        case "CTA710R4":
                        case "CTA710R2":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.tdReportWithoutRecords));
                            break;
                        case "CTA710R0":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.duplicateProcessCapitalLetter));
                            break;
                        case "CTA710R3":
                            break;
                        default:
                            log.warn("Unrecognized report name: {}", currentReportName);
                            break;
                    }
                } else {
                    log.warn("Null or invisible report name encountered for {}", report);
                }

                importedFilesPages.pressEnter();
            }
        }
        log.info("Tipo de reporte recibido: '{}'", reportType);
        if (reportType.equalsIgnoreCase("Rechazado")) {
            for (String ignored : reportsName) {
                String currentReportName = importedFilesPages.nameOfReportDomiciliation();
                log.info("Report Name: {}", currentReportName);
                ExtentReportManager.captureScreenshot("Reporte: " + reportName);
                if (currentReportName != null) {
                    if (currentReportName.trim().equals("CTA710R0")) {
                        validateRejectionMessages(rejectionMessages);
                    } else {
                        log.warn("Unrecognized report name: {}", currentReportName);
                    }
                } else {
                    log.warn("Se encontró un nombre de reporte nulo.");
                }

                importedFilesPages.pressEnter();
            }
        }
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
    }

    /**
     * Verifica si al menos uno de los reportes está presente en la interfaz de usuario.
     *
     * @param rejectionMessages Arreglo de nombres de mensajes a buscar.
     */
    private void validateRejectionMessages(String[] rejectionMessages) {
        for (String message : rejectionMessages) {
            try {
                importedFilesPages.getRejectionMessage(message);
                log.info("Reporte encontrado: " + message);
                return;
            } catch (Exception e) {
                System.err.println("Error al verificar el reporte: " + message + " - " + e.getMessage());
            }
        }
    }

    /**
     * Valida los reportes basándose en el resultado ACHData proporcionado.
     * El metodo revisa el nombre del reporte y realiza acciones de validación específicas según el tipo de reporte.
     *
     * @param result El objeto ACHData que contiene los detalles de cuenta y transacciones para la validación.
     */
    public void validateReports(ACHData result, String reportType) {
        String[] rejectionMessages = {"Proceso Duplicado", "Cuenta Invalida", "Monto de la transaccion invalido", "Lote rechazado completo, ver reporte rechazo",
                "Cuenta Maestra no tiene fondos disponibles"};
        if (reportType.equalsIgnoreCase("Valido")) {
            importedFilesPages.selectLast5Report();
            importedFilesPages.pressEnter();
            for (int i = 0; i < 5; i++) {
                String reportName = importedFilesPages.nameOfReport();
                log.info("Nombre de Reporte: {}", reportName);
                ExtentReportManager.captureScreenshot("Reporte: " + reportName);
                if (reportName != null) {
                    switch (reportName.trim()) {
                        case "CTA038R1":
                            validateAccountsFromReport(result);
                            validateAmountsFromReport(result);
                            break;
                        case "CTA038R2":
                        case "CTA038R3":
                        case "CTA038R5":
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.tdReportWithoutRecords));
                            break;
                        case "CTA038R4":
                            validateAmountsFromReport(result);
                            String firstValue = result.getAccountsDebited().getFirst();
                            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.getAccountsElement(String.valueOf(firstValue))));
                            break;
                    }
                }
                importedFilesPages.pressEnter();
            }
        } else if (reportType.equalsIgnoreCase("Rechazado")) {
            importedFilesPages.selectLastXReport(1);
            importedFilesPages.pressEnter();
            for (int i = 0; i < 1; i++) {
                String reportName = importedFilesPages.nameOfReport();
                log.info("Report Name: {}", reportName);
                ExtentReportManager.captureScreenshot("Reporte: " + reportName);
                if (reportName != null) {
                    if (reportName.trim().equals("CTA038R0")) {
                        validateRejectionMessages(rejectionMessages);
                    } else {
                        log.warn("Nombre de reporte no reconocido: {}", reportName);
                    }
                } else {
                    log.warn("Se encontró un nombre de reporte nulo.");
                }

                importedFilesPages.pressEnter();
            }
        }
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
    }

    /**
     * Selecciona el tipo de cuenta cuando la cuenta no se encuentra.
     */
    public void selectAccountType(String expectedStatusImport, String accountNumber) {
        try {
            if (importedFilesPages.tdAccountNotFound.isDisplayed()) {
                wait.until(ExpectedConditions.visibilityOf(
                        importedFilesPages.btnCurrentAccount.isDisplayed() ? importedFilesPages.btnCurrentAccount : importedFilesPages.btnSavingsAccount
                )).click();
                importedFilesPages.pressEnter();
                if (expectedStatusImport.equalsIgnoreCase("Rechazado") && importedFilesPages.tdAccountNotFound.isDisplayed()) {
                    ExtentReportManager.captureScreenshot("La cuenta " + accountNumber + " no existe");
                }
            }
        } catch (Exception e) {
            log.info("Elemento tdAccountNotFound no encontrado");
        }
    }

    /**
     * Autoriza la visualización de cuentas restringidas
     */
    public void seeRestrictedAccount() {
        try {
            if (importedFilesPages.tdRestrictedAccount.isDisplayed()) {
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnSeeRestrictedAccount)).click();
            }
        } catch (Exception e) {
            log.info("Elemento tdRestrictedAccount no encontrado");
        }
    }

    /**
     * Gestiona la selección del tipo de cuenta durante el proceso de cambio de estado
     * y proporciona un flujo alternativo si la cuenta no se encuentra inicialmente.
     *
     * @param accountNumber El número de cuenta para el cual debe manejarse la selección del tipo.
     */
    public void selectAccountTypeChangeStatus(String accountNumber) {
        if (importedFilesPages.inoutAccountNotFound.isDisplayed()) {
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnOk)).click();
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccountChangeStatus)).clear();
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel)).click();
            accessTransactions("202035");
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccountChangeStatus)).clear();
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccountChangeStatus)).sendKeys(accountNumber);
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputOption1)).sendKeys("1");
            importedFilesPages.pressEnter();
        }
    }


    /**
     * Valida las transacciones de cuentas en un archivo ACH, incluyendo tanto los créditos a cuentas de ahorro
     * como los débitos desde una cuenta específica.
     * Este metodo recorre las transacciones y asegura que los montos correspondientes
     * se muestren correctamente en el sistema para cada cuenta.
     *
     * @param dto El objeto ACHData que contiene las cuentas y los montos que deben ser validados.
     */
    public void validateAccountsTransactions(ACHData dto, String typeProccess, String expectedStatusImport) {
        List<String> accountNumbers = getAccountsFromDto(dto).stream()
                .map(s -> s.trim().toLowerCase())
                .toList();
        List<String> amounts = getAmountsFromDto(dto);
        Set<String> debitedAccounts = getDebitedAccountsFromDto(dto).stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        for (int i = 0; i < accountNumbers.size(); i++) {
            String accountNumber = accountNumbers.get(i);
            String amount = amounts.get(i);

            if (!debitedAccounts.contains(accountNumber)) {
                validateAccountTransaction(accountNumber, amount, expectedStatusImport);
            }
        }

        if (typeProccess.equalsIgnoreCase("Tesoreria") ||
                typeProccess.equalsIgnoreCase("Afiliados") ||
                typeProccess.equalsIgnoreCase("Domiciliacion")) {

            for (int i = 0; i < accountNumbers.size(); i++) {
                String accountNumber = accountNumbers.get(i);
                String amount = amounts.get(i);

                if (debitedAccounts.contains(accountNumber)) {
                    validateAccountTransaction(accountNumber, "-" + amount, expectedStatusImport);
                }
            }
        }
    }

    /**
     * Obtiene una lista de cuentas debitadas desde el DTO,
     * sin importar si viene como lista, string o cualquier otro objeto.
     *
     * @param dto Objeto que contiene las cuentas debitadas.
     * @return Lista de cuentas debitadas como strings.
     */
    private List<String> getDebitedAccountsFromDto(ACHData dto) {
        List<?> debited = dto.getAccountsDebited();
        if (debited == null) {
            return Collections.emptyList();
        }
        return debited.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }


    /**
     * Valida la presencia de una transacción con un monto específico para una cuenta dada.
     * <p>
     * Este metodo navega a la función de consulta de transacciones de cuenta (código 201030),
     * ingresa el número de cuenta, selecciona el tipo de cuenta y verifica si una transacción
     * con el monto especificado se muestra en el sistema. Si no se encuentra la transacción,
     * se imprime un mensaje de error en la consola.
     *
     * @param accountNumber   El número de cuenta en el que se debe buscar la transacción.
     * @param formattedAmount El monto esperado de la transacción, formateado como cadena.
     */
    public void validateAccountTransaction(String accountNumber, String formattedAmount, String expectedStatusImport) {
        if (expectedStatusImport.equalsIgnoreCase("cuentas inactivas")) {
            accessTransactions("202030");
        } else {
            accessTransactions("201030");
        }
        importedFilesPages.pressEnter();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccount)).clear();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccount)).sendKeys(accountNumber);
        importedFilesPages.pressEnter();
        selectAccountType(expectedStatusImport, accountNumber);
        if (!expectedStatusImport.equalsIgnoreCase("Rechazado") && !expectedStatusImport.equalsIgnoreCase("Consulta")) {
            seeRestrictedAccount();
            importedFilesPages.selectOptionByValue("04");
            boolean amountFound = false;
            while (true) {
                try {
                    wait.until(ExpectedConditions.visibilityOf(importedFilesPages.getAmountElement(formattedAmount)));
                    ExtentReportManager.captureScreenshot("Se muestra la transacción " + formattedAmount + " de la cuenta " + accountNumber);
                    log.info("La transacción se muestra correctamente para el monto: {} en la cuenta: {}", formattedAmount, accountNumber);
                    amountFound = true;
                    break;
                } catch (TimeoutException e) {
                    try {
                        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnPageDown)).click();
                        Thread.sleep(1000);
                    } catch (TimeoutException ex) {
                        log.warn("No se encontró paginación adicional y el monto {} no fue encontrado para la cuenta {}", formattedAmount, accountNumber);
                        break;
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            selectAccountType(expectedStatusImport, accountNumber);
            seeRestrictedAccount();
            importedFilesPages.selectOptionByValue("04");

            while (true) {
                try {
                    wait.until(ExpectedConditions.visibilityOf(importedFilesPages.getAmountElement(formattedAmount)));
                    ExtentReportManager.captureScreenshot("Se muestra la transacción " + formattedAmount + " de la cuenta " + accountNumber);
                    log.info("La transacción se muestra correctamente para el monto: {} en la cuenta: {}", formattedAmount, accountNumber);
                    amountFound = true;
                    break;
                } catch (TimeoutException e) {
                    try {
                        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnPageDown)).click();
                        sleep(1000);
                    } catch (TimeoutException ex) {

                        log.warn("No se encontró paginación adicional y el monto {} no fue encontrado para la cuenta {}\"", formattedAmount, accountNumber);
                        break;
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            if (!amountFound) {
                ExtentReportManager.captureScreenshot("No se encontró la transacción " + formattedAmount + " de la cuenta " + accountNumber);
                log.error("No se encontró ninguna transacción para el monto: {} en la cuenta: {}", formattedAmount, accountNumber);
            }
        }
        if (expectedStatusImport.equalsIgnoreCase("Consulta")) {
            ExtentReportManager.captureScreenshot("Se consulta la nueva cuenta creada " + accountNumber);
        }
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnGoOut)).click();
    }

    /**
     * Consulta la cuenta contable navegando a través del flujo de transacciones,
     * ingresando valores predefinidos y desplazándose por las páginas de la cuenta
     * para validar la presencia de los montos de transacción esperados.
     *
     * @param dto El objeto ACHData que contiene los montos que deben ser validados.
     */
    public void consultAccountingAccount(ACHData dto) {
        try {
            // Inicializar y acceder a las transacciones
            accessTransactions("401050");

            // Ingresar la cuenta contable y tomar captura de pantalla
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccountingAccount))
                    .sendKeys("1770110401");
            ExtentReportManager.captureScreenshot("Se validarán los registros en la cuenta contable 1770110401");
            importedFilesPages.pressEnter();

            // Seleccionar opciones
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputOption1)).sendKeys("5");
            importedFilesPages.pressEnter();
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputOption10)).sendKeys("1");
            importedFilesPages.pressEnter();

            // Preparar los conjuntos de montos
            Set<String> pendingAmounts = dto.getTransactionAmount().stream()
                    .map(AmountEntry::getTransactionAmounts)
                    .map(amount -> String.format("%.2f", amount))
                    .collect(Collectors.toSet());
            Set<String> foundAmounts = new HashSet<>();

            // Navegar a la última página
            boolean canNavigate = true;
            while (canNavigate) {
                try {
                    if (importedFilesPages.btnPageDown.isDisplayed()) {
                        importedFilesPages.btnPageDown.click();
                        sleep(300);
                    } else {
                        canNavigate = false;
                    }
                } catch (Exception e) {
                    canNavigate = false;
                }
            }

            // Procesar las páginas de abajo hacia arriba
            canNavigate = true;
            while (canNavigate && !pendingAmounts.isEmpty()) {
                // Verificar los montos en la página actual
                Iterator<String> iterator = pendingAmounts.iterator();
                while (iterator.hasNext()) {
                    String amount = iterator.next();
                    try {
                        if (importedFilesPages.getAmountElement(amount).isDisplayed()) {
                            log.info("La transacción con monto: {} es visible en la cuenta contable", amount);
                            foundAmounts.add(amount);
                            iterator.remove();
                        }
                    } catch (Exception ignored) {
                        // Continuar buscando
                    }
                }

                // Intentar ir a la página anterior
                try {
                    if (importedFilesPages.btnPageUp.isDisplayed() && !pendingAmounts.isEmpty()) {
                        importedFilesPages.btnPageUp.click();
                        sleep(300);
                    } else {
                        canNavigate = false;
                    }
                } catch (Exception e) {
                    canNavigate = false;
                }
            }

            // Registrar resultados
            ExtentReportManager.captureScreenshot("Se visualizaron los montos " + foundAmounts + " en la cuenta contable");
            for (String amount : pendingAmounts) {
                log.warn("Transaction for amount: {} was NOT found in accounting account", amount);
            }

            // Limpiar
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel)).click();
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnGoOut)).click();

        } catch (Exception e) {
            log.error("Error en consultAccountingAccount: ", e);
            throw e;
        }
    }

    /**
     * Cambia el estado de una cuenta dada navegando a través del flujo de mantenimiento de cuentas.
     *
     * @param accountNumber    El número de la cuenta cuyo estado se va a cambiar.
     * @param accountCondition El código de condición que representa el nuevo estado (por ejemplo, "1" para activo, "5" para inactivo).
     */
    public void changeStatusAccountFlow(String accountNumber, String accountCondition) {
        accessTransactions("202030");
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccountChangeStatus)).clear();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccountChangeStatus)).sendKeys(accountNumber);
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputOption1)).sendKeys("1");
        importedFilesPages.pressEnter();
        selectAccountTypeChangeStatus(accountNumber);
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccountCondition)).clear();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputAccountCondition)).sendKeys(accountCondition);
        ExtentReportManager.captureScreenshot("Se realiza el cambio de status " + accountCondition + "a la cuenta : " + accountNumber);
        importedFilesPages.pressEnter();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel)).click();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel)).click();
    }

    /**
     * Desactiva una lista de cuentas proporcionadas en el objeto ACHData.
     * <p>
     * Este método obtiene todos los números de cuenta relacionados del ACHData dado (dto)
     * y cambia su estado a "inactivo" estableciendo el código de estado en "5".
     *
     * @param dto El objeto ACHData que contiene los detalles de las cuentas a desactivar.
     */
    public void deactivateAccounts(ACHData dto) {
        List<String> accountNumbers = getAccountsFromDto(dto);
        for (String accountNumber : accountNumbers) {
            changeStatusAccountFlow(accountNumber, "5");
            log.info("La cuenta: {} ha sido inactivada", accountNumber);
        }
    }

    /**
     * Activa una lista de cuentas proporcionadas en el objeto ACHData.
     * <p>
     * Este metodo obtiene todos los números de cuenta relacionados del ACHData dado (dto)
     * y cambia su estado a "activo" estableciendo el código de estado en "1".
     *
     * @param dto El objeto ACHData que contiene los detalles de las cuentas a activar.
     */
    public void activateAccount(ACHData dto) {
        List<String> accountNumbers = getAccountsFromDto(dto);
        for (String accountNumber : accountNumbers) {
            changeStatusAccountFlow(accountNumber, "1");
            log.info("La cuenta: {} ha sido activada", accountNumber);
        }
    }

    /**
     * Valida el Informe de No Publicación (CTA038) para asegurar que no se hayan registrado transacciones en las cuentas.
     * <p>
     * Este método accede a los archivos spooled, abre el informe CTA038 y verifica si contiene algún registro de publicación en cuentas.
     * Si el informe contiene registros, la prueba fallará. De lo contrario, se registra un mensaje indicando que el informe no tiene publicaciones en cuentas.
     */
    public void validationNonPostingReport(String spoolFileTrans) {
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        importedFilesPages.pressShiftAndF2();
        accessWorkSpooledFiles(spoolFileTrans);
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputOption5)).sendKeys("5");
        importedFilesPages.pressEnter();
        String reportName = importedFilesPages.nameOfReport();
        if (importedFilesPages.tdReportWithoutRecords.isDisplayed()) {
            log.info("En el informe {} no se muestran registros de publicaciones en cuentas", reportName);
        } else {
            Assertions.fail("El informe " + reportName + " muestra publicaciones en cuentas, prueba fallida");
        }
        importedFilesPages.pressEnter();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
    }

    /**
     * Envía un archivo a un servidor remoto usando SFTP.
     * El método sube un archivo desde la ruta local a la ruta de carpeta remota especificada.
     *
     * @param remoteFolderPath La ruta en el servidor remoto donde se subirá el archivo.
     * @param localFilePath    La ruta del archivo en la máquina local que se va a subir.
     */
    public void sendFileToServer(String remoteFolderPath, String localFilePath) {
        boolean uploaded = SftpUploader.uploadFile(remoteFolderPath, localFilePath);
        log.info(uploaded ? "Archivo subido correctamente." : "Hubo un problema al subir el archivo.");
    }

    /**
     * Gestiona el proceso de transacciones ACH.
     * Este método procesa el archivo ACH validando y verificando los datos de transacción extraídos.
     * Realiza múltiples pasos incluyendo la validación de la importación del archivo,
     * la validación de reportes, la validación de cuentas y la validación de transacciones.
     *
     * @throws IOException Si ocurre un problema al leer el archivo ACH.
     */
    public void achTransactionManagement(String typeProcess, String expectedStatusImport,
                                         String spoolFileTransaction) throws IOException, InterruptedException {
        String localFilePath = ModifyFileAch.getGeneratedACHFilePath(typeProcess, expectedStatusImport);
        sleep(30000);
        ACHData result = achExtractDATA.extractACHInformation(localFilePath, typeProcess);
        boolean isInactiveAccounts = expectedStatusImport.equalsIgnoreCase("Cuentas inactivas");
        if (!isInactiveAccounts) {
            batchActions.selectEnvironment("QA");
        }
        accessTransactions("240070");
        ExtentReportManager.captureScreenshot("Se visualiza que el archivo " + ModifyFileAch.getGeneratedACHFileName() + " se ha importado en la transacción 240070");
        if (confirmFileImport()) {
            if (isInactiveAccounts) {
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel)).click();
                validationNonPostingReport(spoolFileTransaction);
            } else if (typeProcess.equalsIgnoreCase("Afiliados") && expectedStatusImport.equalsIgnoreCase("Rechazado")) {
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel)).click();
                proceedWithValidationFlow(result, spoolFileTransaction, typeProcess, expectedStatusImport);
            } else {
                executeImportedFilesFlow(result, expectedStatusImport, spoolFileTransaction, typeProcess);
            }
        } else {
            log.error("El archivo no se importó correctamente en Signature");
        }
    }

    /**
     * Gestiona el proceso de transacciones Canceladas.
     * Este método procesa el archivo de Débitos y Créditos validando y verificando los datos de transacción extraídos.
     * Realiza múltiples pasos incluyendo la validación de la importación del archivo,
     * la validación de reportes, la validación de cuentas y la validación de transacciones.
     *
     */
    public void dcCancelledTransaction(String transaction) {
        batchActions.selectEnvironment("QA");
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel));
        accessTransactions(transaction);
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel));
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputFirstReport));
        importedFilesPages.selectFirstReport();
        AssertionsAndChecks.Checks.isVisible(importedFilesPages.cancelledText);
        log.error("El archivo no se importó correctamente en Signature");
        ExtentReportManager.captureScreenshot("Se muestran los datos de la carga del archivo");
    }

    /**
     * Valida los archivos importados según su estado de reporte y tipo de procesamiento.
     *
     * @param result               El ACHData a validar.
     * @param expectedStatusImport El estado esperado con el cual se realizará la verificación.
     * @param spoolFileTransaction Nombre del archivo spool para la validación de transacciones.
     * @param typeProcess          Tipo de procesamiento (por ejemplo, "Tesorería", "Afiliados").
     */
    private void executeImportedFilesFlow(
            ACHData result,
            String expectedStatusImport,
            String spoolFileTransaction,
            String typeProcess) {
        String reportState = getReportState(expectedStatusImport, typeProcess);
        if ("Tesoreria".equalsIgnoreCase(typeProcess) && isFailedState(reportState)) {
            log.info("Prueba detenida debido al estado del reporte: {}", reportState);
            return;
        }
        proceedWithValidationFlow(result, spoolFileTransaction, typeProcess, expectedStatusImport);
    }

    /**
     * Obtiene el estado del reporte según el tipo de proceso.
     * Se delega al método correspondiente para leer el estado dependiendo de si
     * el proceso es "Tesorería" o "Afiliados".
     *
     * @param expectedStatusImport El estado esperado del proceso de importación.
     * @param typeProcess          El tipo de proceso ("Tesorería" o "Afiliados").
     * @return El estado actual del reporte importado.
     */
    private String getReportState(String expectedStatusImport, String typeProcess) {
        if ("Tesoreria".equalsIgnoreCase(typeProcess) || "Domiciliacion".equalsIgnoreCase(typeProcess) || "DC".equalsIgnoreCase(typeProcess)) {
            return readStateImportedFiles(typeProcess, expectedStatusImport);
        } else if ("Afiliados".equalsIgnoreCase(typeProcess) || "TransACH".equalsIgnoreCase(typeProcess)) {
            return readStateImportedFilesAffiliatePayment(expectedStatusImport, typeProcess);
        } else {
            importedFilesPages.btnCancel.click();
            return null;
        }
    }


    /**
     * Determina si el estado del reporte representa una importación fallida o cancelada.
     *
     * @param state El estado del reporte.
     * @return true si el estado es "Cancelada" o "Importación fracasada", false en caso contrario.
     */
    private boolean isFailedState(String state) {
        return "Cancelada".equalsIgnoreCase(state) || "Importación fracasada".equalsIgnoreCase(state);
    }

    /**
     * Continúa el proceso de validación de un reporte importado una vez que su estado ha sido validado.
     * <p>
     * Este método abre la interfaz de archivos spooled, selecciona el número apropiado de entradas del reporte
     * según el tipo de proceso, realiza validaciones sobre el contenido del reporte y verifica
     * las transacciones de cuenta y contables.
     *
     * @param result               El objeto ACHData que contiene los datos para la validación.
     * @param spoolFileTransaction El nombre del archivo spool al que se accederá.
     * @param typeProcess          El tipo de proceso, que determina la ruta de validación ("Tesorería" o "Afiliados").
     */
    private void proceedWithValidationFlow(ACHData result, String spoolFileTransaction, String typeProcess, String expectedStatusImport) {
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        importedFilesPages.pressShiftAndF2();
        accessWorkSpooledFiles(spoolFileTransaction);
        if ("Tesoreria".equalsIgnoreCase(typeProcess)) {
            validateReports(result, expectedStatusImport);
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
            if (expectedStatusImport.equalsIgnoreCase("Valido")) {
                validateAccountsTransactions(result, typeProcess, expectedStatusImport);
            }
        } else if ("Afiliados".equalsIgnoreCase(typeProcess)) {
            affiliateReportValidations(result, expectedStatusImport);
        } else if ("ReversosACH".equalsIgnoreCase(typeProcess)) {
            validateReportsReverseACH(result, expectedStatusImport);
        } else if ("Domiciliacion".equalsIgnoreCase(typeProcess)) {
            domiciliationReportValidations(result, expectedStatusImport);
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
            if (expectedStatusImport.equalsIgnoreCase("Valido")) {
                validateAccountsTransactions(result, typeProcess, expectedStatusImport);
            }
        } else if ("TransACH".equalsIgnoreCase(typeProcess)) {
            transactionACHReportValidations(result, expectedStatusImport);
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
            if (expectedStatusImport.equalsIgnoreCase("Valido")) {
                validateAccountsTransactions(result, typeProcess, expectedStatusImport);
            } else if (expectedStatusImport.equalsIgnoreCase("Rechazado")) {
                validateAccountsTransactions(result, typeProcess, expectedStatusImport);
            }
        } else if ("DC".equalsIgnoreCase(typeProcess)) {
            transactionDCReportValidations(result, expectedStatusImport);
            wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnExit)).click();
            validateAccountsTransactions(result, typeProcess, expectedStatusImport);
        }
    }

    /**
     * Navega a la página de cuentas regionales en el navegador.
     */
    public void navigateToRegionalAccountPage() {
        String url = ExternalURLs.getRegionalAccountUrl();
        Browser.getWebDriver().get(url);
    }

    /**
     * Verifica las cuentas regionales usando los datos del DTO proporcionado.
     *
     * @param dto Datos ACH que contienen las cuentas regionales a verificar.
     */
    public void consultRegionalAccounts(ACHData dto) {
        if (importedFilesPages.tdConfirmRegionalAccountPage.isDisplayed()) {
            importedFilesPages.selectCountryByValue("DO");
            importedFilesPages.selectBankByValue("BRRDDOSDXXX");
            List<String> regionalAccounts = getRegionalAccountsFromDTO(dto);
            for (String regionalAccount : regionalAccounts) {
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputRegionalAccount)).clear();
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.inputRegionalAccount))
                        .sendKeys(regionalAccount);
                wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnVerify)).click();
                try {
                    if (importedFilesPages.alertValidAccount.isEnabled()) {
                        sleep(3000);
                        ExtentReportManager.captureScreenshot("Verificación de la Cuenta Regional: " + regionalAccount);
                        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnUnderstood)).click();
                    } else {
                        sleep(3000);
                        ExtentReportManager.captureScreenshot("La Cuenta Regional no es valida: " + regionalAccount);
                        System.err.println("La cuenta no es válida: " + regionalAccount);
                        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnUnderstood)).click();
                    }
                } catch (TimeoutException | NoSuchElementException e) {
                    System.err.println("No se detectó alerta para la cuenta: " + regionalAccount);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        } else {
            log.error("No se pudo acceder a la página de consulta de cuentas regionales.");
        }
    }

    public String getAchFileName() {
        return ModifyFileAch.getGeneratedACHFileName();
    }

}
