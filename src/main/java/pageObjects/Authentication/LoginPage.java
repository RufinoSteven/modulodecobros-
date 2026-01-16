package pageObjects.Authentication;

import config.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.AssertionsAndChecks;

/**
 * Esta clase representa la página de inicio de sesión y contiene sus elementos web.
 *
 * <p>Esta clase sigue el patrón Page Object Model para encapsular
 * los elementos web de la página de inicio de sesión, facilitando su mantenimiento y reutilización.</p>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */
public class LoginPage {
    @FindBy(xpath = "//input[@id='in_453_10']")
    public WebElement usernameInput;

    @FindBy(xpath = "//input[@id='in_533_28']")
    public WebElement passwordInput;

    @FindBy(xpath = "//input[@id='OKButton']")
    public WebElement signInBtn;

    @FindBy(xpath = "//input[@type='button' and @value='Salir']")
    public WebElement exitBtn1;

    @FindBy(xpath = "//input[@type='button' and @value='Salida']")
    public WebElement exitBtn2;

    @FindBy(xpath = "//td[contains(text(),'salida')]")
    public WebElement logoutPageTitle;

    @FindBy(xpath = "//input[@type='button' and @value=' OK ']")
    public WebElement outOkBtn;



    @FindBy(xpath = "//input[@name='in_2515_60']")
    public WebElement mainOperationInput;

    @FindBy(xpath = "//input[@value='Cancelar']")
    public WebElement cancelButton;

    public LoginPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }

    public WebElement getBackButton() {
        return AssertionsAndChecks.Checks.isVisible(exitBtn1) ? exitBtn1 : exitBtn2;
    }

}
