package stepDefinitions.RenovacionYCancelacion;

import actions.Authentication.LoginActions;
import actions.Certificate.CertificateActions;
import actions.Loans.LoansActions;
import actions.Maintenace.SavingsCurrentActions;
import actions.dbActions.EntriesActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.Authentication.LoginPage;
import utils.Navigator;

import java.util.List;

public class CancelacionSteps {
    private final CertificateActions CertiAction = new CertificateActions();
    private final LoginActions login = new LoginActions();
    private final SavingsCurrentActions menuActions = new SavingsCurrentActions();
    private final LoansActions loansAction = new LoansActions();
    private final LoginPage loginPage = new LoginPage();
    private final EntriesActions DBConsult = new EntriesActions();

    @Given("Usuario ingresa usuario {string} y contrasena {string} correcta")
    public void Beginlogin(String user, String Password){
       // login.navigateToLoginPage();
        login.login(user, Password);
        Navigator.waitForLoad();
    }
    @When("Seleccionar opcion {string} he ir al menu 6 pantalla de creacion de prestamos opcion {string} menu prestamo")
    public void ScrennCreationloans(String option, String funtion ){
        menuActions.enterOption(option);
        menuActions.enterFunction(funtion);
        if (funtion.equals("901020")) {
            loansAction.setCurrentLoanType("Línea de Crédito");
        } else {
            loansAction.setCurrentLoanType("Otro");
        }
    }
    @When("Seleccionar opcion {string} he ir a la pantalla cancelacion de certificado {string}")
    public void GoTocancelCertificatePage(String option, String funtion){
        menuActions.enterOption(option);
        menuActions.enterFunction(funtion);
    }
    @And("Insertar documento de identidad {string}")
    public void insertdocument(String documentId) throws InterruptedException {
        loansAction.SelectDocumentId(documentId);
    }

    @And("Agregar cuenta prestamo con tipo de prestamo {string} Y seleccionar cuenta")
    public void FormAddLoans(String loans) throws InterruptedException {
        loansAction.FormAddLoansAccount(loans);
        loansAction.selectAccount();
        //loansAction.createSureLoans();
    }
    @Then("Ingresar a la pantalla {string} para cancelar prestamo")
    public void CancelLoan(String funtion) throws InterruptedException {
        Thread.sleep(1000);
        menuActions.enterFunction(funtion);
        loansAction.CancelLoan();
    }

}
