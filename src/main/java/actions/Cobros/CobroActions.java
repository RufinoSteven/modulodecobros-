package actions.Cobros;

import actions.Authentication.LoginActions;
import actions.Batch.BatchActions;
import actions.dbActions.CommissionActions;
import actions.dbActions.EntriesActions;
import dataStorageModel.AccounInfo;
import dataStorageModel.CommissionInfo;
import dataStorageModel.CommissionInfoAccount;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Cobros.ConsultAccountPage;
import utils.AccountConsultOption;
import utils.ExtentReportManager;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static config.Browser.wait;
import static utils.AccountConsultOption.BASIC_INFORMATION;
import static utils.AccountConsultOption.CURRENT_ACCOUNT_STATUS;

@Getter
public class CobroActions {
    private static final Logger log = LogManager.getLogger(CobroActions.class);
    private final EntriesActions entriesActions = new EntriesActions();
    private final CommissionActions commissionActions = new CommissionActions();
    private final BatchActions batchActions = new BatchActions();
    private final ConsultAccountPage consultAccountPage = new ConsultAccountPage();
    private final LoginActions loginActions = new LoginActions();

    // Variables para almacenar datos del cobro actual
    private String clientId;
    private String collectionId;
    private String collectionDescription;
    private String accountNumber;
    private String currencyCode;
    private String accountType;
    private String initialPendingAmount;
    private String pendingAmount;
    private String transactionCode;
    private String transactionType;
    private String amountComparison;
    private double actualAmountCollected;
    private String initialAmountCollected;
    private String originalAmountCollected;
    private String collectionOriginalDate;
    private double currentBalance;
    private String amountLoanBySignature;
    // Variables para almacenar criterios de búsqueda
    private int targetPendingItemType;
    private int targetStatusItem;

