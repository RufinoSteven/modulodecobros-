package stepDefinitions.Embargo;

import actions.Embargo.CompleteInfoEmbargoActions;
import actions.Embargo.ProcessEmbargoActions;
import dataStorageModel.Embargo.EmbargoPageData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.Navigator;

import static utils.ExtentReportManager.logMessage;

/**
 * This class implements the steps for the Cucumber scenarios related to embargo processing.
 *
 * <p>This class connects the scenarios defined in the .feature files
 * with the automation code, using the actions defined in ProcessEmbargoActions.</p>
 *
 * <p>Example of a related scenario:</p>
 * <pre>
 * Given the user accesses the page to review embargos
 * When the user sends embargo "12345" for processing
 * And the user accesses the page to process the embargo
 * When the user processes the embargo "12345"
 * Then the embargo should be processed correctly
 * </pre>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */

public class ProcessEmbargoSteps {

    private final ProcessEmbargoActions processEmbargoActions = new ProcessEmbargoActions();
    private final CompleteInfoEmbargoActions completeInfoEmbargoActions = new CompleteInfoEmbargoActions();
    private final EmbargoPageData embargoPageData = new EmbargoPageData();

    @Given("el usuario accede a la pagina de para revisar embargos")
    public void navigateToRevisionEmbargoPage() throws InterruptedException {
        completeInfoEmbargoActions.navigateToEmbargoReviewPage();
    }

    @When("el usuario envia a procesar el numero de operacion deseada {string}")
    public void sendEmbargoToProcess(String operationNumber) throws InterruptedException {
        embargoPageData.setEmbargoNumber(operationNumber);
        completeInfoEmbargoActions.positionOnEmbargo(embargoPageData.getEmbargoNumber());
        processEmbargoActions.selectOption10();
        processEmbargoActions.pressSpecialKey("F14");
        logMessage = "Se envia el numero de operacion:" + operationNumber;
    }

    @And("el usuario accede a la pagina para procesar el embargo")
    public void navigateToProcessEmbargoPage() throws InterruptedException {
        processEmbargoActions.navigateToProcessEmbargoPage();
    }

    @And("el usuario selecciona la operacion de procesar el embargo {string}")
    public void seleccionarOpcion1() {
        processEmbargoActions.selectOption1();
    }

    @When("el usuario procesa el embargo")
    public void processEmbargo() throws InterruptedException {
        processEmbargoActions.processEmbargo(embargoPageData.getEmbargoNumber(), this.embargoPageData);
        Navigator.waitForLoad();
    }

    @When("el usuario procesa el embargo carta compromiso")
    public void processEmbargoCartaCompromiso() throws InterruptedException {
        processEmbargoActions.processEmbargo(embargoPageData.getEmbargoNumber(), this.embargoPageData);
        Navigator.waitForLoad();
    }

    @Then("el embargo debe quedar procesado correctamente")
    public void embargoProcessedSuccessfully() {
        ///CODE
    }
}
