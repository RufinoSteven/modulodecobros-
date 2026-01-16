package pageObjects.Certificate;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CertificateObjects {
    //Cancel page

    @FindBy(id = ("in_567_12"))
    public WebElement input_NumCertificate;

    @FindBy(xpath = "//input[@id='in_699_4']")
    public WebElement CodTransaction;

    @FindBy(xpath = "//input[@id='in_831_6']")
    public WebElement EffectiveDate;

    @FindBy(xpath = "//input[@id='OKButton']")
    public WebElement OkBtn;

    @FindBy(xpath = "//input[@value='No se pudo encontrar la cuenta.']")
    public WebElement text_notFound;

    @FindBy(xpath = "//input[@id='in_1353_17']")
    public WebElement input_AmountOfRetreat;

    @FindBy(xpath = "//input[@id='in_1485_30']")
    public WebElement input_DescriptionOfRetreat;

    @FindBy(xpath = "//td//td[@class='HCYAN HF'][@colspan='22']")
    public WebElement Text_AMOUNTofCertificate;
}
