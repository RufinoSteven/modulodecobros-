package pageObjects.Embargo;

import config.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchEmbargoPage {

    // Input para digitar el número de función
    @FindBy(xpath = "//input[@id='in_201_10' and @class='HGREEN HF']")
    public WebElement SearchEmbargoInput;

    // Botón OK
    @FindBy(xpath = "//input[@id='OKButton']")
    public WebElement okButton;

    // Primera fila de la tabla
    @FindBy(xpath = "(//td[@class='HGREEN HF' and @colspan='5'])[1]")
    public WebElement firstTableRow;

    // Constructor que inicializa los elementos
    public SearchEmbargoPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }
}
