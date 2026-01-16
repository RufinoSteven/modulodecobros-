package stepDefinitions.Batch;

import actions.Batch.BatchActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BatchSteps {
    private final BatchActions batchActions = new BatchActions();

    @Given("el usuario navega a la transacción de lote ingresando al entorno {string}")
    public void navigateBathTransaction(String environment) {
        batchActions.navigateBatchTransaction(environment);
    }

    @When("se debe crear una transacción para el nuevo lote y luego postearla, ingresando un nombre de lote {string}, un tipo de lote {string}, una cuenta {string}, un código de transacción {string}, un monto {string}, un codigo de moneda {string} y un centro de costos {string}")
    public void addTransactionToBatch(String batchName, String batchType, String accountTransaction,
                                      String transactionCode, String transactionAmount, String currencyCode,String transactionCostCenter) {
        batchActions.createBatch(batchName, batchType);
        batchActions.addTransactionToBatch(batchName, accountTransaction, transactionCode, transactionAmount, currencyCode,transactionCostCenter);
    }

    @Then("el usuario debe cerrar sesión en Signature$")
    public void verifyLogoutSuccess() {
        /// CODE
    }

}