    /**
     * Obtiene un cliente y su cuenta asociada que cumple con criterios estrictos:
     * 1. La cuenta tiene EXACTAMENTE UN cobro total pendiente.
     * 2. Ese único cobro es el primero en la prioridad de la cuenta (implícito si solo hay uno).
     * 3. El tipo y el estado de ese único cobro coinciden con los parámetros dados.
     *
     * @param pendingItemType El TIPO de cobro que se busca (ej. 3 para comisión).
     * @param statusItem      El ESTADO del cobro que se busca (ej. 1 para pendiente).
     * @return verdadero si se encontró y validó un cliente/cuenta, falso en caso contrario.
     */
    public boolean getClientWithPendingCommissions(int pendingItemType, int statusItem) {
        batchActions.selectEnvironment("QA");
        this.targetPendingItemType = pendingItemType;
        this.targetStatusItem = statusItem;
        int maxCandidatesToFetch = 2200; // Máximo de clientes a buscar

        log.info("Iniciando búsqueda de cliente con tipo de cobro: {} y estado: {}.", pendingItemType, statusItem);

        List<Map<String, Object>> candidateList = entriesActions.getCandidateAccountsList(
                pendingItemType, statusItem, maxCandidatesToFetch);

        if (candidateList == null || candidateList.isEmpty()) {
            log.warn("El generador de candidatos no devolvió candidatos para el tipo: {} y estado: {}.", pendingItemType, statusItem);
            return false;
        }

        log.info("Se encontraron {} candidatos iniciales. Verificando condiciones...", candidateList.size());

        for (Map<String, Object> candidateData : candidateList) {
            String candidateAccountNumber = String.valueOf(candidateData.get("ACCOUNT_NBR"));

            log.debug("Procesando candidato: Cuenta {}", candidateAccountNumber);

            // Verificando si la cuenta tiene solo 1 cobro total
            int totalCharges = entriesActions.getTotalChargesInAccount(candidateAccountNumber);
            log.debug("  VERIF 1: La cuenta {} tiene {} cobro(s) en total.", candidateAccountNumber, totalCharges);

            if (totalCharges != 1) {
                log.debug("  VERIF 1 FALLIDA: Se esperaba 1 cobro total. Buscando otro cliente.");
                continue; // Buscar otro cliente
            }
            log.debug("  VERIF 1 EXITOSA: La cuenta tiene exactamente 1 cobro total.");

            // Verificación 2: Comprobar si el único cobro coincide con el estado y tipo esperados
            // Como totalCharges es 1, getActualFirstChargeDetailsForAccount nos dará ese único cobro
            Map<String, Object> singleChargeDetails = entriesActions.getActualFirstChargeDetailsForAccount(candidateAccountNumber);

            if (singleChargeDetails == null) {
                log.warn("  VERIF 2 FALLIDA: No se pudieron obtener los detalles del cobro para la cuenta {}. Omitiendo.", candidateAccountNumber);
                continue;
            }

            // Nombres de columna directos de la consulta
            Object tipCobObj = singleChargeDetails.get("C10TIPCOB"); // Tipo de Cobro
            Object estCobObj = singleChargeDetails.get("C10ESTCOB"); // Estado del Cobro
            Object numProdObj = singleChargeDetails.get("C10NUMPROD"); // ID del Cobro

            if (tipCobObj == null || estCobObj == null || numProdObj == null) {
                log.warn("  VERIF 2 FALLIDA: Los detalles del cobro (tipo, estado o ID) son nulos para la cuenta {}. Cobro: {}", candidateAccountNumber, singleChargeDetails);
                continue;
            }

            int actualChargeType = ((Number) tipCobObj).intValue();
            int actualChargeStatus = ((Number) estCobObj).intValue();
            String actualChargeNumProd = String.valueOf(numProdObj);

            log.debug("VERIF 2: Detalles del cobro único: ID '{}', Tipo {}, Estado {}. Buscando: Tipo {}, Estado {}",
                    actualChargeNumProd, actualChargeType, actualChargeStatus,
                    this.targetPendingItemType, this.targetStatusItem);

            if (actualChargeType == this.targetPendingItemType && actualChargeStatus == this.targetStatusItem) {
                log.info("VERIF 2 EXITOSA: El cobro único de la cuenta coincide con el tipo/estado objetivo.");

                // Establecer variables de instancia con datos del cobro validados
                this.collectionId = actualChargeNumProd;
                this.collectionDescription = singleChargeDetails.get("C10DESCRI") != null ? String.valueOf(singleChargeDetails.get("C10DESCRI")).trim() : "N/A";
                this.clientId = singleChargeDetails.get("IDENTIFICACION") != null ? String.valueOf(singleChargeDetails.get("IDENTIFICACION")).trim() : "N/A";
                this.accountNumber = candidateAccountNumber;
                this.pendingAmount = singleChargeDetails.get("C10VACOPE") != null ? String.valueOf(singleChargeDetails.get("C10VACOPE")) : "0.0";
                this.originalAmountCollected = singleChargeDetails.get("C10VACOOR") != null ? String.valueOf(singleChargeDetails.get("C10VACOOR")) : "N/A";
                this.initialAmountCollected = singleChargeDetails.get("C10VALCOB") != null ? String.valueOf(singleChargeDetails.get("C10VALCOB")) : "N/A";
                this.initialPendingAmount = this.pendingAmount;
                this.currencyCode = singleChargeDetails.get("CURRENCY_CODE") != null ? String.valueOf(singleChargeDetails.get("CURRENCY_CODE")) : "N/A";
                this.collectionOriginalDate = singleChargeDetails.get("C10FECORI") != null ? formatDate(String.valueOf(singleChargeDetails.get("C10FECORI"))) : "N/A";

                log.info("CLIENTE VALIDADO: ID Cliente: {}, Cuenta: {}, ID Cobro: {}, Monto Pendiente: {}",
                        this.clientId, this.accountNumber, this.collectionId, this.pendingAmount);
                return true; // Cobro Encontrado
            } else {
                log.debug("  VERIF 2 FALLIDA: El cobro único (Tipo {}, Estado {}) no coincide con el objetivo (Tipo {}, Estado {}). Omitiendo.",
                        actualChargeType, actualChargeStatus, this.targetPendingItemType, this.targetStatusItem);
            }
        }

        log.warn("Se agotaron los {} candidatos sin encontrar uno que cumpla todas las condiciones", candidateList.size());
        return false;
    }

    /**
     * Establece los parámetros de tipo de transacción y comparación de montos.
     */
    public void setTransactionParameters(String transType, String comparison) {
        this.transactionType = transType;
        this.amountComparison = comparison;
        log.info("Parámetros establecidos - Tipo: {}, Comparación: {}", transType, comparison);
    }

