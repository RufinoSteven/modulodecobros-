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

import java.util.List;

public class RenovacionSteps {
    private final LoginActions login = new LoginActions();
    private final SavingsCurrentActions menuActions = new SavingsCurrentActions();
    private final CertificateActions CertiAction = new CertificateActions();
    private final EntriesActions DbActions = new EntriesActions();
    private final LoansActions loanAction = new LoansActions();

    @Given("Usuario ingresa {string} con y contrasena {string} correcta")
    public void Login(String user, String Password){
        login.login(user, Password);
    }
    @When("Seleccionar opcion {string} he ir a la pantalla cancelacion de certificados {string}")
    public void SelectScreenCertificateCancel(String option, String funtion){
        menuActions.enterOption(option);
        menuActions.enterFunction(funtion);
    }
    @And("Realizar {string} certificados")
    public void CancelOrRenewCertification(String Process) throws InterruptedException {
        List<String> NumCertificate;
        if (Process.equals("Renovacion")){
            NumCertificate = DbActions.getCertificateCanceled();
        }else {
            NumCertificate = DbActions.getCertificate();
        }
        CertiAction.CancelCertificate(NumCertificate,Process);
    }

    @Then("Renovar prestamos NO EIF")
    public void renewLoans() throws InterruptedException {
        List<String> NumLoas = DbActions.getLOANS();
        String affectiveDate = DbActions.getAffectiveDate();
        loanAction.renewLoans(NumLoas, affectiveDate);
    }
}
