package pageObjects.Embargo;

import config.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Esta clase representa la página de reasignación de Embargo y contiene sus elementos web.
 * Sigue el patrón Page Object Model para encapsular la estructura de la página.
 **/

public class UserReassignmentPage {

    @FindBy(xpath = "//input[@id='in_1323_1']")
    public WebElement optionInput;

    @FindBy(xpath = "//input[@name='in_2515_60']")
    public WebElement referenceInput;

    @FindBy(xpath = "//input[@value='Confirmar']")
    public WebElement confirmationButton;

    @FindBy(xpath = "//input[@id='OKButton']")
    public WebElement okButton;

    @FindBy(xpath = "//input[@id='in_950_11']")
    public WebElement operationNumberInput;

    @FindBy(xpath = "//input[@id='in_1058_2']")
    public WebElement option2Input;

    @FindBy(xpath = "//input[@id='in_1874_10']")
    public WebElement userNameInput;

    @FindBy(xpath = "//td[contains(@class, 'HMAGENTA HF')]")
    public WebElement userNameField;

    /**
     * Constructor que inicializa los elementos web usando el PageFactory de Selenium.
     */
    public UserReassignmentPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }

    /**
     * Método para capturar el nombre de usuario de la tabla.
     * @return El nombre de usuario extraído como un String.
     */
    public String getUsernameFromTable() {
        return userNameField.getText().trim();
    }
}