package pageObjects.Embargo;

import config.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Esta clase representa la página de Creación de Embargo y contiene sus elementos web.
 * Sigue el patrón Page Object Model para encapsular la estructura de la página.
 **/

public class CreateEmbargoPage {

    @FindBy(xpath = "//input[@id='in_1323_1']")
    public WebElement optionInput;

    @FindBy(xpath = "//input[@name='in_2515_60']")
    public WebElement referenceInput;

    @FindBy(xpath = "//input[@value='Crear']")
    public WebElement btnCrear;

    @FindBy(xpath = "//input[@id='in_692_5']")
    public WebElement operationTypeInput;

    @FindBy(xpath = "//input[@id='in_956_2']")
    public WebElement identificationTypeInput;

    @FindBy(xpath = "//input[@id='in_1088_20']")
    public WebElement indentificationNumberInput ;

    @FindBy(xpath = "//input[@id='in_1880_5']")
    public WebElement officeInput ;

    @FindBy(xpath = "//input[@type='button' and @value='Confirmar']")
    public WebElement createEmbargoButton;

    @FindBy(xpath = "//input[@id='OKButton']")
    public WebElement okButton;

    @FindBy(xpath = "(//td[@colspan='10'])[1]")
    public WebElement embargoIDText;

    /**
     * Constructor que inicializa los elementos web usando el PageFactory de Selenium.
     */
    public CreateEmbargoPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }
}