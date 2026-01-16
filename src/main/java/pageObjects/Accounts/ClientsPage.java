package pageObjects.Accounts;

import config.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ExtentReportManager;

import java.time.Duration;
import java.util.Map;

import static actions.ImportedFiles.ConsultImportedFilesActions.log;
import static config.Browser.wait;

public class ClientsPage {
    @FindBy(xpath = "(//a[contains(text(), 'CIF')])[position() = 1]")
    public WebElement btnCIF;
    @FindBy(xpath = "//input[@value = 'Page Down']")
    public WebElement btnPageDown;
    @FindBy(xpath = "(//a[contains(@class, 'HATSLINK')])[position() = 3]")
    public WebElement btnWorkWithClient;
    @FindBy(xpath = "(//a[contains(@class, 'HATSLINK')])[position() = 1]")
    public WebElement btnShowDeposit;
    @FindBy(xpath = "//input[contains(@name, 'in_193_20')]")
    public WebElement inputPosition;
    @FindBy(xpath = "//input[@value = 'Crear']")
    public WebElement btnCreate;
    @FindBy(xpath = "//input[@value = 'Cancelar']")
    public WebElement btnCancel;
    @FindBy(xpath = "//input[contains(@name, 'in_571_18')]")
    public WebElement inputShortName;
    @FindBy(xpath = "(//a[contains(@class, 'HATSLINK')])[position() = 1]")
    public WebElement arrowPersonalClient;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 1]")
    public WebElement inputOption1;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 2]")
    public WebElement inputOption2;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 3]")
    public WebElement inputOption3;
    @FindBy(xpath = "//input[@value = ' OK ']")
    public WebElement btnOk;
    @FindBy(xpath = "//input[contains(@name, 'in_684_6')]")
    public WebElement inputTitleClient;
    @FindBy(xpath = "//input[contains(@name, 'in_816_20')]")
    public WebElement inputClientName;
    @FindBy(xpath = "//input[contains(@name, 'in_948_20')]")
    public WebElement inputMiddleName;
    @FindBy(xpath = "//input[contains(@name, 'in_1080_20')]")
    public WebElement inputLastName;
    @FindBy(xpath = "//input[contains(@name, 'in_1212_20')]")
    public WebElement inputSecondLastName;
    @FindBy(xpath = "//input[contains(@name, 'in_1344_4')]")
    public WebElement inputApartmentNumber;
    @FindBy(xpath = "//input[contains(@name, 'in_1476_20')]")
    public WebElement inputHouseName;
    @FindBy(xpath = "//input[contains(@name, 'in_1519_6')]")
    public WebElement inputHouseNumber;
    @FindBy(xpath = "//input[contains(@name, 'in_1519_6')]")
    public WebElement inputStreet;
    @FindBy(xpath = "(//a[contains(@class, 'HATSLINK')])[position() = 2]")
    public WebElement arrowCologne;
    @FindBy(xpath = "//input[contains(@class, 'HGREEN HF')]")
    public WebElement inputFirtOption;
    @FindBy(xpath = "(//a[contains(@class, 'HATSLINK')])[position() = 3]")
    public WebElement arrowCity;
    @FindBy(xpath = "(//a[contains(@class, 'HATSLINK')])[position() = 4]")
    public WebElement arrowState;
    @FindBy(xpath = "//input[contains(@name, 'in_2136_35')]")
    public WebElement inputSector;
    @FindBy(xpath = "//input[contains(@name, 'in_2311_1')]")
    public WebElement inputDirectionType;
    @FindBy(xpath = "//input[contains(@name, 'in_2443_1')]")
    public WebElement inputConfirmedDate;
    @FindBy(xpath = "//input[contains(@name, 'in_2400_8')]")
    public WebElement inputLivesHereSince;
    @FindBy(xpath = "//input[contains(@name, 'in_684_30')]")
    public WebElement inputPost;
    @FindBy(xpath = "//input[contains(@name, 'in_816_40')]")
    public WebElement inputPatron;
    @FindBy(xpath = "//input[contains(@name, 'in_948_40')]")
    public WebElement inputDirectionOne;
    @FindBy(xpath = "//input[contains(@name, 'in_1080_40')]")
    public WebElement inputDirectionTwo;
    @FindBy(xpath = "//input[contains(@name, 'in_1740_10')]")
    public WebElement inputPostalCodeWork;
    @FindBy(xpath = "//input[contains(@name, 'in_1784_1')]")
    public WebElement inputDirectionTypeWork;
    @FindBy(xpath = "//input[contains(@name, 'in_1872_5')]")
    public WebElement inputSIC_Code;
    @FindBy(xpath = "//input[contains(@name, 'in_2004_40')]")
    public WebElement inputEmail;
    @FindBy(xpath = "//input[contains(@name, 'in_2136_13')]")
    public WebElement inputTelephone;
    @FindBy(xpath = "//input[contains(@name, 'in_2180_5')]")
    public WebElement inputWorkExtension;
    @FindBy(xpath = "//input[contains(@name, 'in_2400_5')]")
    public WebElement inputIncome;
    @FindBy(xpath = "(//a[contains(@class, 'HATSLINK')])[position() = 4]")
    public WebElement arrowIncomeSource;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 2]")
    public WebElement selectIncomeSource;
    @FindBy(xpath = "//input[contains(@name, 'in_2532_6')]")
    public WebElement inputStarDate;
    @FindBy(xpath = "//input[contains(@name, 'in_2576_2')]")
    public WebElement inputYearEmployment;
    @FindBy(xpath = "//input[contains(@name, 'in_2664_3')]")
    public WebElement inputProfessionCode;
    @FindBy(xpath = "//input[contains(@name, 'in_2796_20')]")
    public WebElement payrollNumber;
    @FindBy(xpath = "//input[contains(@name, 'in_819_11')]")
    public WebElement inputSocialNumber;
    @FindBy(xpath = "//input[contains(@name, 'in_1083_20')]")
    public WebElement inputId;
    @FindBy(xpath = "//input[contains(@name, 'in_1124_1')]")
    public WebElement inputClassification;
    @FindBy(xpath = "//input[contains(@name, 'in_1759_1')]")
    public WebElement inputForeignTax;
    @FindBy(xpath = "//input[contains(@name, 'in_2394_5')]")
    public WebElement inputSucursalNumber;
    @FindBy(xpath = "//input[contains(@name, 'in_2444_3')]")
    public WebElement inputPrincipalOfficer;
    @FindBy(xpath = "//input[contains(@name, 'in_2136_5')]")
    public WebElement inputMarketSegment;
    @FindBy(xpath = "//input[contains(@name, 'in_950_8')]")
    public WebElement inputDateOfBirth;
    @FindBy(xpath = "//input[contains(@name, 'in_995_1')]")
    public WebElement inputGender;
    @FindBy(xpath = "//input[contains(@name, 'in_1082_1')]")
    public WebElement inputMaritalStatus;
    @FindBy(xpath = "//input[contains(@value, 'Salida')]")
    public WebElement btnGoOut;
    @FindBy(xpath = "//input[contains(@value, 'Salir')]")
    public WebElement btnSalir;


    // Crear cuentas
    @FindBy(xpath = "//input[contains(@name, 'in_1097_5')]")//00074
    public WebElement inputProductType;
    @FindBy(xpath = "//input[contains(@name, 'in_1229_5')]")//00001
    public WebElement inputSucursalAccount;
    @FindBy(xpath = "//input[contains(@name, 'in_1361_3')]")//ATM
    public WebElement inputOfficeAccount;
    @FindBy(xpath = "//input[contains(@name, 'in_1493_20')]")//ATM
    public WebElement inputTitleAccount;
    @FindBy(xpath = "(//td[contains(text(), '960')])[position() = 1]")
    public WebElement tdAccountNumber;
    @FindBy(xpath = "//input[contains(@name, 'in_2681_3')]")//000
    public WebElement inputMoneyCode;
    @FindBy(xpath = "(//td[contains(text(), '5297')])[position() = 1]")
    public WebElement tdCardNumber;
    @FindBy(xpath = "//input[contains(@name, 'in_1885_2')]")//1
    public WebElement inputNumberOfCardsToGenerate;
    @FindBy(xpath = "//input[contains(@name, 'in_961_6')]")//41228
    public WebElement inputCardExpirationDate;
    @FindBy(xpath = "//input[contains(@name, 'in_1093_6')]")//41228
    public WebElement inputCardNextReviewDate;
    @FindBy(xpath = "//input[contains(@name, 'in_1225_6')]")//
    public WebElement inputNextPinIssueDate;
    @FindBy(xpath = "//input[contains(translate(@value, ' ', ' '), 'Agregar Tarjeta Nueva')]")
    public WebElement btnAddNewCard;


    // Depósito a Plazo
    @FindBy(xpath = "(//a[contains(@class, 'HATSLINK')])[position() = 4]")
    public WebElement btnTermDeposit;
    @FindBy(xpath = "(//a[contains(text(), '301090')])[position() = 1]")
    public WebElement btnAddTermDeposit;
    @FindBy(xpath = "(//td[contains(text(), '00960')])[position() = 1]")
    public WebElement tdTermDepositAccountNumber;
    @FindBy(xpath = "//input[contains(@name, 'in_974_5')]")
    public WebElement inputTypeNewAccount;
    @FindBy(xpath = "//input[contains(@name, 'in_992_5')]")
    public WebElement inputSucursalNumberTermDeposit;
    @FindBy(xpath = "//input[contains(@name, 'in_1388_17')]")
    public WebElement inputIssueAmount;
    @FindBy(xpath = "//input[contains(@name, 'in_1240_1')]")
    public WebElement inputRenewalOption;
    @FindBy(xpath = "//input[contains(@name, 'in_1372_1')]")
    public WebElement inputRenewalPeriod;//M
    @FindBy(xpath = "//input[contains(@name, 'in_1635_2')]")
    public WebElement inputSpecificRenewalDay;
    @FindBy(xpath = "//input[contains(@name, 'in_2164_1')]")
    public WebElement inputDispositionCode;
    @FindBy(xpath = "//input[contains(@name, 'in_1088_18')]")
    public WebElement inputOrderReference;
    @FindBy(xpath = "//input[contains(@name, 'in_1623_12')]")
    public WebElement inputAccountNumberCreateTermDeposit;
    @FindBy(xpath = "//input[contains(@name, 'in_1755_17')]")
    public WebElement inputTermDepositAmount;//0
    @FindBy(xpath = "//input[contains(@name, 'in_1887_3')]")
    public WebElement inputMoneyCodeTermDeposit;
    @FindBy(xpath = "//input[contains(@name, 'in_417_12')]")
    public WebElement inputAccountDeposit;
    public ClientsPage() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }


    /**
     * Pausa la ejecución durante un número determinado de segundos.
     *
     * Este método utiliza Thread.sleep para detener el hilo actual.
     *
     * @param seconds Número de segundos a esperar.
     */
    public void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica si un cliente ha sido creado exitosamente basándose en el nombre corto y el ID.
     *
     * Este método espera la presencia de elementos en el DOM que coincidan con el nombre corto
     * y el número de identificación dados. Si ambos se encuentran dentro del tiempo de espera, devuelve verdadero.
     * De lo contrario, registra un error y devuelve falso.
     *
     * @param shortName  El nombre corto del cliente a verificar.
     * @param id         El número de identificación del cliente.
     * @return           verdadero si se encuentra el cliente; falso en caso contrario.
     */
    public boolean isClientCreated(String shortName, String id){
        WebDriverWait wait = new WebDriverWait(Browser.getWebDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//td[contains(translate(text(), ' ', ' '), '"+shortName+"')]")));

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@value, '"+id+"')]")));
            ExtentReportManager.captureScreenshot("El cliente "+shortName+" con la cédula " +id+ " se ha creado correctamente");
            log.info("✅ Cliente ha sido creado exitosamente: nombreCorto={}, id={}", shortName, id);
            wait.until(ExpectedConditions.visibilityOf(inputOption2)).sendKeys("5");
            wait.until(ExpectedConditions.visibilityOf(btnOk)).click();
            wait.until(ExpectedConditions.visibilityOf(inputOption1)).sendKeys("1");
            ExtentReportManager.captureScreenshot("Se consulta el cliente creado");
            wait.until(ExpectedConditions.visibilityOf(btnOk)).click();
            waitSeconds(3);
            ExtentReportManager.captureScreenshot("Se consulta del Nombre y Dirección del cliente creado");
            wait.until(ExpectedConditions.visibilityOf(btnCancel)).click();
            wait.until(ExpectedConditions.visibilityOf(inputOption1)).clear();
            wait.until(ExpectedConditions.visibilityOf(inputOption2)).sendKeys("1");
            wait.until(ExpectedConditions.visibilityOf(btnOk)).click();
            waitSeconds(3);
            ExtentReportManager.captureScreenshot("Se consulta la información laboral del cliente creado");
            wait.until(ExpectedConditions.visibilityOf(btnSalir)).click();
            return true;
        } catch (TimeoutException e) {
            ExtentReportManager.captureScreenshot("El cliente "+shortName+" con la cédula " +id+ " no se ha creado correctamente");
            log.error("❌ Cliente no encontrado después de la espera. NombreCorto: {}, ID: {}", shortName, id);
            return false;
        }
    }

    /**
     * Verifica si un cliente se puede encontrar para la creación de una cuenta, basándose en el nombre corto y el ID.
     *
     * Este método espera la presencia de elementos en el DOM que coincidan con el nombre corto
     * y el número de identificación dados. Si ambos se encuentran dentro del tiempo de espera, devuelve verdadero.
     * De lo contrario, registra un error y devuelve falso.
     *
     * @param shortName  El nombre corto del cliente a verificar.
     * @param id         El número de identificación del cliente.
     * @return           verdadero si se encuentra el cliente; falso en caso contrario.
     */
    public boolean findClientForCreateAccount(String shortName, String id){
        WebDriverWait wait = new WebDriverWait(Browser.getWebDriver(), Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//td[contains(translate(text(), ' ', ' '), '"+shortName+"')]")));

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@value, '"+id+"')]")));
            ExtentReportManager.captureScreenshot("Se ha encontrado el cliente "+shortName+" con la cédula " +id);
            log.info("✅ Cliente encontrado exitosamente: nombreCorto={}, id={}", shortName, id);
            return true;
        } catch (TimeoutException e) {
            ExtentReportManager.captureScreenshot("No se ha encontrado el cliente "+shortName+" con la cédula " +id);
            log.error("❌ Cliente no encontrado después de la espera. NombreCorto: {}, ID: {}", shortName, id);
            return false;
        }
    }

    /**
     * Selecciona el tipo de cuenta de destino por índice de entrada y confirma la selección.
     *
     * Basado en el tipo de cuenta proporcionado (ej. CORRIENTE, AHORRO, LIBRO MAYOR), este método lo mapea
     * a un índice de entrada correspondiente y rellena el valor "1" en el campo de entrada relacionado.
     * Luego, hace clic en el botón OK para proceder.
     *
     * @param destinyAccount  El tipo de cuenta de destino (debe ser CORRIENTE, AHORRO o LIBRO MAYOR).
     * @throws IllegalArgumentException si el tipo de cuenta proporcionado no es válido.
     */
    public void destinyAccount(String destinyAccount) {
        final Map<String, Integer> TITLE_INDEX_MAP = Map.of(
                "CORRIENTE", 1,
                "AHORRO", 2,
                "LIBRO MAYOR", 3
        );
        Integer index = TITLE_INDEX_MAP.get(destinyAccount.toUpperCase());

        if (index == null) {
            throw new IllegalArgumentException("Tipo de cuenta de destino del cliente no válido: " + destinyAccount);
        }
        Browser.getWebDriver().findElement(By.xpath("(//input[contains(@class, 'HATSINPUT')])[" + index + "]")).sendKeys("1");
        wait.until(ExpectedConditions.visibilityOf(btnOk)).click();
    }
}
