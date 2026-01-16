package pageObjects.Embargo;

import config.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.AssertionsAndChecks;

/**
 * Esta clase representa la página de completado de información de embargo y contiene sus elementos web.
 *
 * <p>Esta clase sigue el patrón Page Object Model para encapsular
 * los elementos web de la página de información de embargo, facilitando su mantenimiento y reutilización.</p>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */

public class CompleteInfoEmbargoPage {
    // Elementos de la Primera Página
    @FindBy(xpath = "//input[@id='in_201_10']")
    public WebElement positionOperationInputComplete;

    @FindBy(xpath = "//input[@id='in_206_10']")
    public WebElement positionOperationInputProcess;

    @FindBy(xpath = "//input[@id='in_1058_2']")
    public WebElement optionInput;

    // Elementos de la segunda página (Completar Información)
    @FindBy(xpath = "//input[@id='in_1210_15']")
    public WebElement actNumberInput;

    @FindBy(xpath = "//input[@id='in_2002_1']")
    public WebElement requestorIdTypeInput;

    @FindBy(xpath = "//tbody/tr[6]/td[4]")
    public WebElement requestorIdTypeText;

    @FindBy(xpath = "//input[@id='in_2048_20']")
    public WebElement requestorIdNumberInput;

    @FindBy(xpath = "//tbody/tr[6]/td[10]")
    public WebElement requestorIdNumberText;

    @FindBy(xpath = "//tbody/tr[7]/td[8]")
    public WebElement requestorFullNameText;

    @FindBy(xpath = "//input[@id='in_2134_20']")
    public WebElement requestorFirstNameInput;

    @FindBy(xpath = "//input[@id='in_2176_20']")
    public WebElement requestorLastNameInput;

    @FindBy(xpath = "//input[@id='in_2134_40']")
    public WebElement requestorSocialReasonInput;

    @FindBy(xpath = "//input[@id='in_2398_1']")
    public WebElement attorneyIdTypeInput;

    @FindBy(xpath = "//input[@id='in_2444_20']")
    public WebElement attorneyIdNumberInput;

    @FindBy(xpath = "//input[@id='in_2530_20']")
    public WebElement attorneyFirstNameInput;

    @FindBy(xpath = "//input[@id='in_2572_20']")
    public WebElement attorneyLastNameInput;

    @FindBy(xpath = "//input[@id='in_2794_18']")
    public WebElement operationAmountInput;

    @FindBy(xpath = "//td[@colspan=\"16\" and contains(@class, \"HWHITE\") and contains(@class, \"HF\")]")
    public WebElement valueApliedPreviusOperation;

    @FindBy(xpath = "//input[@id='OKButton']")
    public WebElement okBtn;

    @FindBy(xpath = "//input[@value='Confirmar']")
    public WebElement confirmBtn;

    @FindBy(xpath = "//input[@id='in_1323_1']")
    public WebElement environmentSelectionInput;

    @FindBy(xpath = "//input[@name='in_2515_60']")
    public WebElement transactionSelectionInput;

    @FindBy(xpath = "//input[@id='in_1342_10']")
    public WebElement previousOperationInput;

    @FindBy(xpath = "//input[@id='in_1058_2']")
    public WebElement operationConsultInput;

    @FindBy(xpath = "//input[@id='in_2926_40']")
    public WebElement insuranceCompany;

    @FindBy(xpath = "//input[@id='in_3058_20']")
    public WebElement policyCode;

    public CompleteInfoEmbargoPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }

    public WebElement getPositionOperationInput() {
        if (AssertionsAndChecks.Checks.isVisible(positionOperationInputComplete)) {
            return positionOperationInputComplete;
        } else {
            return positionOperationInputProcess;
        }
    }
}