    /**
     * Verifica si la cuenta actual tiene saldo cero.
     */
    public boolean verifyAccountWithoutBalance() {
        AccounInfo accountInfo = commissionActions.getAccountBalanceAndTypeAccount(accountNumber);
        if (accountInfo != null) {
            accountType = accountInfo.getAccountType();
            try {
                double balanceDB = Double.parseDouble(accountInfo.getAccountCurrent());
                this.currentBalance = balanceDB;
                log.info("Cuenta: {}, Tipo: {}, Saldo actual: {}", accountNumber, accountType, accountInfo.getAccountCurrent());

                navigateConsultAccount(getAccountType(), BASIC_INFORMATION);
                String rawText = wait.until(ExpectedConditions.visibilityOf(consultAccountPage.accountActualBalanced)).getText().trim();
                double balanceUI = Double.parseDouble(rawText.replaceAll("[^0-9.]", "")) * (rawText.contains("-") ? -1 : 1);
                ExtentReportManager.captureScreenshot("Se muestra el balance actual de la cuenta");

                loginActions.navigateToMainPage();
                return balanceDB <= 0 && balanceUI <= 0;
            } catch (NumberFormatException e) {
                log.error("Error al parsear el saldo - BD: '{}', UI: '{}'",
                        accountInfo.getAccountCurrent(),
                        consultAccountPage.accountActualBalanced.getText(), e);
                return false;
            } catch (Exception e) {
                log.error("Error al obtener el saldo de la UI para la cuenta {}: {}", accountNumber, e.getMessage(), e);
                return false;
            }
        }
        log.warn("No se pudo obtener la información del saldo para la cuenta {}", accountNumber);
        return false;
    }

    /**
     * Determina el tipo de cuenta (Ahorros o Corriente).
     */
    public String getAccountType() {
        return "1".equals(accountType) ? "Ahorros" : "Corriente";
    }

    /**
     * Determina el código de transacción apropiado según el tipo de cuenta.
     */
    public boolean determineTransactionCode() {
        String accountType = getAccountType();
        transactionCode = "Ahorros".equals(accountType) ? "021N" : "0011";
        log.info("Tipo de cuenta: {}, Código de transacción determinado: {}", accountType, transactionCode);
        return transactionCode != null && !transactionCode.isEmpty();
    }

    /**
     * Realiza una transacción en la cuenta considerando si el monto será mayor o menor
     * y si el tipo de transacción es crédito o débito.
     */
    public boolean performAccountTransaction() {
        try {
            determineTransactionCode();
            double pendingAmount = Double.parseDouble(this.pendingAmount);
            double transactionAmount;
            double additionalAmount;

            if ("mayor".equalsIgnoreCase(amountComparison)) {
                additionalAmount = 10.0 + (currentBalance < 0 ? Math.abs(currentBalance) : 0);
                transactionAmount = pendingAmount + additionalAmount;
                this.actualAmountCollected = pendingAmount;
            } else if ("menor".equalsIgnoreCase(amountComparison)) {
                additionalAmount = (currentBalance < 0 ? Math.abs(currentBalance) : 0);
                transactionAmount = (pendingAmount * 0.8) + additionalAmount;
                this.actualAmountCollected = Math.round(transactionAmount * 100.0) / 100.0 - additionalAmount;
            } else if ("igual".equalsIgnoreCase(amountComparison)) {
                transactionAmount = pendingAmount + (currentBalance < 0 ? Math.abs(currentBalance) : 0);
                this.actualAmountCollected = pendingAmount;
            } else {
                transactionAmount = pendingAmount;
                this.actualAmountCollected = transactionAmount;
            }

            String amount = String.format(Locale.FRANCE, "%.2f", transactionAmount);

            if ("crédito".equalsIgnoreCase(transactionType)) {
                return performAccountCredit(amount);
            } else if ("débito".equalsIgnoreCase(transactionType)) {
                return performAccountDebit(amount);
            } else {
                log.error("Tipo de transacción no reconocido: {}", transactionType);
                return false;
            }
        } catch (Exception e) {
            log.error("Error al realizar la transacción en la cuenta", e);
            return false;
        }
    }

