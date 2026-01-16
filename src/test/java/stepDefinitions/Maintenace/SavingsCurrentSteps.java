package stepDefinitions.Maintenace;

import actions.Authentication.LoginActions;
import actions.Maintenace.SavingsCurrentActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Maintenace.SavingsCurrentsPage;
import utils.Navigator;

import static config.Browser.wait;

public class SavingsCurrentSteps {
    private final SavingsCurrentActions savingsCurrentActions = new SavingsCurrentActions();
    private final SavingsCurrentsPage savingsCurrentsPage = new SavingsCurrentsPage();
    private final LoginActions loginActions = new LoginActions();

    @Given("el usuario ingresa la opcion {string} en el menu")
    public void enterOption(String option) {
        savingsCurrentActions.enterOption(option);
        Navigator.waitForLoad();
    }

    @When("el usuario ingresa la funcion {string}")
    public void enterFunction(String function) {
        savingsCurrentActions.enterFunction(function);
        Navigator.waitForLoad();
    }

    @And("^el usuario hace clic en el botón de ok")
    public void clickOk() {
        wait.until(ExpectedConditions.elementToBeClickable(savingsCurrentsPage.okBtn)).click();
        Navigator.waitForLoad();
    }

    //Assertion
    @Then("^el usuario visualiza la página de mantenimiento de cuentas$")
    public void verifyMaintenacePage() {
        /// CODE
    }

    @When("ir a la  pagina de mantenimiento de cuentas con la opcion {string} y funcion {string}")
    public void goToMaintenacePage(String option, String function) {
        savingsCurrentActions.enterOption(option);
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.elementToBeClickable(savingsCurrentsPage.okBtn)).click();
        Navigator.waitForLoad();
        savingsCurrentActions.enterFunction(function);
        Navigator.waitForLoad();
    }

    @And("regresar a la  pagina del menu principal y dirigirse a la pagina de consulta con la funcion {string}")
    public void goToConsultancePage(String function) {
        loginActions.backToMainMenu();
        Navigator.waitForLoad();
        savingsCurrentActions.enterFunction(function);
        Navigator.waitForLoad();
    }

    @When("el usuario ingresa el numero de cuenta de ahorro en el menu de mantenimiento")
    public void typeSavingAccountNumber() {
        savingsCurrentActions.typeSavingAccount();
        Navigator.waitForLoad();
    }

    @When("el usuario ingresa el numero de cuenta corriente en el menu de mantenimiento")
    public void typeCurrentAccountNumber() {
        savingsCurrentActions.typeCurrentAccount();
        Navigator.waitForLoad();
    }

    @And("el usuario ingresa la opcion {string} en el menu de mantenimiento")
    public void enterMaintenaceInput(String option) {
        savingsCurrentActions.getDate(option);
        Navigator.waitForLoad();
    }

    @And("el usuario va a la pagina de cambio de estatus a traves de la opcion {string} en el menu de mantenimiento")
    public void enterCondition(String option) {
        savingsCurrentActions.enterCondition(option);
        Navigator.waitForLoad();
    }

    @And("el usuario cambia el estatus de la cuenta con la opcion {string} en el menu")
    public void changeCondition(String status) {
        savingsCurrentActions.changeCondition(status);
        Navigator.waitForLoad();
    }

    @And("el usuario ingresa el numero de cuenta de ahorro en el menu de consulta")
    public void enterSavingAccount() {
        savingsCurrentActions.enterSavingAccount();
        Navigator.waitForLoad();
    }

    @And("el usuario ingresa el numero de cuenta corriente en el menu de consulta de cuenta corriente")
    public void enterCurrentAccount() {
        savingsCurrentActions.enterCurrentAccount();
        Navigator.waitForLoad();
    }

    @Then("^el usuario vizualiza el menu de mantenimiento$")
    public void verifyMantenacePage() {
        /// CODE
    }

    @Then("^el usuario vizualiza el estatus de la cuenta$")
    public void verifyAccountStatus() {
        /// CODE
    }

    @Then("el usuario vizualiza la fecha de creacion de la cuenta")
    public void checkCreationDate() {
        /// CODE
    }
}
