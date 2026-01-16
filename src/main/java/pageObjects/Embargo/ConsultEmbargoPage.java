
package pageObjects.Embargo;

import config.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ConsultEmbargoPage {

    // Elemento para ingresar el número de función
    @FindBy(xpath = "//input[@name='in_2515_60']")
    public WebElement functionInput;

    // Elemento para seleccionar la operación (ingresar "5" para consultar)
    /**
     * Constructor que inicializa los elementos web usando el PageFactory de Selenium.
     */
    public ConsultEmbargoPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }
}