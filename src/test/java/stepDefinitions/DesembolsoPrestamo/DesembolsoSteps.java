package stepDefinitions.DesembolsoPrestamo;

import actions.Batch.BatchActions;
import actions.Loans.LoansActions;
import actions.Maintenace.SavingsCurrentActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import pageObjects.Loans.LoansPage;
import utils.ExtentReportManager;

import java.util.Map;

public class DesembolsoSteps {
    private  final LoansActions loansActions = new LoansActions();
    private  final BatchActions batchActions = new BatchActions();
    private final SavingsCurrentActions menuActions = new SavingsCurrentActions();
    private final LoansPage loansObjects = new LoansPage();
    private final LoansActions loansAction = new LoansActions();

    @And("Se obtiene el monto maximo y minimo del prestamo {string}")
    public void seObtieneElMontoMaximoYMinimoDelPrestamo(String typeProduct) {
        batchActions.selectEnvironment("QA");
        Map<String, Long> dataAmounLoan = loansActions.getTheMinimumAndMaximumLoanAmount(typeProduct);
        ExtentReportManager.logStep("Monto recibido"+ dataAmounLoan, "pass");
        loansObjects.Button_Salir.click();

    }
    @And("Se ingresa a la funcion {string} y se inserta la cedula del cliente {string}")
    public void seIngresaALaFuncion(String funtion, String idcedula) throws InterruptedException {
        menuActions.enterFunction(funtion);
        loansAction.SelectDocumentId(idcedula);
        ExtentReportManager.logStep("Se selecciona el documento de identidad correctamente.", "pass");
    }
    @And("Agregar cuenta de prestamo con su tipo {string} Y seleccionar cuenta")
    public void FormAddLoans(String loans) throws InterruptedException {
        loansAction.FormAddLoansAccount(loans);
        loansAction.selectAccount();
        //loansAction.createSureLoans();
        ExtentReportManager.logStep("Completar formulario", "pass");
    }
    @Then("Consultar prestamo en funcion {string} y salir de signature")
    public void searchLoansD(String function) {
        menuActions.enterFunction(function);
        loansAction.SearchLoans();
        ExtentReportManager.logStep("El prestamo se consulto correctamenmte.", "pass");
    }
    @And("Se ingresa a la funcion {string} y seleccionamos tipo de credito conjunto insertar la cedula del beneficiario {string}")
    public void EnterTofuntion(String funtion,String idcedula) throws InterruptedException {
        menuActions.enterFunction(funtion);
        ExtentReportManager.captureScreenshot("Ingresamos a la funcion 681110.");
        loansObjects.input_SelectAgreements.sendKeys("18");
        loansObjects.button_OK.click();
        loansObjects.input_SelectpromiPyme.sendKeys("6");
        ExtentReportManager.captureScreenshot("Selecionamos el tipo de acuerdo");
        loansObjects.button_OK.click();
        loansObjects.input_SelectNewAccount.sendKeys("1");
        loansObjects.button_OK.click();
        loansAction.SelectDocumentId(idcedula);
        ExtentReportManager.logStep("Se selecciona el tipo de linea correctamente.", "pass");
    }

}
