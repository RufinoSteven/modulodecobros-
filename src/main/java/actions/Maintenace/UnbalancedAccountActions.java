package actions.Maintenace;

import actions.dbActions.EntriesActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Maintenace.SavingsCurrentsPage;
import pageObjects.Maintenace.UnbalancedAccountPage;

import java.util.List;
import java.util.Map;

import static config.Browser.wait;

/**
 * Esta clase proporciona las acciones que se pueden realizar en la página de cuentas desbalanceadas.
 *
 * <p>Ejemplo de uso:</p>
 *
 * <pre>
 * UnbalancedAccountActions unbalancedAccountActions = new UnbalancedAccountActions();
 * unbalancedAccountActions.gotoTheAccountQuery();
 * unbalancedAccountActions.enterAccount();
 * </pre>
 *
 * @author Jodir Jiménez
 * @version 1.0
 */

public class UnbalancedAccountActions {
    private final UnbalancedAccountPage unbalancedAccountPage = new UnbalancedAccountPage();
    private final SavingsCurrentsPage savingsCurrentsPage = new SavingsCurrentsPage();
    private final SavingsCurrentActions savingsCurrentActions = new SavingsCurrentActions();
    private final EntriesActions entriesActions = new EntriesActions();

    public String getSavingUnbalancedAccount() {
        return (String) ((List<Map<String, Object>>) entriesActions.getSavingUnbalancedAccount()).getFirst().get("AccountNumber");
    }

    public String geCurrentsUnbalancedAccount() {
        return (String) ((List<Map<String, Object>>) entriesActions.geCurrentsUnbalancedAccount()).getFirst().get("AccountNumber");
    }

    public void gotoTheAccountQuery() {
        savingsCurrentActions.enterOption("1");
        wait.until(ExpectedConditions.visibilityOf(savingsCurrentsPage.okBtn)).click();
        savingsCurrentActions.enterFunction("201030");
    }

    public void enterAccount() {
        wait.until(ExpectedConditions.visibilityOf(unbalancedAccountPage.accountInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(unbalancedAccountPage.accountInput)).sendKeys(getSavingUnbalancedAccount());
    }

    public void pageNumberInput(String pageNumber) {
        wait.until(ExpectedConditions.visibilityOf(unbalancedAccountPage.pageNumberInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(unbalancedAccountPage.pageNumberInput)).sendKeys("04");
    }
}
