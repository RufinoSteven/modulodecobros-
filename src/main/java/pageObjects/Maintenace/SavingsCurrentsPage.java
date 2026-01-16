package pageObjects.Maintenace;

import config.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * This class represents the savings page and contains its web elements.
 *
 * <p>This class follows the Page Object Model pattern to encapsulate
 * the web elements of the savings page, making it easier to maintain and reuse.</p>
 *
 * @author Rancer Ventura
 * @version 1.0
 */
public class SavingsCurrentsPage {

    @FindBy(xpath = "//*[@id='in_1323_1']")
    public WebElement optionInput;

    @FindBy(xpath = "//input[@id='OKButton']")
    public WebElement okBtn;

    @FindBy(xpath = "/html/body/table[1]/tbody/tr[2]/td/table/tbody/tr/td[2]/form/div/table[3]/tbody/tr[2]/td[2]/input")
    public WebElement functionInput;

    @FindBy(xpath = "//*[@id='in_833_10']")
    public WebElement accountInput;

    @FindBy(xpath = "//*[@id='in_2246_1']")
    public WebElement dateInput;

    @FindBy(xpath = "//*[@id='in_1586_1']")
    public WebElement conditionOption;

    @FindBy(xpath = "//*[@id='in_966_2']")
    public WebElement conditionInput;

    @FindBy(xpath = "//*[@id='in_417_10']")
    public WebElement savingCurrentAccountInput;

    @FindBy(xpath = "//*[@id='in_461_2']")
    public WebElement pageNumberInput;

    public SavingsCurrentsPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }

}
