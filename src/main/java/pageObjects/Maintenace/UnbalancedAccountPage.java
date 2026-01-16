package pageObjects.Maintenace;

import config.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UnbalancedAccountPage {

    @FindBy(xpath = "//input[@id = 'in_417_10']")
    public WebElement accountInput;

    @FindBy(xpath = "//input[@id = 'in_461_2']")
    public WebElement pageNumberInput;

    public UnbalancedAccountPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }
}
