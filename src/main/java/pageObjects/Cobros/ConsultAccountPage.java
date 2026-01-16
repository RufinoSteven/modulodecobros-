package pageObjects.Cobros;

import config.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ConsultAccountPage {

    @FindBy(xpath = "//input[@name='in_2515_60']")
    public WebElement mainOperationInput;

    @FindBy(xpath = "//input[@id='in_461_2']")
    public WebElement numberPageOptions;

    @FindBy(xpath = "//input[@id='in_417_10']")
    public WebElement accountNumberInput;

    @FindBy(xpath = "//tbody/tr[7]/td[6]")
    public WebElement accountActualBalanced;

    @FindBy(xpath = "//tbody/tr[12]/td[5]")
    public WebElement accountAmountTransaction;

    @FindBy(xpath = "//tbody/tr[19]/td[5]")
    public WebElement accountDescriptionTransaction;

    @FindBy(xpath = "//input[@id='in_413_12']")
    public WebElement numberLoandAccountInput;

    @FindBy(xpath = "//input[contains(@value,'Salir')]")
    public WebElement exitButton;


    public ConsultAccountPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }

    /**
     * Selecciona una transacción de la tabla según el monto especificado
     * @param amount El monto de la transacción a buscar
     * @return verdadero si la transacción se encontró y seleccionó con éxito, falso en caso contrario
     */
    public boolean selectTransactionByAmount(double amount) {
        try {
            DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
            String targetAmount = "-" + df.format(amount).replaceFirst("^-?0\\.", ".");
            String xpath = "//table[@class='HATSTABLE']//tr[.//*[contains(@class, 'HBROWN') and contains(text(), '" + targetAmount + "')]]//input[@type='text']";
            WebElement input = Browser.wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            input.clear();
            input.sendKeys("1", Keys.ENTER);
            double transactionAmount = Double.parseDouble(accountAmountTransaction.getText().trim().replace(",",""));
            return transactionAmount == amount;
        } catch (Exception e) {
            return false;
        }
    }

}
