package stepDefinitions.Embargo;

import actions.Authentication.LoginActions;
import actions.Embargo.CompleteInfoEmbargoActions;
import actions.Embargo.CreateEmbargoActions;
import actions.Embargo.ProcessEmbargoActions;
import actions.Embargo.UserReassignmentActions;
import actions.dbActions.CommissionActions;
import config.Browser;
import dataStorageModel.AccountBalanceId;
import dataStorageModel.Embargo.EmbargoPageData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Embargo.CompleteInfoEmbargoPage;
import pageObjects.Embargo.CreateEmbargoPage;
import utils.ExtentReportManager;
import utils.Navigator;

import java.util.logging.Logger;

import static config.Browser.wait;
import static utils.ExtentReportManager.logMessage;

public class CreateEmbargoSteps {
    private final CreateEmbargoActions createEmbargoActions = new CreateEmbargoActions();
    public static String createdEmbargo;
    public static String lastProcessedEmbargo;
    private final CreateEmbargoPage createEmbargoPage = new CreateEmbargoPage();
    private final CompleteInfoEmbargoActions completeInfoEmbargoActions = new CompleteInfoEmbargoActions();
    private final CompleteInfoEmbargoPage completeInfoEmbargoPage = new CompleteInfoEmbargoPage();
    private final UserReassignmentActions userReassignmentActions = new UserReassignmentActions();
    private final ProcessEmbargoActions processEmbargoActions = new ProcessEmbargoActions();
    private final EmbargoPageData embargoPageData = new EmbargoPageData();
    private static final Logger log = Logger.getLogger(CreateEmbargoSteps.class.getName());
    private static String lastIdentificationNumber = null;
    private final LoginActions loginActions = new LoginActions();

    @Given("el usuario ingresa la referencia {string}")
    public void elUsuarioIngresaLaReferencia(String referencia) throws InterruptedException {
        createEmbargoActions.enterReference(referencia);
    }

    @When("el usuario ingresa numero de funcion {string}")
    public void elUsuarioIngresaNumeroDeFuncion(String functionNumber) {
        createEmbargoActions.enterFunctionNumber(functionNumber);
    }

    @When("el usuario selecciona la opción de creación con F6")
    public void elUsuarioSeDirigeAlApartadoCrearFianza() {
        createEmbargoActions.createFunction();
    }

    @And("el usuario selecciona el tipo de operación {string} y tipo de identificación {string}")
    public void elUsuarioSeleccionaElTipoDeOperacion(String operationTypeInput, String identificationTypeInput) {
        createEmbargoActions.selectOperationTypeAndIdentificationType(operationTypeInput, identificationTypeInput);
        logMessage = "Tipo de operacion y tipo de identificacion:" + operationTypeInput + identificationTypeInput;
        ExtentReportManager.captureScreenshot("el usuario selecciona el tipo de operacion y la indentificacion ");
        ExtentReportManager.logStep("Tipo de operacion y tipo de identificacion:"+ operationTypeInput + identificationTypeInput, "info");
    }

    @And("el usuario ingresa el número de identificación {string} y la oficina {string}")
    public void elUsuarioIngresaElNumeroDeIdentificacion(String identificationNumber, String office) throws InterruptedException {
        createEmbargoActions.enterIdentificationNumberAndOffice(identificationNumber, office);
        logMessage = "Numero de identificacion y oficina:" + identificationNumber + office;
        ExtentReportManager.captureScreenshot("el usuario ingresa el numero de indentificacion y la oficina ");
        ExtentReportManager.logStep("Tipo de operacion y tipo de identificacion:"+ identificationNumber + office, "info");
    }

    @And("el usuario ingresa la oficina {string}")
    public void elUsuarioIngresaElNumeroDeoficina(String office) {
        createEmbargoActions.enterEditOffice(office);
    }

    @Then("El usuario crea un embargo sobre una cuenta corriente sin saldo con tipo de operacion {string} y tipo de identificacion {string}")
    public void crearEmbargoCorrienteSinSaldo(String operationTypeInput, String identificationTypeInput) throws InterruptedException {
        CommissionActions commissionActions = new CommissionActions();
        AccountBalanceId cuentaSinSaldo = commissionActions.getCuentaCorrienteSinSaldo();
        Assertions.assertNotNull(cuentaSinSaldo, "No se encontró una cuenta corriente sin saldo en la base de datos");
        createEmbargoActions.enterFunctionNumber("209450");
        createEmbargoActions.createFunction();
        Navigator.waitForLoad();
        createEmbargoActions.selectOperationTypeAndIdentificationType(operationTypeInput, identificationTypeInput);
        Navigator.waitForLoad();
        createEmbargoActions.enterIdentificationNumberAndOffice(cuentaSinSaldo.getIdIdentification(), "17");
        createdEmbargo = wait.until(ExpectedConditions.visibilityOf(createEmbargoPage.embargoIDText)).getText().replace(" ", "");
        createEmbargoActions.clickConfirmationButton();
        Navigator.waitForLoad();
    }


