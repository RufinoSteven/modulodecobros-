package actions.Maintenace;

import actions.dbActions.EntriesActions;
import lombok.Getter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Maintenace.SavingsCurrentsPage;

import java.util.List;
import java.util.Map;

import static config.Browser.wait;

/**
 * Esta clase proporciona las acciones que se pueden realizar en la página de ahorros.
 *
 * <p>Ejemplo de uso:</p>
 *
 * <pre>
 * SavingsActions savingsActions = new SavingsActions();
 * savingsActions.navigateToLoginPage();
 * savingsActions.enterOption(String option);
 * </pre>
 *
 * @author Rancer Ventura
 * @version 1.0
 */

public class SavingsCurrentActions {

    private final SavingsCurrentsPage savingsCurrentsPage = new SavingsCurrentsPage();
    private final EntriesActions entriesActions = new EntriesActions();
    @Getter
    String SavingAccount = (String) ((List<Map<String, Object>>) entriesActions.getSavingAccountNumber()).getFirst().get("AccountNumber");
    @Getter
    String CurrentAccount = (String) ((List<Map<String, Object>>) entriesActions.getCurrentAccountNumber()).getFirst().get("AccountNumber");

    public void enterOption(String option) {
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.optionInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.optionInput)).sendKeys(option,Keys.ENTER);
    }

    public void enterFunction(String function) {
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.functionInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.functionInput)).sendKeys(function, Keys.ENTER);
    }

    public void typeSavingAccount() {
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.accountInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.accountInput)).sendKeys(SavingAccount);
    }

    public void typeCurrentAccount() {
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.accountInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.accountInput)).sendKeys(CurrentAccount);
    }

    public void getDate(String option) {
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.dateInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.dateInput)).sendKeys(option);
    }

    public void enterCondition(String option) {
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.conditionOption)).clear();
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.conditionOption)).sendKeys(option);
    }

    public void changeCondition(String option) {
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.conditionInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.conditionInput)).sendKeys(option, Keys.ENTER);
    }

    public void enterSavingAccount() {
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.savingCurrentAccountInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.savingCurrentAccountInput)).sendKeys(SavingAccount, Keys.ENTER);
    }

    public void enterCurrentAccount() {
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.savingCurrentAccountInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.savingCurrentAccountInput)).sendKeys(CurrentAccount, Keys.ENTER);
    }
}



