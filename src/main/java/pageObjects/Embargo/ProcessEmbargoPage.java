package pageObjects.Embargo;

import config.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * Esta clase representa la página de procesamiento de embargos y contiene sus elementos web.
 *
 * <p>Esta clase sigue el patrón Page Object Model para encapsular
 * los elementos web de la página de procesamiento de embargos, facilitando su mantenimiento y reutilización.</p>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */

public class ProcessEmbargoPage {

    @FindBy(xpath = "//input[@name='in_2515_60' and @type='text' and contains(@class, 'HATSINPUT')]")
    public WebElement transactionSelectionInput;

    @FindBy(xpath = "//input[@name='in_1535_3']")
    public WebElement inputDescription;

    @FindBy(xpath = "//input[@name='in_1531_3']")
    public WebElement inputDescription2;

    @FindBy(xpath = "//input[@name='in_1667_3']")
    public WebElement inputDescription3;

    @FindBy(xpath = "//input[@name='in_1799_3']")
    public WebElement inputDescription4;

    @FindBy(xpath = "//input[@maxlength='18']")
    public List<WebElement> inputDescription5Amount;

    @FindBy(xpath = " //input[@value='Cancelar']")
    public WebElement OperationCancel;

    @FindBy(xpath = "//input[@id='in_206_10']")
    public WebElement positionOperationInput;

    @FindBy(xpath = "//input[@id='in_1058_2']")
    public WebElement optionInput;

    // Elementos específicos para procesar
    @FindBy(xpath = "//input[@value='Aplicar']")
    public WebElement applyBtn;

    @FindBy(xpath = "//input[@id='in_1588_2']")
    public WebElement processOptionInput;

    @FindBy(xpath = "//tr/td[contains(text(),'No.') and contains(text(),'Operación')]/following-sibling::td[@colspan='10' and contains(@class, 'HWHITE') and contains(@class, 'HF')]")
    public WebElement embargoNumbervalue;

    // Localizador para el valor de operación que debemos capturar
    @FindBy(xpath = "//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[6]/td[4]")
    public WebElement operationValueText;

    // Filas de la tabla (excluyendo encabezados)
    @FindBy(xpath = "//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[9]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[position()>3]")
    public List<WebElement> accountRows;

    // Botones de confirmación y navegación
    @FindBy(xpath = "//input[@id='OKButton']")
    public WebElement okBtn;

    @FindBy(xpath = "//input[@name='[enter]']")
    public WebElement okBtnEnter;

    @FindBy(xpath = "//input[@value='Confirmar']")
    public WebElement confirmBtn;

    @FindBy(xpath = "//input[@id='in_1265_23']")
    public WebElement confirmOperationInput;

    // Campos de objeto de página
    @FindBy(xpath = "//td[@class='HGREEN HF' and @colspan='9' and @nowrap]")
    public WebElement getAccount;

    @FindBy(xpath = "//td[@colspan='10' and contains(@class,'HGREEN') and contains(@class,'HF')]")
    public WebElement getAccountSecond;

    @FindBy(xpath = "//td[@class='HGREEN HF' and @colspan='10' and @nowrap]")
    public WebElement getAccountBasicEmbargo;

    @FindBy(xpath = "//td[@colspan='9' and contains(@class, 'HGREEN') and contains(@class, 'HF') and @nowrap]")
    public WebElement getAccountBasicEmbargofourth;

    @FindBy(xpath = "//td[@class='HGREEN HF' and @colspan='2' and @nowrap]")
    public WebElement getAccountType;

    @FindBy(xpath = "//td[@colspan='30' and contains(@class, 'HBLUE')]")
    public WebElement embargoStatus;

    @FindBy(xpath = "//td[@class='HWHITE HF' and @colspan='23']")
    public WebElement getAmountOperation;

    @FindBy(xpath = "(//td[@class='HWHITE HF'])[1]")
    public WebElement getOperationEmbargo;

    @FindBy(xpath = "(//td[@colspan=\"22\" and contains(@class, \"HWHITE\") and contains(@class, \"HF\")])[1]")
    public WebElement valorOperacionInput;

