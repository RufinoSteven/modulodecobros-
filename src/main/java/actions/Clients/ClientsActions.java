package actions.Clients;


import actions.Batch.BatchActions;
import actions.ImportedFiles.ConsultImportedFilesActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Accounts.ClientsPage;
import pageObjects.Reports.ImportedFilesPages;
import utils.ExtentReportManager;

import static config.Browser.wait;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientsActions {
    private final ClientsPage clientsPage = new ClientsPage();
    public static final Logger log = LogManager.getLogger(ClientsActions.class);
    private final BatchActions batchActions = new BatchActions();
    private final ImportedFilesPages importedFilesPages = new ImportedFilesPages();
    private final ConsultImportedFilesActions consultImportedFilesActions = new ConsultImportedFilesActions();

    /**
     * Accede a la pantalla de transacciones del CIF (Archivo de Información del Cliente).
     * <p>
     * Este método establece el entorno en QA y hace clic en el botón "CIF" para abrir la transacción.
     */
    public void accessTo_CIF_Transaction() {
        batchActions.selectEnvironment("QA");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnCIF)).click();
    }

    /**
     * Accede a la transacción 901020 para trabajar con la información del cliente.
     * <p>
     * Este método hace clic en el botón "Work With Client" para abrir la pantalla de transacción correspondiente.
     */
    public void accessTo_901020_transaction() {
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnWorkWithClient)).click();
    }

    /**
     * Busca un cliente por su ID e inicia el proceso de creación si no se encuentra.
     * <p>
     * Este método introduce el número de identificación del cliente, realiza la búsqueda y procede
     * a crear el cliente haciendo clic en el botón "Crear".
     *
     * @param idNumber El número de identificación del cliente a buscar.
     */
    public void findClientToCreateIt(String idNumber) {
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputPosition)).sendKeys(idNumber);
        importedFilesPages.pressF6();
        clientsPage.waitSeconds(2);
        ExtentReportManager.captureScreenshot("Se busca la cédula " + idNumber + " para verificar que el cliente no se encuentra creado");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnCreate)).click();
    }

    /**
     * Completa el nombre corto del cliente y confirma la selección de cliente personal.
     * <p>
     * Este método establece el nombre corto para el cliente, selecciona la opción de cliente personal
     * y confirma los datos haciendo clic en "OK" dos veces.
     *
     * @param shortName El nombre corto a asignar al cliente.
     */
    public void completeCustomerShortName(String shortName) {
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputShortName)).sendKeys(shortName);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.arrowPersonalClient)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOption2)).sendKeys("1");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.waitSeconds(2);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
    }

    public void completeAddressInfo(String sector, String directionType, String livesHereSince, String confirmDate) {
        ExtentReportManager.captureScreenshot("Primer formulario de creación de cliente");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.arrowCologne)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputFirtOption)).sendKeys("1");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.arrowCity)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputFirtOption)).sendKeys("1");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.arrowState)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputFirtOption)).sendKeys("1");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSector)).sendKeys(sector);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputDirectionType)).sendKeys(directionType);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputLivesHereSince)).sendKeys(livesHereSince);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputConfirmedDate)).sendKeys(confirmDate);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
    }

    /**
     * Completa el formulario de información laboral del cliente.
     * <p>
     * Este metodo rellena varios campos relacionados con el trabajo, como empleador, dirección, información de contacto,
     * ingresos, fecha de inicio del empleo y profesión. También selecciona la fuente de ingresos
     * y finaliza el envío del formulario.
     *
     * @param post           Cargo o título del puesto.
     * @param patron         Nombre del empleador.
     * @param directionOne   Línea 1 de la dirección de trabajo.
     * @param directionTwo   Línea 2 de la dirección de trabajo.
     * @param postalCodeWork Código postal de la dirección de trabajo.
     * @param directionType  Tipo de dirección de trabajo (ej. residencial, comercial).
     * @param SIC_CODE       Código de clasificación industrial.
     * @param email          Dirección de correo electrónico del trabajo.
     * @param telephone      Número de teléfono del trabajo.
     * @param workExtension  Número de extensión del teléfono del trabajo.
     */
    public void completeEmploymentInfo(String post, String patron, String directionOne, String directionTwo, String postalCodeWork,
                                       String directionType, String SIC_CODE, String email, String telephone, String workExtension) {
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputPost)).sendKeys(post);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputPatron)).sendKeys(patron);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputDirectionOne)).sendKeys(directionOne);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputDirectionTwo)).sendKeys(directionTwo);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputPostalCodeWork)).sendKeys(postalCodeWork);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputDirectionTypeWork)).sendKeys(directionType);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSIC_Code)).sendKeys(SIC_CODE);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputEmail)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputTelephone)).sendKeys(telephone);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputWorkExtension)).sendKeys(workExtension);
    }

    /**
     * Completa el formulario de información laboral del cliente.
     * <p>
     * Este metodo rellena varios campos relacionados con el trabajo, principalmente información laboral. También selecciona la fuente de ingresos
     * y finaliza el envío del formulario.
     *
     * @param income          Ingresos mensuales o anuales.
     * @param starDate        Fecha de inicio del empleo.
     * @param yearsEmployment Número de años en el trabajo actual.
     * @param payrollNumber   Número de nómina o ID de empleado.
     * @param professionCode  Código que representa la profesión del cliente.
     */
    public void completeWorkExperience(String income, String starDate, String yearsEmployment, String payrollNumber, String professionCode) {
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputIncome)).sendKeys(income);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.arrowIncomeSource)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.selectIncomeSource)).sendKeys("1");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputStarDate)).sendKeys(starDate);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputYearEmployment)).sendKeys(yearsEmployment);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputProfessionCode)).sendKeys(professionCode);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.payrollNumber)).sendKeys(payrollNumber);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
    }

    /**
     * Completa el formulario de información fiscal y personal del cliente.
     * <p>
     * Este método rellena múltiples campos de datos del cliente, incluyendo número social, ID, clasificación,
     * estado fiscal extranjero, número de sucursal, oficial principal, segmento de mercado, fecha de nacimiento,
     * género y estado civil. Luego, verifica que el cliente se haya creado correctamente.
     *
     * @param socialNumber           Número social del cliente.
     * @param idNumber               Número de identificación del cliente.
     * @param customerClassification Clasificación del cliente.
     * @param foreignTax             Información fiscal extranjera.
     * @param sucursalNumber         Número de la sucursal.
     * @param principalOfficer       Nombre o identificador del oficial principal.
     * @param marketSegment          Clasificación del segmento de mercado.
     * @param dateOfBirth            Fecha de nacimiento del cliente.
     * @param gender                 Género del cliente.
     * @param maritalStatus          Estado civil del cliente.
     * @param shortName              Nombre corto del cliente para verificación.
     */
    public void completeClienteTaxInfo(String socialNumber, String idNumber, String customerClassification, String foreignTax,
                                       String sucursalNumber, String principalOfficer, String marketSegment, String dateOfBirth,
                                       String gender, String maritalStatus, String shortName) {

        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSocialNumber)).sendKeys(socialNumber);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputId)).sendKeys(idNumber);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputClassification)).sendKeys(customerClassification);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputForeignTax)).sendKeys(foreignTax);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSucursalNumber)).sendKeys(sucursalNumber);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputPrincipalOfficer)).sendKeys(principalOfficer);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputMarketSegment)).sendKeys(marketSegment);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputDateOfBirth)).sendKeys(dateOfBirth);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputGender)).sendKeys(gender);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputMaritalStatus)).sendKeys(maritalStatus);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        assertTrue(clientsPage.isClientCreated(shortName, idNumber), "❌ El Cliente no ha sido creado...");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnGoOut)).click();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel)).click();
    }

    /**
     * Accede a la pantalla de transacciones para crear una nueva cuenta.
     * <p>
     * Este método establece el entorno en QA y abre la transacción especificada.
     *
     * @param transaction Código de la transacción a la que se va a acceder.
     */
    public void accessToCreateNewAccountTransaction(String transaction) {
        batchActions.selectEnvironment("QA");
        consultImportedFilesActions.accessTransactions(transaction);
    }

    /**
     * Busca y selecciona un cliente en el sistema para proceder con la creación de la cuenta.
     * <p>
     * Este método introduce el número de identificación del cliente, realiza una búsqueda y verifica
     * si el cliente existe haciendo coincidir el nombre corto y el ID. Luego selecciona al cliente.
     *
     * @param idNumber  Número de identificación del cliente.
     * @param shortName Nombre corto esperado del cliente.
     */
    public void findClientForCreateAccount(String idNumber, String shortName) {
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputPosition)).sendKeys(idNumber);
        importedFilesPages.pressF6();
        clientsPage.waitSeconds(2);
        assertTrue(clientsPage.findClientForCreateAccount(shortName, idNumber), "❌ Cliente no encontrado...");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOption1)).sendKeys("1");
        importedFilesPages.pressEnter();
        importedFilesPages.pressEnter();
    }

    /**
     * Completa la creación de una nueva cuenta y devuelve el número de cuenta generado.
     * <p>
     * Este método rellena la información requerida de la cuenta, como el tipo de producto, sucursal, oficina,
     * título de la cuenta y moneda. Si el tipo de cuenta es "CORRIENTE", realiza pasos de confirmación adicionales.
     *
     * @param typeAccount         Tipo de cuenta (ej. "CORRIENTE").
     * @param productTypeCode     Código que representa el tipo de producto.
     * @param sucursalAccountCode Código de la sucursal para la cuenta.
     * @param officeAccountCode   Código de la oficina para la cuenta.
     * @param titleAccount        Título o descripción de la cuenta.
     * @param moneyCode           Código que representa la moneda de la cuenta.
     * @return El número de cuenta generado.
     */
    public String completeAccountInfo(String typeAccount, String productTypeCode, String sucursalAccountCode, String officeAccountCode, String titleAccount, String moneyCode) {
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputProductType)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputProductType)).sendKeys(productTypeCode);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSucursalAccount)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSucursalAccount)).sendKeys(sucursalAccountCode);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOfficeAccount)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOfficeAccount)).sendKeys(officeAccountCode);
        String accountNumber = wait.until(ExpectedConditions.visibilityOf(clientsPage.tdAccountNumber)).getText().trim();
        ExtentReportManager.captureScreenshot("Se muestra en número de cuenta " + accountNumber + " que se va a crear");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputTitleAccount)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputTitleAccount)).sendKeys(titleAccount);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputMoneyCode)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputMoneyCode)).sendKeys(moneyCode);
        if (typeAccount.equalsIgnoreCase("CORRIENTE")) {
            clientsPage.waitSeconds(2);
            wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
            clientsPage.waitSeconds(2);
            wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
            clientsPage.waitSeconds(2);
            wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        } else {
            wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        }

        return accountNumber;
    }

    /**
     * Completa el proceso de asignación de tarjeta para una cuenta y devuelve el número de tarjeta generado si aplica.
     * <p>
     * Si la cuenta no tiene tarjeta, selecciona la opción correspondiente.
     * Si la cuenta ya tiene una tarjeta, intenta crear una nueva utilizando los detalles de la tarjeta proporcionados.
     * Maneja un reintento en caso de que la opción no esté disponible inmediatamente.
     *
     * @param doesTheAccountHaveCard   Indica si la cuenta ya tiene una tarjeta ("Sí" o "No").
     * @param numbersOfCardsToGenerate Número de tarjetas a generar.
     * @param cardExpirationDate       Fecha de vencimiento de la nueva tarjeta.
     * @param cardNextReviewDate       Fecha para la próxima revisión de la tarjeta.
     * @param nextPinIssueDate         Fecha para la próxima emisión de PIN.
     * @return El número de tarjeta generado si se crea una nueva; de lo contrario, una cadena vacía.
     */
    public String completeCardInfo(String doesTheAccountHaveCard, String numbersOfCardsToGenerate, String cardExpirationDate, String cardNextReviewDate, String nextPinIssueDate) {
        String cardNumber = "";
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.waitSeconds(2);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.waitSeconds(2);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.waitSeconds(2);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        if (doesTheAccountHaveCard.equalsIgnoreCase("No")) {
            wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOption1)).sendKeys("1");
        } else {
            try {
                ExtentReportManager.captureScreenshot("Opción de crear una tarjeta");
                wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOption2)).sendKeys("1");
                cardNumber = completeProcessCreatingNewCard(numbersOfCardsToGenerate, cardExpirationDate, cardNextReviewDate, nextPinIssueDate);

            } catch (TimeoutException | NoSuchElementException e) {
                wait.until(ExpectedConditions.visibilityOf(clientsPage.btnAddNewCard)).click();
                wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOption2)).sendKeys("1");
                cardNumber = completeProcessCreatingNewCard(numbersOfCardsToGenerate, cardExpirationDate, cardNextReviewDate, nextPinIssueDate);
            }

        }
        return cardNumber;
    }

    /**
     * Completa el proceso de creación de una nueva tarjeta y devuelve el número de tarjeta generado.
     * <p>
     * Este método captura el número de tarjeta generado y establece detalles relacionados como
     * el número de tarjetas, fecha de vencimiento, próxima fecha de revisión y próxima fecha de emisión de PIN.
     * Finaliza la creación y sale de la pantalla.
     *
     * @param numbersOfCardsToGenerate Número de tarjetas a generar.
     * @param cardExpirationDate       Fecha de vencimiento de la tarjeta.
     * @param cardNextReviewDate       Fecha para la próxima revisión de la tarjeta.
     * @param nextPinIssueDate         Fecha para la próxima emisión de PIN.
     * @return El número de tarjeta generado.
     */
    public String completeProcessCreatingNewCard(String numbersOfCardsToGenerate, String cardExpirationDate, String cardNextReviewDate, String nextPinIssueDate) {
        importedFilesPages.pressEnter();
        clientsPage.waitSeconds(2);
        String cardNumber = wait.until(ExpectedConditions.visibilityOf(clientsPage.tdCardNumber)).getText().trim();
        ExtentReportManager.captureScreenshot("Se muestra en número de tarjeta " + cardNumber + " que se va a crear");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputNumberOfCardsToGenerate)).sendKeys(numbersOfCardsToGenerate);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputCardExpirationDate)).sendKeys(cardExpirationDate);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputCardNextReviewDate)).sendKeys(cardNextReviewDate);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputNextPinIssueDate)).sendKeys(nextPinIssueDate);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.waitSeconds(2);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.waitSeconds(2);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.waitSeconds(2);
        importedFilesPages.pressF12();
        ExtentReportManager.captureScreenshot("Se ha creado la tarjeta correctamente");
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnGoOut)).click();
        return cardNumber;
    }

    /**
     * Navega a la pantalla de transacciones 301090 para la creación de depósitos a plazo.
     * <p>
     * Este método selecciona el entorno de QA y navega a través de la interfaz de usuario
     * para acceder a la pantalla donde se puede agregar un nuevo depósito a plazo.
     */
    public void accessTo_301090_transaction() {
        batchActions.selectEnvironment("QA");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnTermDeposit)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnAddTermDeposit)).click();
    }

    /**
     * Completa la información de la cuenta requerida para crear un depósito a plazo y devuelve el número de cuenta generado.
     * <p>
     * Este método rellena la identificación del cliente, selecciona el tipo de cuenta e introduce
     * detalles como la sucursal emisora y el monto. Recupera y devuelve el número de cuenta generado.
     *
     * @param idNumber            Número de identificación del cliente.
     * @param typeNewAccount      Código para el nuevo tipo de cuenta.
     * @param sucursalTermDeposit Número de sucursal asociado al depósito a plazo.
     * @param issueAmount         Monto a emitir para el depósito a plazo.
     * @return El número de cuenta del depósito a plazo generado.
     */
    public String completeAccountInfoTermDeposit(String idNumber, String typeNewAccount, String sucursalTermDeposit, String issueAmount) {
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputPosition)).sendKeys(idNumber);
        importedFilesPages.pressF6();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOption1)).sendKeys("1");
        ExtentReportManager.captureScreenshot("Se busca la cédula " + idNumber + "para realizar el depósito a plazo");
        ExtentReportManager.captureScreenshot("Se busca la cédula " + idNumber + " para realizar el depósito a plazo");
        importedFilesPages.pressEnter();
        importedFilesPages.pressEnter();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputTypeNewAccount)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputTypeNewAccount)).sendKeys(typeNewAccount);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        String accountNumber = wait.until(ExpectedConditions.visibilityOf(clientsPage.tdTermDepositAccountNumber)).getText().trim();
        ExtentReportManager.captureScreenshot("Se muestra el número de cuenta " + accountNumber + " donde se realizará el depósito");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSucursalNumberTermDeposit)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSucursalNumberTermDeposit)).sendKeys(sucursalTermDeposit);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputIssueAmount)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputIssueAmount)).sendKeys(issueAmount);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        return accountNumber;
    }

    /**
     * Completa la información de renovación para un depósito a plazo.
     * <p>
     * Este método establece la opción de renovación, el período, el día específico de renovación
     * y el código de disposición, luego confirma los cambios y establece la cuenta de destino.
     *
     * @param renewalOption      Código que indica el tipo de renovación.
     * @param renewalPeriod      Duración del período de renovación.
     * @param specificRenewalDay Día específico para ejecutar la renovación.
     * @param dispositionCode    Código sobre cómo se manejarán los fondos en la renovación.
     * @param destinyAccount     Cuenta a la que se transferirán los fondos.
     */

    public void completeRenewalInfo(String renewalOption, String renewalPeriod, String specificRenewalDay, String dispositionCode, String destinyAccount) {
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputRenewalOption)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputRenewalOption)).sendKeys(renewalOption);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputRenewalPeriod)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputRenewalPeriod)).sendKeys(renewalPeriod);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSpecificRenewalDay)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSpecificRenewalDay)).sendKeys(specificRenewalDay);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputDispositionCode)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputDispositionCode)).sendKeys(dispositionCode);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.waitSeconds(2);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.destinyAccount(destinyAccount);
    }

    /**
     * Completa la creación de una transferencia de orden de destino.
     * <p>
     * Este método rellena los campos requeridos como la referencia de la orden, el monto del depósito a plazo
     * y el código de moneda, luego navega a través de la interfaz para finalizar la transferencia.
     *
     * @param orderReference       Número de referencia de la orden.
     * @param termDepositAmount    Monto a depositar en la cuenta a plazo.
     * @param moneyCodeTermDeposit Código que representa la moneda o el tipo de dinero para el depósito.
     */

    public void completeCreteDestinyOrderTransfer(String orderReference, String termDepositAmount, String moneyCodeTermDeposit, String depositTermNumber) {
        ExtentReportManager.captureScreenshot("Formulario de depósito a plazo");
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOrderReference)).sendKeys(orderReference);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputAccountNumberCreateTermDeposit)).click();
        clientsPage.waitSeconds(2);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.arrowPersonalClient)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputPosition)).sendKeys("960");
        importedFilesPages.pressEnter();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputOption1)).sendKeys("1");
        importedFilesPages.pressEnter();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputTermDepositAmount)).sendKeys(termDepositAmount);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputMoneyCodeTermDeposit)).sendKeys(moneyCodeTermDeposit);
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnOk)).click();
        clientsPage.waitSeconds(2);
        importedFilesPages.pressF10();
        ExtentReportManager.captureScreenshot("Creación de una transferencia de orden de destino completada");
        importedFilesPages.pressEnter();
        importedFilesPages.pressEnter();
        importedFilesPages.pressEnter();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnGoOut)).click();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnCIF)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnPageDown)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.btnShowDeposit)).click();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputAccountDeposit)).clear();
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputAccountDeposit)).sendKeys(depositTermNumber);
        importedFilesPages.pressEnter();
        ExtentReportManager.captureScreenshot("Consulta a la cuenta " + depositTermNumber + " donde se realizó el depósito a plazo");
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnGoOut)).click();
        wait.until(ExpectedConditions.visibilityOf(importedFilesPages.btnCancel)).click();
    }
}