    @Then("el usuario presiona Enter y confirma la creacion")
    public void elUsuarioHaceClicEnElBotonDeConfirmacion() {
        createdEmbargo = wait.until(ExpectedConditions.visibilityOf(createEmbargoPage.embargoIDText)).getText().replace(" ", "");
        createEmbargoActions.clickConfirmationButton();
        ExtentReportManager.captureScreenshot("Confirmacion de creacion del embargo");
    }

    @Then("el usuario presiona Enter y confirma la modificacion")
    public void elUsuarioHaceClicDeConfirmacion() {
        createEmbargoActions.clickConfirmButton();
    }

    @Then("El usuario crea un embargo que sea del tipo de operacion {string} y tipo de identificacion {string}")
    public void elUsuarioCreaUnEmbargoQueSeaDelTipoDeOperacionYTipoDeIdentificacion(String operationTypeInput, String identificationTypeInput) throws InterruptedException {
        Navigator.waitForLoad();
        CommissionActions commissionActions = new CommissionActions();
        String identificationNumber;
        String accountNumber = null;
        String accountType = null;
        String accountAmount = null;

        if ((operationTypeInput.equals("6") || operationTypeInput.equals("8") || operationTypeInput.equals("4") ||
                operationTypeInput.equals("9") || operationTypeInput.equals("10") || operationTypeInput.equals("11") ||
                operationTypeInput.equals("12")) && lastIdentificationNumber != null) {
            identificationNumber = lastIdentificationNumber;

            if (embargoPageData.getAccountNumberBefore() == null || embargoPageData.getAccountTypeBefore() == null) {
                AccountBalanceId accountBalanceData = commissionActions.getIdIdentificationBalanceAccount(1, "6");
                embargoPageData.setAccountNumberBefore(accountBalanceData.getAccount_Number());
                embargoPageData.setAccountTypeBefore(accountBalanceData.getAccount_Type());
            }

        } else {
            if (identificationTypeInput.equals("6")) {
                AccountBalanceId accountBalanceData = commissionActions.getIdIdentificationBalanceAccount(1, "6");
                identificationNumber = accountBalanceData.getIdIdentification();
                lastIdentificationNumber = identificationNumber;
                accountNumber = accountBalanceData.getAccount_Number();
                accountType = accountBalanceData.getAccount_Type();
                accountAmount = accountBalanceData.getCurrent_Balance();
                embargoPageData.setAccountNumberBefore(accountNumber);
                embargoPageData.setAccountTypeBefore(accountType);

            } else if (identificationTypeInput.equals("3")) {
                AccountBalanceId accountBalanceData = commissionActions.getIdIdentificationBalanceAccount(1, "3");
                identificationNumber = accountBalanceData.getRNC();
                lastIdentificationNumber = identificationNumber;
                accountNumber = accountBalanceData.getAccount_Number();
                accountType = accountBalanceData.getAccount_Type();
                embargoPageData.setAccountNumberBefore(accountNumber);
                embargoPageData.setAccountTypeBefore(accountType);
            } else {
                identificationNumber = lastIdentificationNumber != null ? lastIdentificationNumber : "";
            }
        }
        log.info("Cliente seleccionado para el embargo - Tipo de identificación: " + identificationTypeInput +
                ", - Número de identificacion: " + identificationNumber +
                ", - Numero de cuenta:" + accountNumber +
                ", - Tipo de cuenta:" + accountType +
                ", - Monto de cuenta:" + accountAmount );

        createEmbargoActions.enterFunctionNumber("209450");
        createEmbargoActions.createFunction();
        Navigator.waitForLoad();

        createEmbargoActions.selectOperationTypeAndIdentificationType(operationTypeInput, identificationTypeInput);
        logMessage = "Tipo de operacion y tipo de identificacion: " + operationTypeInput + identificationTypeInput;
        Navigator.waitForLoad();

        createEmbargoActions.enterIdentificationNumberAndOffice(identificationNumber, "17");
        logMessage = "Numero de identificacion y oficina: " + identificationNumber + "17";
        createdEmbargo = wait.until(ExpectedConditions.visibilityOf(createEmbargoPage.embargoIDText))
                .getText().replace(" ", "");
        createEmbargoActions.clickConfirmationButton();
    }


    @Then("El usuario crea un embargo sobre una cuenta de ahorro sin saldo con tipo de operacion {string} y tipo de identificacion {string}")
    public void crearEmbargoAhorroSinSaldo(String operationTypeInput, String identificationTypeInput) throws InterruptedException {
        CommissionActions commissionActions = new CommissionActions();
        AccountBalanceId cuentaSinSaldo = commissionActions.getCuentaAhorroSinSaldo();
        String accountNumber = cuentaSinSaldo.getAccount_Number();
        String accountType = cuentaSinSaldo.getAccount_Type();
        embargoPageData.setAccountNumberBefore(accountNumber);
        embargoPageData.setAccountTypeBefore(accountType);
        Assertions.assertNotNull(cuentaSinSaldo, "No se encontró una cuenta de ahorro sin saldo en la base de datos");
        createEmbargoActions.enterFunctionNumber("209450");
        createEmbargoActions.createFunction();
        Navigator.waitForLoad();
        createEmbargoActions.selectOperationTypeAndIdentificationType(operationTypeInput, identificationTypeInput);
        Navigator.waitForLoad();
        createEmbargoActions.enterIdentificationNumberAndOffice(cuentaSinSaldo.getIdIdentification(), "17");
        createdEmbargo = wait.until(ExpectedConditions.visibilityOf(createEmbargoPage.embargoIDText)).getText().replace(" ", "");
        createEmbargoActions.clickConfirmationButton();
        Navigator.waitForLoad();
    }