    /**
     * Realiza un crédito a la cuenta mediante la creación de un lote.
     */
    private boolean performAccountCredit(String creditAmount) {
        try {
            String batchName = "ALOTE" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmm"));
            String batchType = "02"; // Tipo de lote 02 para créditos
            String costCenter = "010";

            log.info("Realizando crédito a la cuenta: {}, Monto: {}, Código de transacción: {}",
                    accountNumber, creditAmount, transactionCode);

            batchActions.accessBatchTransaction();
            batchActions.createBatch(batchName, batchType);
            batchActions.addTransactionToBatch(batchName, accountNumber, transactionCode, creditAmount, currencyCode, costCenter);

            return true;
        } catch (Exception e) {
            log.error("Error al realizar el crédito en la cuenta", e);
            return false;
        }
    }

    /**
     * Realiza un débito a la cuenta.
     */
    private boolean performAccountDebit(String debitAmount) {
        // TODO: Implementar la lógica de débito a la cuenta
        log.info("TODO: Implementar proceso de débito a cuenta: {}, Monto: {}",
                accountNumber, debitAmount);
        return true;
    }

    /**
     * Verifica si existe una transacción de débito para la cuenta en la fecha actual.
     */
    public boolean verifyAccountDebit() {
        loginActions.navigateToMainPage();
        navigateConsultAccount(getAccountType(), CURRENT_ACCOUNT_STATUS);
        try {
            ExtentReportManager.captureScreenshot("El cobro realizado se visualiza correctamente en el estado de la cuenta actual");
            boolean displayedAmount = consultAccountPage.selectTransactionByAmount(this.actualAmountCollected);
            Object result = entriesActions.getAccountDailyTransactions(accountNumber);

            if (result instanceof List && !((List<?>) result).isEmpty()) {
                List<Map<String, Object>> transactions = (List<Map<String, Object>>) result;
                double expectedAmount = this.actualAmountCollected;

                for (Map<String, Object> transaction : transactions) {
                    log.info(transaction);
                    Object amountObj = transaction.get("TRANSACTION_AMOUNT");
                    Object debitCodeObj = transaction.get("DEBIT_CREDIT_CODE");
                    if (amountObj != null) {
                        double amount = Double.parseDouble(amountObj.toString());
                        int debitCode = Integer.parseInt(debitCodeObj.toString());
                        if (amount == expectedAmount && debitCode == 6 && displayedAmount) {
                            ExtentReportManager.captureScreenshot("El monto del cobro realizado coincide con el monto que estaba pendiente");
                            log.info("Débito encontrado por el monto: {}", amountObj);
                            return true;
                        }
                    }
                }
            }
            log.warn("No se encontró débito para la cuenta: {} por el monto: {}", accountNumber, pendingAmount);
            return false;
        } catch (Exception e) {
            log.error("Error al verificar el débito de la cuenta", e);
            return false;
        }
    }

    /**
     * Verifica si el monto pendiente coincide con el valor esperado (normalmente cero).
     */
    public boolean verifyPendingAmount(int expectedAmount) {
        try {
            Object result = entriesActions.getCollectionById(collectionId, 1);

            if (result instanceof List && !((List<?>) result).isEmpty()) {
                List<Map<String, Object>> collectionInfo = (List<Map<String, Object>>) result;
                Map<String, Object> info = collectionInfo.getFirst();

                String pendingAmountStr = String.valueOf(info.get("PendingAmount")).trim();
                double pendingAmountNew = Double.parseDouble(pendingAmountStr);

                log.info("Monto pendiente actual: {}, Monto esperado: {}",
                        pendingAmountNew, expectedAmount);

                return Math.abs(pendingAmountNew - expectedAmount) < 0.01;
            }

            log.warn("No se pudo obtener información actualizada sobre la comision: {}", collectionId);
            return false;
        } catch (Exception e) {
            log.error("Error al verificar el monto pendiente", e);
            return false;
        }
    }

    /**
     * Verifica si el monto cobrado coincide con el monto del débito.
     */
    public boolean verifyCollectedAmount() {
        CommissionInfo commissionInfo = commissionActions.getCommissionInfo(collectionId);
        if (commissionInfo != null) {
            double collected = Double.parseDouble(commissionInfo.getRemainingAmount());
            double expected = this.actualAmountCollected;
            log.info("Monto cobrado: {}, Monto esperado: {}", collected, expected);
            return collected == expected;
        }
        return false;
    }

