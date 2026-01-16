package pageObjects.Lots;

import config.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BatchPage {
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 1]")
    public WebElement inputQAEnvironment;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 2]")
    public WebElement inputGLEnvironment;
    @FindBy(xpath = "//input[@name = 'in_2515_60']")
    public WebElement inputTransactions;
    @FindBy(xpath = "//td//input[contains(@value, 'Crear')]")
    public WebElement btnCreate;
    @FindBy(xpath = "//input[@id = 'in_559_20']")
    public WebElement batchName;
    @FindBy(xpath = "//input[@id = 'in_691_2']")
    public WebElement batchType;
    @FindBy(xpath = "//input[@id = 'in_823_30']")
    public WebElement batchDescripcion;
    @FindBy(xpath = "//input[@id = 'in_955_20']")
    public WebElement bacthAmount;
    @FindBy(xpath = "//input[@id = 'OKButton']")
    public WebElement btnOK;
    @FindBy(xpath = "//input[@value = 'Cancelar']")
    public WebElement btnCancel;
    @FindBy(xpath = "(//input[contains(@class, 'HATSBUTTON')])[position() = 5]")
    public WebElement btnCancelAddTransaction;
    @FindBy(xpath = "//input[@id = 'in_192_20']")
    public WebElement inputFindBatch;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 1]")
    public WebElement inputFirstBatchFound;
    @FindBy(xpath = "//input[@id = 'in_703_12']")
    public WebElement inputAccount;
    @FindBy(xpath = "//input[@id = 'in_835_4']")
    public WebElement inputtransaccionCode;
    @FindBy(xpath = "//input[@id = 'in_967_17']")
    public WebElement inputTransaccionAmount;

    @FindBy(xpath = "//input[@id='in_1099_3']")
    public WebElement inputCurrencyCode;
    @FindBy(xpath = "//input[@id = 'in_1627_5']")
    public WebElement inputCostCenter;
    @FindBy(xpath = "(//td[contains(text(), 'Completo')])[position() = 1]")
    public WebElement firstBatchConditionComplete;
    @FindBy(xpath = "//input[@value = 'Confirmar']")
    public WebElement btnConfirm;
    @FindBy(xpath = "//input[@value = 'Renovar']")
    public WebElement btnRenew;
    @FindBy(xpath = "(//td[contains(text(), 'Posted')])[position() = 1]")
    public WebElement firstBatchConditionPost;


    // Method to make the search for the batchName element dynamic during test execution
    public WebElement getConfirmLot(String batchName) {
        return Browser.getWebDriver().findElement(By.xpath("//td[contains(text(), '" + batchName + "')]"));
    }

    // Method to make the search for the accountTransaction element dynamic during test execution
    public WebElement getConfirmTransaction(String accountTransaction) {
        return Browser.getWebDriver().findElement(By.xpath("//td[contains(text(),'" + accountTransaction + "')]"));
    }


    public BatchPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }

}