    @FindBy(xpath = "//td[@colspan=\"22\" and contains(@class, \"HWHITE\") and contains(@class, \"HF\")]")
    public WebElement valorOperacionInputCloseCarta;

    @FindBy(xpath = "//input[@id='in_1527_74']")
    public WebElement consultReferenceInput;

    @FindBy(xpath = "//input[@id='in_671_10']")
    public WebElement operatioNumberInput;

    @FindBy(xpath = "//input[@id='in_1058_2']")
    public WebElement referenceNumberInput;

    @FindBy(xpath = "//input[@id='in_417_10']")
    public WebElement accountNumberInput;

    @FindBy(xpath = "//table//td[contains(@class, 'HGREEN HF')]")
    public List<WebElement> embargoNumberTable;

    @FindBy(xpath = "//table//td[contains(@class, 'HBROWN')]")
    public List<WebElement> embargoAmountTable;

    @FindBy(xpath = "//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[5]/td[10]")
    public WebElement getOperationType;

    // Estado final de la operación
    @FindBy(xpath = "//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[9]/td[18]")
    public WebElement finalOperationStatus;

    // Método para obtener un valor de entrada por índice de fila
    public WebElement getRetainValueInputByRow(int rowIndex) {
        String rowXPath = "//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[9]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[" + (rowIndex + 4) + "]";
        WebElement row = Browser.getWebDriver().findElement(By.xpath(rowXPath));
        return row.findElement(By.xpath(".//input[contains(@onkeypress, 'allowNumericOnly')]"));
    }

    // Método para obtener la entrada de la entidad por índice de fila
    public WebElement getEntityInputByRow(int rowIndex) {
        String rowXPath = "//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[9]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[" + (rowIndex + 4) + "]";
        WebElement row = Browser.getWebDriver().findElement(By.xpath(rowXPath));
        List<WebElement> inputs = row.findElements(By.tagName("input"));
        WebElement valorInput = row.findElement(By.xpath(".//input[contains(@onkeypress, 'allowNumericOnly')]"));
        return inputs.get(inputs.indexOf(valorInput) + 1);
    }

    // Método para obtener el estado de la cuenta por índice de fila
    public String getAccountStatusByRow(int rowIndex) {
        WebElement estadoElement = Browser.getWebDriver().findElement(
                By.xpath("//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[9]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[" + (rowIndex + 4) + "]/td[16]"));
        return estadoElement.getText().trim();
    }

    // Método para obtener el saldo disponible por índice de fila
    public double getAvailableBalanceByRow(int rowIndex) {
        String rowXPath = "//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[9]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[" + (rowIndex + 4) + "]";
        WebElement row = Browser.getWebDriver().findElement(By.xpath(rowXPath));
        return Double.parseDouble(row.findElement(By.xpath(".//td[contains(@class, 'HGREEN HF') and contains(translate(text(), ',', '.'), '[0-9]')]")).getText().replace(",", "").trim());
    }

    public String getAccountNumberByRow(int rowIndex){
        WebElement numberAccount = Browser.getWebDriver().findElement(
                By.xpath("//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[9]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[" + (rowIndex + 4) + "]/td[2]"));
        return numberAccount.getText().trim();
    }

    public String getAccountNumberByTypeAccount(int rowIndex){
        WebElement typeAccount = Browser.getWebDriver().findElement(
                By.xpath("//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[9]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[" + (rowIndex + 4) + "]/td[10]"));
        return typeAccount.getText().trim();
    }

    public WebElement getRetainValueInputByRowTypeAccount(int rowIndex) {
        String rowXPath = "//body[1]/table[1]/tbody[1]/tr[2]/td[1]/table[1]/tbody[1]/tr[1]/td[2]/form[1]/div[1]/table[2]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[9]/td[1]/table[1]/tbody[1]/tr[1]/td[1]/table[1]/tbody[1]/tr[" + (rowIndex + 4) + "]/td[10]";
        WebElement row = Browser.getWebDriver().findElement(By.xpath(rowXPath));
        return row.findElement(By.xpath(".//input[contains(@onkeypress, 'allowNumericOnly')]"));
    }

    public ProcessEmbargoPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }
}
