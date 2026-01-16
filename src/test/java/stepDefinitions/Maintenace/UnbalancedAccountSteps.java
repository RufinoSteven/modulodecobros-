package stepDefinitions.Maintenace;

import actions.Maintenace.UnbalancedAccountActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Maintenace.SavingsCurrentsPage;
import utils.Navigator;

import static config.Browser.wait;

public class UnbalancedAccountSteps {
    private final SavingsCurrentsPage savingsCurrentsPage = new SavingsCurrentsPage();
    private final UnbalancedAccountActions unbalancedAccountActions = new UnbalancedAccountActions();

    @Given("el usuario ingresa en el menu de consulta de cuentas")
    public void gotoTheAccountQuery() {
        unbalancedAccountActions.gotoTheAccountQuery();
    }

    @When("el usuario ingresa el numero de cuenta en el menu de consulta e ingresa el numero de pagina {string}")
    public void currentAccountStatement(String pageNumber) {
        unbalancedAccountActions.enterAccount();
        Navigator.waitForLoad();
        unbalancedAccountActions.pageNumberInput(pageNumber);
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.elementToBeClickable(savingsCurrentsPage.okBtn)).click();
        Navigator.waitForLoad();
    }

    @Then("^el usuario vizualiza el balance de la cuenta$")
    public void checkBalance() {
        /// CODE
    }

}