    /**
     * Verifica si el estado del registro coincide con el estado esperado.
     */
    public boolean verifyEntryStatus(String expectedStatus) {
        try {
            Object result = entriesActions.getCollectionById(collectionId, 1);

            if (result instanceof List && !((List<?>) result).isEmpty()) {
                List<Map<String,Object>> collectionInfo = (List<Map<String,Object>>) result;
                Map<String, Object> info = collectionInfo.getFirst();

                String status = String.valueOf(info.get("CollectionStatus")).trim();
                return expectedStatus.equals(status);
            }
            log.warn("El estado del registro difiere del esperado: {}", expectedStatus);
            return false;
        } catch (Exception e) {
            log.error("Error al verificar el estado del registro", e);
            return false;
        }
    }

    /**
     * Verifica si la descripción de la transacción contiene la referencia del registro pendiente.
     */
    public boolean verifyTransactionDescription() {
        try {
            CommissionInfoAccount commissionInfo = commissionActions.getCollectionThadOnlyAccount(accountNumber);
            if (commissionInfo == null) {
                log.warn("No se pudo obtener la información de la comisión para la cuenta: {}", accountNumber);
                return false;
            }

            String referenceDescription = collectionDescription;
            String uiDescription = consultAccountPage.accountDescriptionTransaction.getText().trim();
            log.info("Referencia de comisión obtenida: {}", referenceDescription);

            Object result = entriesActions.getAccountDailyTransactions(accountNumber);

            if (result instanceof List && !((List<?>) result).isEmpty()) {
                List<Map<String, Object>> transactions = (List<Map<String, Object>>) result;

                for (Map<String, Object> transaction : transactions) {
                    Object descriptionObj = transaction.get("ALPHABETIC_DATA_1");
                    if (descriptionObj != null) {
                        String description = descriptionObj.toString().trim();
                        log.info("Comparando descripción: {} con referencia: {}", description, referenceDescription);

                        if (description.contains(referenceDescription) && uiDescription.contains(referenceDescription)) {
                            ExtentReportManager.captureScreenshot("Se muestra la descripción de la transaccion realizada");
                            log.info("La descripción de la transacción contiene la referencia de la comisión");
                            return true;
                        }
                    }
                }
            }

            log.warn("No se encontró ninguna transacción con la referencia de la comisión para la cuenta: {}", accountNumber);
            return false;
        } catch (Exception e) {
            log.error("Error al verificar la descripción de la transacción", e);
            return false;
        }
    }

    /**
     * Verifica si el monto pendiente ha disminuido después de un cobro parcial.
     */
    public boolean verifyPendingAmountDecreased() {
        try {
            Object result = entriesActions.getCollectionById(collectionId, 1);

            if (result instanceof List && !((List<?>) result).isEmpty()) {
                List<Map<String, Object>> collectionInfo = (List<Map<String, Object>>) result;
                Map<String, Object> info = collectionInfo.getFirst();

                String newPendingAmountStr = String.valueOf(info.get("PendingAmount"));
                double newPendingAmount = Double.parseDouble(newPendingAmountStr);
                double initialAmount = Double.parseDouble(initialPendingAmount);

                log.info("Monto pendiente inicial: {}, Monto pendiente actual: {}",
                        initialPendingAmount, newPendingAmountStr);

                return newPendingAmount < initialAmount && newPendingAmount > 0;
            }

            log.warn("Could not get updated commission information: {}", collectionId);
            return false;
        } catch (Exception e) {
            log.error("Error al verificar la disminución del monto pendiente", e);
            return false;
        }
    }

    /**
     * Determina el código de pendingItemType según el tipo de ítem.
     */
    public int determinePendingItemType(String tipoItem) {
        return switch (tipoItem.toLowerCase()) {
            case "comisiones" -> 3;
            case "transacciones" -> (int)(Math.random() * 2) + 4;
            case "prestamos" -> 2;
            default -> 3; // Por defecto, comisiones
        };
    }

    /**
     * Determina el código de statusItem según el estado del ítem.
     */
    public int determineStatusItem(String estadoItem) {
        return switch (estadoItem.toLowerCase()) {
            case "pendientes" -> 1;
            case "parcial", "parciales" -> 3;
            case "vencidos", "vencidas" -> 5;
//          pendiente sin acumulación para transacciones
            case "no acumulacion" -> 6;
            case "castigados", "castigadas" -> 7;
            default -> 1; // Por defecto, pendientes
        };
    }

