package actions.Embargo;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Embargo.SearchEmbargoPage;

import static config.Browser.wait;

public class SearchEmbargoActions {
    private final SearchEmbargoPage searchEmbargoPage = new SearchEmbargoPage();

    public void enterFunctionNumber(String functionNumber) {
        wait.until(ExpectedConditions.visibilityOf(searchEmbargoPage.SearchEmbargoInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(searchEmbargoPage.SearchEmbargoInput)).sendKeys(functionNumber);
        wait.until(ExpectedConditions.visibilityOf(searchEmbargoPage.SearchEmbargoInput)).sendKeys(Keys.ENTER);
    }

    public void clickOkButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchEmbargoPage.okButton)).click();
    }

    public String getFirstTableRowText() {
        wait.until(ExpectedConditions.visibilityOf(searchEmbargoPage.firstTableRow));
        return searchEmbargoPage.firstTableRow.getText();
    }
}