    @When("el usuario selecciona la operación consultar embargo con {string}")
    public void theUserSelectsTheConsultEmbargoOperation(String operation) {
        completeInfoEmbargoActions.selectConsultOperation(operation);
        ExtentReportManager.captureScreenshot("El usuario seleciona una operacion ");
        ExtentReportManager.logStep("tipo de operacion: "+ operation, "info");
    }


    @Given("el usuario accede a la pagina de revision de embargos")
        public void accessEmbargoReviewPage() throws InterruptedException {
            completeInfoEmbargoActions.navigateToEmbargoReviewPage();
            Thread.sleep(4000);
        }



    @Given("el usuario completa y procesa el embargo {string}")
    public void completarProcesaEmbargo(String embargoNumber) throws InterruptedException {
        completeInfoEmbargoActions.navigateToEmbargoReviewPage();
        completeInfoEmbargoActions.selectOption2();
        EmbargoPageData embargoData = completeInfoEmbargoActions.buildEmbargoDataFromPage(createdEmbargo);
        completeInfoEmbargoActions.completeInformationForm(embargoData);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.okBtn)).click();
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.confirmBtn)).click();
        Navigator.waitForLoad();
        if (embargoNumber.equals("Existente")) {
            embargoPageData.setEmbargoNumber(createdEmbargo);
        } else {
            embargoPageData.setEmbargoNumber(embargoNumber);
        }
        log.info("EmbargoNumber capturado: " + embargoPageData.getEmbargoNumber());
        ExtentReportManager.captureScreenshot("El usuario seleciona una operacion ");
        ExtentReportManager.logStep("EmbargoNumber capturado: "+ embargoPageData.getEmbargoNumber(), "info");
        Thread.sleep(4000);
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption10();
        processEmbargoActions.pressSpecialKey("F14");
        Navigator.waitForLoad();
        new Actions(Browser.getWebDriver()).sendKeys(Keys.F3).perform();
        processEmbargoActions.navigateToProcessEmbargoPage();
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption1();
        processEmbargoActions.processEmbargo(embargoPageData.getEmbargoNumber(), this.embargoPageData);
        Navigator.waitForLoad();
        lastProcessedEmbargo = createdEmbargo;
        log.info("Ultimo embargo proceed: " + lastProcessedEmbargo);
        ExtentReportManager.captureScreenshot("El usuario procesa el embargo ");
        ExtentReportManager.logStep("Ultimo embargo proceed: "+ lastProcessedEmbargo, "info");

    }

    @Given("el usuario completa y procesa el embargo sin saldo {string}")
    public void completarProcesaEmbargoSinSaldo(String embargoNumber) throws InterruptedException {
        completeInfoEmbargoActions.navigateToEmbargoReviewPage();
        completeInfoEmbargoActions.selectOption2();
        EmbargoPageData embargoData = completeInfoEmbargoActions.buildEmbargoDataFromPage(createdEmbargo);
        completeInfoEmbargoActions.completeInformationForm(embargoData);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.okBtn)).click();
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.confirmBtn)).click();
        loginActions.navigateToMainPageWithF3();
        processEmbargoActions.getAccountBefore(embargoPageData);
        Assertions.assertFalse(processEmbargoActions.verifyEmbargoAmount(), "Se encontro el monto por suspencion del embargo, operacion fallida");
        Assertions.assertFalse(processEmbargoActions.verifyEmbargoNumber(), "Se encontro una suspencion por embargo, operacion fallida");
        ExtentReportManager.captureScreenshot("El usuario consulta estado de la cuenta embargada antes de completar el embargo inverso");
        loginActions.navigateToMainPageWithF3();
        Navigator.waitForLoad();
        createEmbargoActions.enterFunctionNumber("209460");
        if (embargoNumber.equals("Existente")) {
            embargoPageData.setEmbargoNumber(createdEmbargo);
        } else {
            embargoPageData.setEmbargoNumber(embargoNumber);
        }
        log.info("EmbargoNumber capturado: " + embargoPageData.getEmbargoNumber());
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption10();
        processEmbargoActions.pressSpecialKey("F14");
        Navigator.waitForLoad();
        loginActions.navigateToMainPageWithF3();
        processEmbargoActions.navigateToProcessEmbargoPage();
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption1();
        processEmbargoActions.processEmbargoSinSaldo();
        Navigator.waitForLoad();
        lastProcessedEmbargo = createdEmbargo;
        log.info("Ultimo embargo proceed: " + lastProcessedEmbargo);
    }

    @Given("el usuario completa y procesa el embargo {string} con el numero de operacion anterior {string} y el monto anterior {string} y el tipo de operacion {string}")
    public void completarProcesaCartaEmbargo(String embargoNumber, String previousEmbargoNumber, String previousAmount, String operationType) throws InterruptedException {
        completeInfoEmbargoActions.navigateToEmbargoReviewPage();
        completeInfoEmbargoActions.selectOption2();
        EmbargoPageData embargoData = completeInfoEmbargoActions.buildEmbargoDataFromPage(createdEmbargo);
        completeInfoEmbargoActions.completeInformationForm(embargoData);
        if (previousEmbargoNumber.equals("Existente")) {
            String doubledAmount = String.valueOf(Double.parseDouble(completeInfoEmbargoActions.embargoAmount) * 2);
            completeInfoEmbargoActions.enterPreviousOperationNumber(lastProcessedEmbargo, doubledAmount);
            logMessage = "Usuario ingresa el numero de operación y el monto anterior duplicado: " + lastProcessedEmbargo + doubledAmount;
            ExtentReportManager.captureScreenshot("Usuario ingresa el numero de operación y el monto anterior duplicadoo ");
            ExtentReportManager.logStep("Usuario ingresa el numero de operación y el monto anterior duplicado: " +lastProcessedEmbargo + doubledAmount, "info");
        } else {
            completeInfoEmbargoActions.enterPreviousOperationNumber(previousEmbargoNumber, previousAmount);
            logMessage = "Usuario ingresa el numero de operación y el  monto anterior" + previousEmbargoNumber + previousAmount;
            ExtentReportManager.captureScreenshot("Usuario ingresa el numero de operación y el  monto anterior ");
            ExtentReportManager.logStep("Usuario ingresa el numero de operación y el monto anterior duplicado: " + previousEmbargoNumber + previousAmount, "info");
        }
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.confirmBtn)).click();
        Navigator.waitForLoad();
        if (embargoNumber.equals("Existente")) {
            embargoPageData.setEmbargoNumber(createdEmbargo);
        } else {
            embargoPageData.setEmbargoNumber(embargoNumber);
        }

        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption10();
        processEmbargoActions.pressSpecialKey("F14");
        Navigator.waitForLoad();
        new Actions(Browser.getWebDriver()).sendKeys(Keys.F3).perform();
        processEmbargoActions.navigateToProcessEmbargoPage();
        log.info("DEBUG -> EmbargoNumber capturado: " + embargoPageData.getEmbargoNumber());
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption1();
        processEmbargoActions.processEmbargo(operationType, this.embargoPageData);
        Navigator.waitForLoad();
        lastProcessedEmbargo = createdEmbargo;
        log.info("Ultimo embargo proceed: " + lastProcessedEmbargo);
    }

    @And("El usuario se dirige a completar el embargo")
    public void completarEmbargoRNC() throws InterruptedException {
        completeInfoEmbargoActions.navigateToEmbargoReviewPage();
        completeInfoEmbargoActions.selectOption2();
        EmbargoPageData embargoData = completeInfoEmbargoActions.buildEmbargoDataFromPage(createdEmbargo);
        completeInfoEmbargoActions.completeInformationForm(embargoData);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.okBtn)).click();
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.confirmBtn)).click();
        ExtentReportManager.captureScreenshot("El usuario se dirige a completar el embargo");
    }

    @Then("El usuario envia a procesar el embargo")
    public void sentToProcessEmbargoRNC() throws InterruptedException {
        completeInfoEmbargoActions.positionOnEmbargo(createdEmbargo);
        processEmbargoActions.selectOption10();
        processEmbargoActions.pressSpecialKey("F14");
    }

    @Then("el usuario procesa el embargo RNC")
    public void processEmbargoRNC() throws InterruptedException {
        processEmbargoActions.navigateToProcessEmbargoPage();
        completeInfoEmbargoActions.positionOnEmbargo(createdEmbargo);
        processEmbargoActions.selectOption1();
        processEmbargoActions.processEmbargo(createdEmbargo, this.embargoPageData);
        Navigator.waitForLoad();
        ExtentReportManager.captureScreenshot("El usuario procesa el embargo RNC");
    }

    @When("el usuario ingresa el numero de operación {string}")
    public void enterEmbargoNumber(String embargoNumber) throws InterruptedException {
        log.info("Embargo type 11 created: " + embargoNumber);
        ExtentReportManager.captureScreenshot("el usuario ingresa el numero de operacion ");
        ExtentReportManager.logStep("Embargo type 11 created:"+ embargoNumber, "info");
        if (embargoNumber.equals("Existente")) {
            embargoPageData.setEmbargoNumber(createdEmbargo);
            completeInfoEmbargoActions.positionOnEmbargo(createdEmbargo);
            Navigator.waitForLoad();
        } else {
            embargoPageData.setEmbargoNumber(embargoNumber);
            completeInfoEmbargoActions.positionOnEmbargo(embargoNumber);
            Navigator.waitForLoad();
        }
    }

    @When("el usuario selecciona la opción {string} para completar la información del embargo")
    public void selectEmbargoOption(String option) {
        if (option.equals("2")) {
            completeInfoEmbargoActions.selectOption2();
            Navigator.waitForLoad();
        }
    }

    @When("el usuario ingresa el numero de operación {string} del embargo previamente completado con el monto anterior {string}")
    public void previousOperationNumberEmbargoOption(String previousEmbargoNumber, String previousAmount) {
        if (previousEmbargoNumber.equals("Existente")) {
            String doubledAmount = String.valueOf(Double.parseDouble(completeInfoEmbargoActions.embargoAmount) * 2);
            completeInfoEmbargoActions.enterPreviousOperationNumber(lastProcessedEmbargo, doubledAmount);
            ExtentReportManager.captureScreenshot("Usuario ingresa el numero de operación y el monto anterior duplicado");
            ExtentReportManager.logStep("Usuario ingresa el numero de operación y el monto anterior duplicado", "info");
            logMessage = "Usuario ingresa el numero de operación y el monto anterior duplicado: " + lastProcessedEmbargo + doubledAmount;
        } else {
            completeInfoEmbargoActions.enterPreviousOperationNumber(previousEmbargoNumber, previousAmount);
            ExtentReportManager.captureScreenshot("Usuario ingresa el numero de operación y el  monto anterior");
            ExtentReportManager.logStep("Usuario ingresa el numero de operación y el  monto anterior", "info");
            logMessage = "Usuario ingresa el numero de operación y el  monto anterior" + previousEmbargoNumber + previousAmount;
        }
    }

    @When("el usuario ingresa el numero de operación {string} del embargo previamente completado con el monto anterior")
    public void previousOperationNumberEmbargoOptionClose(String previousEmbargoNumber) {
        if (previousEmbargoNumber.equals("Existente")) {
            completeInfoEmbargoActions.enterPreviousOperationCloseNumber(lastProcessedEmbargo);
            logMessage = "Usuario ingresa el numero de operación y el monto anterior duplicado: " + lastProcessedEmbargo;
            ExtentReportManager.captureScreenshot("Usuario ingresa el numero de operación y el monto anterior duplicado ");
            ExtentReportManager.logStep("Usuario ingresa el numero de operación y el monto anterior duplicado: " + lastProcessedEmbargo,"info");
        } else {
            completeInfoEmbargoActions.enterPreviousOperationCloseNumber(previousEmbargoNumber);
            logMessage = "Usuario ingresa el numero de operación y el  monto anterior" + previousEmbargoNumber;
            ExtentReportManager.captureScreenshot("Usuario ingresa el numero de operación y el  monto anterior ");
            ExtentReportManager.logStep("Usuario ingresa el numero de operación y el  monto anterior: " + previousEmbargoNumber,"info");
        }
    }

    @When("el usuario ingresa el numero de operación {string} del embargo previamente completado")
    public void previousOperationNumberEmbargoOptionLifting(String previousEmbargoNumber) {
        if (previousEmbargoNumber.equals("Existente")) {

            completeInfoEmbargoActions.enterPreviousOperationLiftingNumber(embargoPageData.getPreviousOperationNumberLifting());
            logMessage = "Usuario ingresa el numero de operación y el monto anterior duplicado: " + embargoPageData.getPreviousOperationNumberLifting();
            ExtentReportManager.captureScreenshot("Usuario ingresa el numero de operación y el monto anterior duplicado ");
            ExtentReportManager.logStep("Usuario ingresa el numero de operación y el monto anterior duplicado: " + embargoPageData.getPreviousOperationNumberLifting(),"info");
        } else {
            completeInfoEmbargoActions.enterPreviousOperationLiftingNumber(previousEmbargoNumber);
            logMessage = "Usuario ingresa el numero de operación y el  monto anterior" + previousEmbargoNumber;
            ExtentReportManager.captureScreenshot("Usuario ingresa el numero de operación y el  monto anterior ");
            ExtentReportManager.logStep("Usuario ingresa el numero de operación y el  monto anterior: " + previousEmbargoNumber,"info");
        }
    }

    @When("el usuario completa el formulario")
    public void completeSpecificInformationForm() {
        //Create EmbargoData from Map
        EmbargoPageData embargoData = completeInfoEmbargoActions.buildEmbargoDataFromPage(createdEmbargo);
        completeInfoEmbargoActions.completeInformationForm(embargoData);
        logMessage = "Data del Embargo:" + embargoData;
        ExtentReportManager.captureScreenshot("el usuario completa el formulario ");
        ExtentReportManager.logStep("Data del Embargo:"+ embargoData,"info");
    }

    @Then("el usuario confirma la operación")
    public void confirmOperation() throws InterruptedException {
        Thread.sleep(4000);
        processEmbargoActions.pressSpecialKey("F14");
        Navigator.waitForLoad();
    }

    @Then("el usuario da cliclk ok y confirma la operación")
    public void okAndconfirmOperation() {
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.okBtn)).click();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.confirmBtn)).click();
        Navigator.waitForLoad();
    }

    @Then("el usuario envia a procesar el embargo")
    public void sendForProcessing() throws InterruptedException {
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        embargoPageData.setPreviousOperationNumberLifting(embargoPageData.getEmbargoNumber());
        log.info("accountType getAccount: " + embargoPageData.getEmbargoNumber());
        ExtentReportManager.captureScreenshot("el usuario envia a procesar el embargo");
        ExtentReportManager.logStep("accountType getAccount:" + embargoPageData.getEmbargoNumber(),"info");
        processEmbargoActions.selectOption10();
        processEmbargoActions.pressSpecialKey("F14");
        ExtentReportManager.captureScreenshot("el usuario envia a procesar el embargo con F14");
    }

    @Then("el usuario se dirige a procesar el embargo")
    public void procesingEmbargoStep1() throws InterruptedException {
        new Actions(Browser.getWebDriver()).sendKeys(Keys.F3).perform();
        processEmbargoActions.navigateToProcessEmbargoPage();
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption1();
    }

    @Then("el usuario usuario presiona la opcion aplicar")
    public void procesingEmbargoStep2() throws InterruptedException {
        processEmbargoActions.clickApply();
    }

    @Then("el usuario procesa el embargo con tipo de operacion {string}")
    public void procesingEmbargoFinalStep(String operationType) throws InterruptedException {
        processEmbargoActions.processEmbargo(operationType, this.embargoPageData);
        ExtentReportManager.captureScreenshot("El usuario procesa el embargo con tipo de operacion");
    }

    @Then("el usuario consulta estado de la cuenta embargada")
    public void theUserConsultAccount() {
        Navigator.waitForLoad();
        processEmbargoActions.getAccount(embargoPageData);
        Assertions.assertFalse(processEmbargoActions.verifyEmbargoAmount(), "Se encontro el monto por suspencion del embargo, operacion fallida");
        Assertions.assertFalse(processEmbargoActions.verifyEmbargoNumber(), "Se encontro una suspencion por embargo, operacion fallida");
        ExtentReportManager.captureScreenshot("El usuario consulta estado de la cuenta embargada");
    }

    @Then("el usuario consulta estado de la cuenta embargada antes de completar el embargo")
    public void theUserConsultAccountBefore() {
        Navigator.waitForLoad();
        processEmbargoActions.getAccountBefore(embargoPageData);
        Assertions.assertTrue(processEmbargoActions.verifyEmbargoAmount(), "No se encontró el monto por suspensión del embargo");
        Assertions.assertTrue(processEmbargoActions.verifyEmbargoNumber(), "No se encontró el número de suspensión por embargo");
        ExtentReportManager.captureScreenshot("El usuario consulta estado de la cuenta embargada antes de completar el embargo");
    }

    @Then("el usuario consulta estado de la cuenta embargada antes de completar el embargo inverso")
    public void theUserConsultAccountBeforeInverso() {
        Navigator.waitForLoad();
        processEmbargoActions.getAccountBefore(embargoPageData);
        Assertions.assertFalse(processEmbargoActions.verifyEmbargoAmount(), "Se encontro el monto por suspencion del embargo, operacion fallida");
        Assertions.assertFalse(processEmbargoActions.verifyEmbargoNumber(), "Se encontro una suspencion por embargo, operacion fallida");
        ExtentReportManager.captureScreenshot("El usuario consulta estado de la cuenta embargada antes de completar el embargo inverso");
    }


    @Then("el usuario consulta estado de la cuenta disponible embargada")
    public void theUserConsulDispotAccount() {
        Navigator.waitForLoad();
        processEmbargoActions.getAccount(embargoPageData);
        Assertions.assertTrue(processEmbargoActions.verifyEmbargoAmount(), "No se encontró el monto por suspensión del embargo");
        Assertions.assertTrue(processEmbargoActions.verifyEmbargoNumber(), "No se encontró el número de suspensión por embargo");
        ExtentReportManager.captureScreenshot("El usuario consulta estado de la cuenta embargada");
    }

    @Then("el usuario consulta estado de la cuenta disponible embargada usd o eur")
    public void theUserConsulDispotAccountusdeu() {
        Navigator.waitForLoad();
        processEmbargoActions.getAccount(embargoPageData);
        Assertions.assertTrue(processEmbargoActions.verifyEmbargoNumber(), "No se encontró el número de suspensión por embargo");
        ExtentReportManager.captureScreenshot("El usuario consulta estado de la cuenta embargada");
    }

    @Then("el usuario consulta estado de la cuenta sin saldo embargada")
    public void theUserConsultNoBalanceAccount() {
        processEmbargoActions.cuentaSinSaldo();
    }

    @Then("el usuario consulta reportes del embargo por operaciones")
    public void theUserConsultReport() {
        Navigator.waitForLoad();
        processEmbargoActions.consultOperation(embargoPageData.getEmbargoNumber());
        ExtentReportManager.captureScreenshot("El usuario consulta estado de la cuenta sin saldo embargada");
    }

    @Given("se captura el nombre del usuario")
    public void capturaElNombreDelUsuario() {
        userReassignmentActions.captureUserName();
        logMessage = "Nombre del usuario:" + userReassignmentActions.captureUserName();
        ExtentReportManager.captureScreenshot("Se captura el nombre del usuario");
        ExtentReportManager.logStep("Nombre del usuario: "+ userReassignmentActions, "info");
    }

    @When("el usuario ingresa número de referencia para reasignación {string}")
    public void usuarioIntroduceNumeroReferenciaReasignacion(String referencia) throws InterruptedException {
        userReassignmentActions.enterReferenceReassignment(referencia);
    }

    @And("el usuario proporciona el número de función para reasignación {string}")
    public void usuarioIntroduceNumeroFuncionReasignacion(String numeroFuncion) throws InterruptedException {
        userReassignmentActions.enterFunctionNumberReassignment(numeroFuncion);
    }

    @And("el usuario coloca el numero de embargo que se va a reasignar {string}")
    public void elUsuarioColocaNumeroEmbargo(String embargoID) {
        if (embargoID.equals("Existente")) {
            userReassignmentActions.enterOperationNumberInput(CreateEmbargoSteps.createdEmbargo);
        } else {
            userReassignmentActions.enterOperationNumberInput(embargoID);
            logMessage = "Numero de embargo:" + embargoID;
            ExtentReportManager.captureScreenshot("el usuario coloca el numero de embargo que se va a reasignar");
            ExtentReportManager.logStep("Numero de embargo: "+ embargoID,"info");
        }
    }

    @And("el usuario da click Enter, confirmar")
    public void Confirmar() throws InterruptedException {
        userReassignmentActions.clickConfirmAction();
    }

    @And("el usuario introduce la nueva referencia para modificar {string}")
    public void usuarioIngresaLaReferenciaModificar(String referencia2) throws InterruptedException {
        userReassignmentActions.enterReference2(referencia2);
    }

    @And("el usuario ingresa el nombre de usuario que quiere asignar")
    public void elUsuarioIngresaUsuarioAsignado() {
        userReassignmentActions.assignUserIDtoEmbargo();
    }

    @Then("el usuario finaliza la confirmación")
    public void elUsuarioConfirmaFinal() throws InterruptedException {
        userReassignmentActions.confirmFinalAction();
    }

    @And("El usuario se asigna el embargo {string} al usuario logueado")
    public void elUsuarioSeAsignaElEmbargoAlUsuarioLogueado(String embargoID) throws InterruptedException {
        userReassignmentActions.enterFunctionNumberReassignment("209455");
        if (embargoID.equals("Existente")) {
            userReassignmentActions.enterOperationNumberInput(CreateEmbargoSteps.createdEmbargo);
        } else {
            userReassignmentActions.enterOperationNumberInput(embargoID);
            logMessage = "Numero de embargo:" + embargoID;
            ExtentReportManager.captureScreenshot("El usuario se asigna el embargo ");
            ExtentReportManager.logStep("Numero de embargo: "+ embargoID, "info");
        }
        userReassignmentActions.clickConfirmAction();
        userReassignmentActions.enterReference2("2");
        userReassignmentActions.assignUserIDtoEmbargo();
        userReassignmentActions.confirmFinalAction();
    }


    @Then("El usuario crea un embargo que sea del tipo de operacion {string} y tipo de identificacion {string} y del tipo de moneda {string}")
    public void elUsuarioCreaUnEmbargoQueSeaDelTipoDeOperacionYTipoDeIdentificacionYMoneda(String operationTypeInput, String identificationTypeInput, String currencyCode) throws InterruptedException {
        Navigator.waitForLoad();
        CommissionActions commissionActions = new CommissionActions();
        String identificationNumber;
        String accountNumber;
        String accountType;
        AccountBalanceId accountBalanceData;

        if (operationTypeInput.equals("8") && lastIdentificationNumber != null) {
            identificationNumber = lastIdentificationNumber;
        } else {
            if (currencyCode.equals("2")) {
                accountBalanceData = commissionActions.getidIdentificationBalanceAccountByCurrencyCodeEUR(currencyCode);
                accountNumber = accountBalanceData.getAccount_Number();
                accountType = accountBalanceData.getAccount_Type();

                embargoPageData.setAccountNumberBefore(accountNumber);
                embargoPageData.setAccountTypeBefore(accountType);
            } else if (currencyCode.equals("1")) {
                accountBalanceData = commissionActions.getIdIdentificationBalanceAccountByCurrencyCodeUSD(currencyCode);
                accountNumber = accountBalanceData.getAccount_Number();
                accountType = accountBalanceData.getAccount_Type();

                embargoPageData.setAccountNumberBefore(accountNumber);
                embargoPageData.setAccountTypeBefore(accountType);
            } else {
                accountBalanceData = commissionActions.getIdIdentificationBalanceAccountByCurrencyCode(currencyCode);
                accountNumber = accountBalanceData.getAccount_Number();
                accountType = accountBalanceData.getAccount_Type();

                embargoPageData.setAccountNumberBefore(accountNumber);
                embargoPageData.setAccountTypeBefore(accountType);
            }

            identificationNumber = accountBalanceData.getIdIdentification();
            lastIdentificationNumber = identificationNumber;
            processEmbargoActions.setAccountBalanceId(accountBalanceData);
        }

        createEmbargoActions.enterFunctionNumber("209450");
        createEmbargoActions.createFunction();
        Navigator.waitForLoad();

        createEmbargoActions.selectOperationTypeAndIdentificationType(operationTypeInput, identificationTypeInput);
        logMessage = "Tipo de operacion y tipo de identificacion:" + operationTypeInput + identificationTypeInput;
        Navigator.waitForLoad();

        createEmbargoActions.enterIdentificationNumberAndOffice(identificationNumber, "17");
        logMessage = "Numero de identificacion y oficina:" + identificationNumber + "17";

        createdEmbargo = wait.until(ExpectedConditions.visibilityOf(createEmbargoPage.embargoIDText)).getText().replace(" ", "");
        createEmbargoActions.clickConfirmationButton();

        embargoPageData.setEmbargoNumber(createdEmbargo);
    }

    @Given("el usuario completa y procesa el embargo {string} Tomando la cuenta del query")
    public void completarProcesaEmbargoCuentaQuery(String embargoNumber) throws InterruptedException {
        completeInfoEmbargoActions.navigateToEmbargoReviewPage();
        completeInfoEmbargoActions.selectOption2();
        EmbargoPageData embargoData = completeInfoEmbargoActions.buildEmbargoDataFromPage(createdEmbargo);
        completeInfoEmbargoActions.completeInformationForm(embargoData);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.okBtn)).click();
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.confirmBtn)).click();
        Navigator.waitForLoad();
        if (embargoNumber.equals("Existente")) {
            embargoPageData.setEmbargoNumber(createdEmbargo);
        } else {
            embargoPageData.setEmbargoNumber(embargoNumber);
        }
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption10();
        processEmbargoActions.pressSpecialKey("F14");
        Navigator.waitForLoad();
        new Actions(Browser.getWebDriver()).sendKeys(Keys.F3).perform();
        processEmbargoActions.navigateToProcessEmbargoPage();
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption1();
        processEmbargoActions.processEmbargoAccountSelectionQuery(embargoPageData.getEmbargoNumber());
        Navigator.waitForLoad();
        lastProcessedEmbargo = createdEmbargo;
        log.info("Ultimo embargo proceed: " + lastProcessedEmbargo);
    }

    @And("El usuario se dirige a completar el embargo USD")
    public void completarProcesaEmbargoUsd() throws InterruptedException {
        completeInfoEmbargoActions.navigateToEmbargoReviewPage();
        completeInfoEmbargoActions.selectOption2();
        EmbargoPageData embargoData = completeInfoEmbargoActions.buildEmbargoDataFromPage(createdEmbargo);
        completeInfoEmbargoActions.completeInformationForm(embargoData);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.okBtn)).click();
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.confirmBtn)).click();
        Navigator.waitForLoad();
    }

    @Then("el usuario procesa el embargo USD")
    public void processEmabargoUSD() throws InterruptedException {
        processEmbargoActions.navigateToProcessEmbargoPage();
        completeInfoEmbargoActions.positionOnEmbargo(createdEmbargo);
        processEmbargoActions.selectOption1();
        processEmbargoActions.processEmbargo(embargoPageData.getEmbargoNumber(), this.embargoPageData);
        Navigator.waitForLoad();
        Navigator.waitForLoad();
        lastProcessedEmbargo = createdEmbargo;
        log.info("Ultimo embargo proceed: " + lastProcessedEmbargo);
    }

    @And("El usuario selecciona la operacion de embargo con 1")

    public void processEmbargoOpcion1() throws InterruptedException {
        processEmbargoActions.navigateToProcessEmbargoPage();
        completeInfoEmbargoActions.positionOnEmbargo(createdEmbargo);
        processEmbargoActions.selectOption1();
    }

    @And("El usuario selecciona Aplicar")
    public void processEmbargoConOpcionAplicar() throws InterruptedException {
        processEmbargoActions.processEmbargoApplySelection();
    }

    @And("El usuario selecciona la opcion 1")
    public void processEmbargoEnterProcessOption() {
        processEmbargoActions.processEmbargoEnterProcessOption();
    }

    @Then("el usuario consulta estado de la cuenta embargada por el numero de embargo")
    public void theUserConsultAccountByNumberEmbargo() {
        Navigator.waitForLoad();
        processEmbargoActions.getAccount(embargoPageData);
        Assertions.assertTrue(processEmbargoActions.verifyEmbargoNumberByRefactor(), "Se encontro una suspencion por embargo, operacion Exitosa.");
    }



}