    /**
     * Navega a la consulta de cuenta con el tipo de cuenta y la opción especificados.
     */
    public void navigateConsultAccount(String accountType, AccountConsultOption option){
        wait.until(ExpectedConditions.visibilityOf(consultAccountPage.mainOperationInput)).click();
        wait.until(ExpectedConditions.visibilityOf(consultAccountPage.mainOperationInput)).sendKeys("Ahorros".equals(accountType) ? "201030" : "201020", Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOf(consultAccountPage.accountNumberInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(consultAccountPage.accountNumberInput)).sendKeys(accountNumber);
        wait.until(ExpectedConditions.visibilityOf(consultAccountPage.numberPageOptions)).clear();
        wait.until(ExpectedConditions.visibilityOf(consultAccountPage.numberPageOptions)).sendKeys(option.getCode(), Keys.ENTER);
    }

    public boolean validateLastPayLoansExpired(String accountNumber) {
        loginActions.navigateToMainPage();
        double descriptionValue;
        boolean valueReturn = false;
        try {
            wait.until(ExpectedConditions.visibilityOf(consultAccountPage.mainOperationInput)).sendKeys("501020", Keys.ENTER);
            wait.until(ExpectedConditions.visibilityOf(consultAccountPage.numberLoandAccountInput)).click();
            wait.until(ExpectedConditions.visibilityOf(consultAccountPage.numberLoandAccountInput)).clear();
            consultAccountPage.numberLoandAccountInput.sendKeys(accountNumber, Keys.ENTER);

            String uiDescription = wait.until(ExpectedConditions.visibilityOf(consultAccountPage.accountDescriptionTransaction)).getText().trim();
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            descriptionValue = format.parse(uiDescription).doubleValue();

            ExtentReportManager.captureScreenshot("Se Muestra los datos del Prestamo.");

            if (descriptionValue == 0.0) {
                valueReturn = true;
            } else {
                log.error("La validación falló: El valor de la transacción de descripción de la cuenta NO es cero para la cuenta '" + accountNumber + "'. Valor actual: " + descriptionValue);
            }

        } catch (Exception e) {
            log.error("Ocurrió un error durante la validación del préstamo para la cuenta '" + accountNumber + "': " + e.getMessage());
        }
        return valueReturn;
    }

    public String validateOverdueLoanPaymen (String accountNumber, boolean shouldNavigateToMainPage) {
        String uiDescription = null;
        try {

            if (shouldNavigateToMainPage) {loginActions.navigateToMainPage();}

            wait.until(ExpectedConditions.visibilityOf(consultAccountPage.mainOperationInput)).sendKeys("501020", Keys.ENTER);
            wait.until(ExpectedConditions.visibilityOf(consultAccountPage.numberLoandAccountInput)).click();
            wait.until(ExpectedConditions.visibilityOf(consultAccountPage.numberLoandAccountInput)).clear();
            consultAccountPage.numberLoandAccountInput.sendKeys(accountNumber, Keys.ENTER);

            uiDescription = wait.until(ExpectedConditions.visibilityOf(consultAccountPage.accountDescriptionTransaction)).getText().trim();
            this.amountLoanBySignature = uiDescription;
            ExtentReportManager.captureScreenshot("Se Muestra los datos del Prestamo");

            loginActions.navigateToMainPage();
        } catch (Exception e) {
            log.error("Ocurrió un error durante la validación del pago del préstamo vencido: " + e.getMessage());
        }
        return uiDescription;
    }

    private String formatDate(String dateString) {
        if (dateString != null && dateString.length() == 8) {
            try {
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"))
                        .format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("es-ES")));
            } catch (Exception e) {
                log.warn("Error al formatear fecha: " + dateString, e);
                return dateString; // Retorna el original si hay error
            }
        }
        return dateString;
    }


    /**
     * Recupera los detalles más actualizados del ítem de cobro desde la base de datos.
     * Se utiliza para verificar el estado final del ítem después de que se ha procesado una transacción.
     *
     * @return Un Mapa que contiene la información más reciente del ítem de cobro (ej. MontoPendiente, MontoCobrado, Estado),
     *         o nulo si no se pudo recuperar la información.
     */
    public Map<String, String> getUpdatedCollectionDetails() {
        try {
            Object result = entriesActions.getCollectionById(this.collectionId, 1);

            // Verificar que el resultado sea una lista no vacía antes de procesar.
            if (result instanceof List && !((List<?>) result).isEmpty()) {
                return (Map<String, String>) ((List<?>) result).getFirst();
            }
            log.warn("No se pudieron recuperar los detalles actualizados para el ID de cobro: {}", this.collectionId);
            return null;
        } catch (Exception e) {
            log.error("Error al obtener los detalles actualizados para el ID de cobro: {}", this.collectionId, e);
            return null;
        }
    }

    /**
     * Obtiene los detalles del préstamo desde la interfaz de usuario, los compara con los datos de la BD y prepara el monto pendiente definitivo para el pago.
     * Asegura que la transacción utilice el monto mayor entre la UI y la BD para garantizar un pago completo.
     * También registra un estado detallado "Antes del Pago" para los préstamos.
     * @param shouldNavigateToMainPage Una bandera para controlar la navegación después de obtener los datos de la UI.
     */
    public void prepareAndLogLoanDetails(boolean shouldNavigateToMainPage) {
        String uiDescription = getOverdueLoanPaymentFromUI(this.collectionId, shouldNavigateToMainPage);
        Assertions.assertNotNull(uiDescription, "No se pudo obtener el monto del préstamo vencido de la UI.");

        try {
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            double uiAmount = format.parse(uiDescription.replace(",", "")).doubleValue();
            double dbAmount = Double.parseDouble(this.initialPendingAmount);

            // Determinar el monto correcto a pagar
            double amountToPay = Math.max(uiAmount, dbAmount);

            // Esto nos permite usar el método genérico performAccountTransaction().
            this.pendingAmount = String.valueOf(amountToPay);

            log.info("Preparación del pago del préstamo completa. Monto BD: {}, Monto UI: {}. Usando monto final para la transacción: {}", dbAmount, uiAmount, amountToPay);

            ExtentReportManager.logStep("Detalles del préstamo consultados para el pago."
                            + "<br><br><b>--------- INFORMACIÓN CORREGIDA ANTES DEL PAGO ---------</b>"
                            + "<br><b>Monto pendiente en la base de datos:</b> " + dbAmount
                            + "<br><b>Monto pendiente en la interfaz:</b> " + uiAmount
                            + "<br><b>Monto real a pagar:</b> " + this.pendingAmount
                    , "info");

        } catch (Exception e) {
            log.error("Error al interpretar o comparar los montos del préstamo.", e);
            Assertions.fail("No fue posible procesar los montos del préstamo. Valor en la interfaz: " + uiDescription + ", Valor en la base de datos: " + this.initialPendingAmount);
        }
    }

    /**
     * Obtiene el monto del pago vencido del préstamo desde la UI (Signature).
     * @param loanId El identificador del préstamo (collectionId).
     * @param shouldNavigateToMainPage Controla si se navega de regreso a la página principal.
     * @return La cadena con el monto desde la UI.
     */
    public String getOverdueLoanPaymentFromUI(String loanId, boolean shouldNavigateToMainPage) {
        String uiDescription = null;
        try {
            if (shouldNavigateToMainPage) {
                loginActions.navigateToMainPage();
            }

            wait.until(ExpectedConditions.visibilityOf(consultAccountPage.mainOperationInput)).sendKeys("501020", Keys.ENTER);
            wait.until(ExpectedConditions.visibilityOf(consultAccountPage.numberLoandAccountInput)).click();
            wait.until(ExpectedConditions.visibilityOf(consultAccountPage.numberLoandAccountInput)).clear();
            consultAccountPage.numberLoandAccountInput.sendKeys(loanId, Keys.ENTER);

            uiDescription = wait.until(ExpectedConditions.visibilityOf(consultAccountPage.accountDescriptionTransaction)).getText().trim();
            this.amountLoanBySignature = uiDescription;
            ExtentReportManager.captureScreenshot("Visualización de los datos del préstamo en Signature.");

            loginActions.navigateToMainPage();
        } catch (Exception e) {
            log.error("Ocurrió un error al obtener el pago del préstamo vencido de la UI: " + e.getMessage());
        }
        return uiDescription;
    }
}