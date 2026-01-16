package stepDefinitions.Prestamos;

import actions.Loans.LoansActions;
import actions.Maintenace.SavingsCurrentActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.ExtentReportManager;

public class CreateLoansSteps {
    private final SavingsCurrentActions menuActions = new SavingsCurrentActions();
    private final LoansActions loansAction = new LoansActions();

    @When("Seleccionar la opcion {string} he ir al menu 6 pantalla de creacion de prestamos opcion {string} menu prestamo")
    public void screenCreationLoans(String option, String funtion) {
        menuActions.enterOption(option);
        menuActions.enterFunction(funtion);
        ExtentReportManager.captureScreenshot("Se ingresa a la pantalla de creacion de prestamos" + funtion);
        if (funtion.equals("901020")) {
            loansAction.setCurrentLoanType("Línea de Crédito");
        } else {
            loansAction.setCurrentLoanType("Otro");
        }
        ExtentReportManager.logStep("Se ingresa con exito a la pantalla de creacion de prestamos.", "pass");
    }

    @And("Insertar el documento de identidad {string}")
    public void insertDocument(String documentId) throws InterruptedException {
        loansAction.SelectDocumentId(documentId);
        ExtentReportManager.logStep("Se selecciona el documento de identidad correctamente.", "pass");
    }

    @And("Agregar cuenta de prestamo con tipo de prestamo {string} Y seleccionar cuenta")
    public void FormAddLoans(String loans) throws InterruptedException {
        loansAction.FormAddLoansAccount(loans);
        loansAction.selectAccount();
        //loansAction.createSureLoans();
        ExtentReportManager.logStep("Completar formulario", "pass");
    }

    @Then("Consultar prestamo funcion {string} y salir de signature")
    public void searchLoans(String function) {
        menuActions.enterFunction(function);
        loansAction.SearchLoans();
        ExtentReportManager.logStep("El prestamo se consulto correctamenmte.", "pass");
    }

    @And("Trabajar linea de credito")
    public void createCreditLine() throws InterruptedException {
        loansAction.createCreditLine();
        ExtentReportManager.logStep("Se creo la linea de credito correctamemte.", "pass");
    }

    @Then("Buscar linea de credito creada y salir de signature")
    public void searchCreditLine() throws InterruptedException {
        loansAction.searchCreditLine();
        ExtentReportManager.logStep("Se encontro la linea de credito.", "pass");
    }
}
