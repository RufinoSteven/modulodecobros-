package actions.Loans;

import actions.Authentication.LoginActions;
import actions.dbActions.EntriesActions;
import config.Browser;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Authentication.LoginPage;
import pageObjects.Cobros.ConsultAccountPage;
import pageObjects.Loans.LoansPage;
import pageObjects.Reports.ImportedFilesPages;
import utils.AssertionsAndChecks;
import utils.ExtentReportManager;
import utils.Navigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static config.Browser.wait;
import static utils.ExtentReportManager.logMessage;

public class LoansActions {
    private static final ImportedFilesPages SelectValue = new ImportedFilesPages();
    private static final EntriesActions BDactions = new EntriesActions();
    private static final Logger log = LogManager.getLogger(LoansActions.class);
    private static final Actions actions = new Actions(Browser.getWebDriver());
    // Constantes de configuración de préstamos
    private static final String numOffice = "45220";
    private final LoginActions loginAC = new LoginActions();
    private static final String frequencyPay = "M";
    private static final String propertyAmount = "45000";
    private static final String schemaLoans = "3";
    private static final String numPay = "32";
    public static final String amountNomina = "5000000";
    private static final String approbation = "Y";
    private static final String changedFormSuvar = "N";
    private static final String resources = "1";
    private static final String interestRate = "1800";
    private static final String warranty = "Y";
    private static final String creditLine = "1";
    // Constantes de seguros
    private static final String TypeSureLoans = "00649";
    private static final String policyNumber = "00649";
    private static final String loanValue = "45000";
    private static final String fixedPrima = "1";
    private static final String FrequencyPrima = "1";
    private static final String OpCalSure = "0";
    private static final String PeriodPrima = "M";
    // Constantes de línea de crédito
    private static final String TypeLineCredit = "10700";
    private static final String CodeBilling = "6";
    private static final String RevisionDate = "100726";
    private static final String LimitWarningSurprise = "002";
    private static final String amountLineCredit = "50000";
    // Tipos de préstamo
    private static final String LOAN_TYPE_CONSUMO = "20666";
    private static final String WarningMaturityOfficial = "002";
    private static final String WarningMaturityClient = "002";
    private static final String LOAN_TYPE_COMERCIAL = "10657";
    private static final String Committed = "2";
    private static final String LOAN_TYPE_EMPLEADO = "20637";
    private static final String LOAN_TYPE_HIPOTECARIO = "30701";
    private static final String LOAN_TYPE_PROMIPYME = "50692";
    private static final String[] LOAN_TYPES = {
            "Consumo", "Comercial", "EmplFeliz", "Hipotecario","PROMIPYME"
    };
    private static final String[] LOAN_CODES = {
            LOAN_TYPE_CONSUMO, LOAN_TYPE_COMERCIAL, LOAN_TYPE_EMPLEADO, LOAN_TYPE_HIPOTECARIO, LOAN_TYPE_PROMIPYME
    };
    // Objetos de página
    private final LoginPage loginPage = new LoginPage();
    private final LoansPage loansObjects = new LoansPage();
    public String NumProductLoans = "";
    public String NumProductLoansCreated = "";
    public String NumLineCredit = "";
    public String NumProductLineCreated = "";
    // Variables de instancia
    @Setter
    private String currentLoanType = "DEFAULT";
    private final String code = String.format("%05d", ThreadLocalRandom.current().nextInt(100000));

    public void SelectDocumentId(String documentId) throws InterruptedException {
        if (AssertionsAndChecks.Checks.isVisible(loansObjects.setCustomer)) {
            wait.until(ExpectedConditions.visibilityOf(loansObjects.setCustomer)).click();
            loansObjects.setCustomer.sendKeys(documentId, Keys.ENTER);
        } else {
            logMessage = "No se encontró el campo para insertar el ID.";
        }
        Thread.sleep(1000);
        List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath("//table[@class='HATSTABLE']//tbody//tr[3]"));
        int countElements = elements.size();
        if (countElements > 0) {
            for (WebElement fila : elements) {
                List<WebElement> cells = fila.findElements(By.xpath("//td[contains(@class,'HGREEN HF')][2]"));
                for (WebElement cedula : cells) {
                    String getDocument = cedula.getText().trim();
                    if (getDocument.equals(documentId)) {
                        try {
                            WebElement buttonInput = cells.get(2);
                            WebElement input = buttonInput.findElement(By.xpath("//input[@class='HATSINPUT']"));
                            input.isEnabled();
                            input.click();
                            ExtentReportManager.captureScreenshot("Se visualiza los documentos de identidad y validamos el siguinente: " + documentId);
                            String valueToSend = currentLoanType.equalsIgnoreCase("Línea de Crédito") ? "16" : "1";
                            input.sendKeys(valueToSend, Keys.ENTER);
                            Thread.sleep(1000);
                            actions.sendKeys(Keys.ENTER).perform();
                            log.info("Cédula encontrada y valor insertado.");
                        } catch (Exception e) {
                            log.info("No se pudo escribir en la celda 2: " + e.getMessage());
                        }
                        break; // se encontró la cédula, no es necesario seguir iterando
                    }
                }
                break;
            }
        } else {
            log.info("No se encontraron filas en la tabla.");
        }
    }

    public void FormAddLoansAccount(String loanType) throws InterruptedException {
        if (!isValidLoanType(loanType)) {
            throw new IllegalArgumentException("Tipo de préstamo no válido: " + loanType);
        }

        int index = getLoanTypeIndex(loanType);
        String loanCode = LOAN_CODES[index];

        setLoanType(loanCode);
        NumProductLoans = loansObjects.Text_NumLoans.getText().trim();
        setLoanConfiguration();
        formCodesAndValues();
    }

    private boolean isValidLoanType(String loanType) {
        for (String type : LOAN_TYPES) {
            if (type.equalsIgnoreCase(loanType)) {
                return true;
            }
        }
        return false;
    }

    private int getLoanTypeIndex(String loanType) {
        for (int i = 0; i < LOAN_TYPES.length; i++) {
            if (LOAN_TYPES[i].equalsIgnoreCase(loanType)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid loan type: " + loanType);
    }

    private void setLoanType(String loanCode) {
        AssertionsAndChecks.Checks.isVisible(loansObjects.input_typeOfloans);
        loansObjects.input_typeOfloans.clear();
        loansObjects.input_typeOfloans.click();
        loansObjects.input_typeOfloans.sendKeys(loanCode);
    }

    private void setLoanConfiguration() {
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_NumOffice)).click();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_NumOffice)).sendKeys(numOffice);
        loansObjects.input_SchemaLoans.click();
        loansObjects.input_SchemaLoans.sendKeys(schemaLoans);
        loansObjects.input_NumOfPay.click();
        loansObjects.input_NumOfPay.sendKeys(numPay);
        loansObjects.input_mountOfNomina.click();
        loansObjects.input_mountOfNomina.sendKeys(amountNomina);
        loansObjects.input_PayFrecuency.click();
        loansObjects.input_PayFrecuency.sendKeys(frequencyPay);
        loansObjects.input_SU_VAR.click();
        loansObjects.input_SU_VAR.sendKeys(changedFormSuvar);
        loansObjects.AmountInmueble.click();
        loansObjects.AmountInmueble.sendKeys(propertyAmount);
        ExtentReportManager.captureScreenshot("Rellenar formulario de configuración.");
        loansObjects.button_OK.click();
    }

    public void formCodesAndValues() throws InterruptedException {
        if (AssertionsAndChecks.Checks.isVisible(loansObjects.input_interestRate)) {
            loansObjects.input_interestRate.click();
            loansObjects.input_interestRate.sendKeys(interestRate);
            loansObjects.input_warranty.click();
            loansObjects.input_warranty.sendKeys(warranty);
            loansObjects.input_aprobation.click();
            loansObjects.input_aprobation.sendKeys(approbation);
            loansObjects.input_OpCalSure.clear();
            loansObjects.input_OpCalSure.sendKeys(OpCalSure);
            loansObjects.input_recursos.click();
            loansObjects.input_recursos.sendKeys(resources);
            loansObjects.input_crèditoLine.click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.visibilityOf(loansObjects.input_loanValue)).sendKeys(loanValue);
            wait.until(ExpectedConditions.visibilityOf(loansObjects.button_OK)).click();
            Thread.sleep(1000);
            if (AssertionsAndChecks.Checks.isVisible(loansObjects.input_PlanLoans)) {
                actions.sendKeys(Keys.ENTER).perform();
                if(validateCredilinePyme()){return;}
                addLoansInformation();
            }
        }
    }
    public Boolean validateCredilinePyme(){
        if (AssertionsAndChecks.Checks.isVisible(loansObjects.select_CustomerScreenConectLineCredit)){
            actions.sendKeys(Keys.ENTER).perform();
            ExtentReportManager.captureScreenshot("Seleccionar acuerdos de inversiòn.");
            loansObjects.button_OK.click();
            return true;
        }
        return false;
    }

    public void createSureLoans() throws InterruptedException {
        if (!AssertionsAndChecks.Checks.isVisible(loansObjects.table_Sureloans)) {
            return;
        }

        actions.sendKeys(Keys.F6).perform();
        setInsuranceField(loansObjects.input_TypeSureLoans, TypeSureLoans);
        setInsuranceField(loansObjects.input_NumPoliza, policyNumber);
        setInsuranceField(loansObjects.input_fijaPrima, fixedPrima);
        Thread.sleep(1000);
        setInsuranceField(loansObjects.input_PeriodoPrima, PeriodPrima);
        setInsuranceField(loansObjects.input_FrecuencyPrima, FrequencyPrima);
        setInsuranceField(loansObjects.input_codeBilling, CodeBilling);
        actions.sendKeys(Keys.ENTER).perform();
    }

    private void setInsuranceField(WebElement element, String value) {
        wait.until(ExpectedConditions.visibilityOf(element)).clear();
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(value, Keys.ENTER);
    }

    public void addLoansInformation() throws InterruptedException {
        Navigator.waitForLoad();
        if (AssertionsAndChecks.Checks.isVisible(loansObjects.input_codigoTaza)) {
            actions.sendKeys(Keys.ENTER).perform();
        }
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_RevisionTasa)).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_IndicadorVariacion)).sendKeys(Keys.ENTER);
        if (AssertionsAndChecks.Checks.isVisible(loansObjects.select_CustomerScreenConectLineCredit)) {
            wait.until(ExpectedConditions.visibilityOf(loansObjects.select_CustomerScreenConectLineCredit)).sendKeys("1");
            actions.sendKeys(Keys.ENTER).perform();
            //   crearLineaDeCredito();
        }
        //Cancelar línea de crédito
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOf(loansObjects.button_Cancel)).click();
    }

    public void selectAccount() throws InterruptedException {
        Thread.sleep(1000);
        if (AssertionsAndChecks.Checks.isVisible(loansObjects.Input_ComitionALIANZ)) {
            wait.until(ExpectedConditions.visibilityOf(loansObjects.button_PasarPorAlto)).click();
        }
        wait.until(ExpectedConditions.visibilityOf(loansObjects.Select_AccoutDest)).sendKeys("1", Keys.ENTER);
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.Input_OrdenReference)).sendKeys(code);
        wait.until(ExpectedConditions.visibilityOf(loansObjects.button_OK)).click();
        Navigator.waitForLoad();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.Input_DestinationNumAccount)).click();
        actions.sendKeys(Keys.F4).perform();
        Navigator.waitForLoad();
        try {
            List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath("//tr[contains(@class,'HATSTABLEODDROW')]/td[contains(@colspan,'2')]"));
            int countElements = elements.size();
            if (countElements > 0) {
                loansObjects.Select_AccoutDest.sendKeys("1");
                loansObjects.button_OK.click();
                loansObjects.Amount.clear();
                loansObjects.button_OK.click();
            } else {
                wait.until(ExpectedConditions.visibilityOf(loansObjects.button_OK)).click();
                wait.until(ExpectedConditions.visibilityOf(loansObjects.Input_PerfilAccountDest)).clear();
                wait.until(ExpectedConditions.visibilityOf(loansObjects.Input_PerfilAccountDest)).sendKeys("9");
                wait.until(ExpectedConditions.visibilityOf(loansObjects.Input_DestinationNumAccount)).click();
                actions.sendKeys(Keys.F4).perform();
                wait.until(ExpectedConditions.visibilityOf(loansObjects.Select_AccoutDest)).click();
                wait.until(ExpectedConditions.visibilityOf(loansObjects.Select_AccoutDest)).sendKeys("1");
                wait.until(ExpectedConditions.visibilityOf(loansObjects.button_OK)).click();
                wait.until(ExpectedConditions.visibilityOf(loansObjects.Amount)).clear();
                wait.until(ExpectedConditions.visibilityOf(loansObjects.button_OK)).click();
            }
        } catch (Exception e) {
            log.info("El elemento no fue encontrado: {}", e.getMessage());
        }
        wait.until(ExpectedConditions.visibilityOf(loansObjects.button_OK)).click();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.button_NextClient)).click();
        if(AssertionsAndChecks.Checks.isVisible(loansObjects.input_SelectpromiPyme)){
            wait.until(ExpectedConditions.visibilityOf(loansObjects.button_Cancel)).click();
            wait.until(ExpectedConditions.visibilityOf(loansObjects.button_Cancel)).click();
            return;
        }
        wait.until(ExpectedConditions.visibilityOf(loansObjects.button_NextClient)).click();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.button_Cancel)).click();
    }

    public void SearchLoans() {
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_SearchLoans)).clear();
        loansObjects.input_SearchLoans.sendKeys(NumProductLoans, Keys.ENTER);
        NumProductLoansCreated = loansObjects.Text_NumLoansCreated.getText().trim();
        if (NumProductLoansCreated.equals(NumProductLoans)) {
            ExtentReportManager.captureScreenshot("Validar que el número de producto es el mismo: " + NumProductLoans + " y el monto: " + amountNomina);
            loginAC.backToMainMenu();
        } else {
            ExtentReportManager.captureScreenshot("El número de préstamo no es igual al creado.");
            loginAC.backToMainMenu();
        }
    }

    public void createCreditLine() throws InterruptedException {
        if (AssertionsAndChecks.Checks.isVisible(loansObjects.button_nextStep)) {
            wait.until(ExpectedConditions.visibilityOf(loansObjects.button_nextStep)).click();
        }
        ExtentReportManager.captureScreenshot("Pantalla para crear línea de crédito.");
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_typelinea)).sendKeys(TypeLineCredit);
        NumLineCredit = loansObjects.Text_NumLinecredit.getText().trim();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_AmountLine)).sendKeys(amountLineCredit, Keys.ENTER);
        //fecha
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_FechaRevition)).clear();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_FechaRevition)).sendKeys(RevisionDate);
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_WarningMaturityClient)).clear();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_WarningMaturityClient)).sendKeys(WarningMaturityClient);
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_warningMaturityOfficial)).clear();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_warningMaturityOfficial)).sendKeys(WarningMaturityOfficial);
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_LimitWarningSopresa)).clear();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_LimitWarningSopresa)).sendKeys(LimitWarningSurprise);
        ExtentReportManager.captureScreenshot("Se rellenan los campos con las informaciones" + NumProductLoans);
        actions.sendKeys(Keys.ENTER).perform();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_commmitted)).clear();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_commmitted)).sendKeys(Committed, Keys.ENTER);
        //añadir sublímites
        if (AssertionsAndChecks.Checks.isVisible(loansObjects.select_warranty)) {
            wait.until(ExpectedConditions.visibilityOf(loansObjects.select_warranty)).sendKeys("1", Keys.ENTER);
            String input_subLimits = "0";
            wait.until(ExpectedConditions.visibilityOf(loansObjects.input_sublimite)).sendKeys(input_subLimits);
            actions.sendKeys(Keys.ENTER).perform();
            Thread.sleep(1000);
            // wait.until(ExpectedConditions.visibilityOf(loansObjects.button_OK)).click();
            wait.until(ExpectedConditions.elementToBeClickable(loansObjects.button_Cancel)).click();
            actions.sendKeys(Keys.F3).perform();
            Thread.sleep(1000);
            actions.sendKeys(Keys.F3).perform();
        }
    }

    public void searchCreditLine() throws InterruptedException {
        Thread.sleep(1000);
        List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath("//table[@class='HATSTABLE']//tbody//tr[2]"));
        int countElements = elements.size();
        if (countElements > 0) {
            for (WebElement fila : elements) {
                List<WebElement> cells = fila.findElements(By.xpath("//td[contains(@class,'HBROWN HF')][1]"));
                for (WebElement cedula : cells) {
                    String getCreditLine = cedula.getText().trim();
                    if (getCreditLine.equals(NumLineCredit)) {
                        ExtentReportManager.captureScreenshot("Línea de crédito encontrada." + NumLineCredit);
                        try {
                            if (AssertionsAndChecks.Checks.isVisible(loginPage.getBackButton())) {
                                loginAC.backToMainMenu();
                                wait.until(ExpectedConditions.visibilityOf(loginPage.getBackButton())).click();
                            } else if (!AssertionsAndChecks.Checks.isVisible(loginPage.getBackButton())) {
                                Actions action = new Actions(Browser.getWebDriver());
                                action.sendKeys(Keys.F3).perform();
                            }
                        } catch (Exception e) {
                            log.info("No se pudo escribir en la celda 2: " + e.getMessage());
                        }
                        break; // se encontró la cédula, no es necesario seguir iterando
                    }
                }
                break;
            }
        } else {
            log.info("No se encontraron filas en la tabla.");
        }
    }

    public void CancelLoan() throws InterruptedException {
        loansObjects.input_NumLoansToCancel.sendKeys(NumProductLoans);
        loansObjects.input_DescriptionCancelLoan.sendKeys("Automatización cancelación");
        ExtentReportManager.captureScreenshot("Se ingresa el número de producto y descripción: " + NumProductLoans);
        loansObjects.button_OK.click();
        String Date = BDactions.getAffectiveDate();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_Dateaffective)).sendKeys(Date);
        ExtentReportManager.captureScreenshot("Se rellena el formulario.: ");
        loansObjects.button_OK.click();
        Thread.sleep(1000);
        loansObjects.button_OK.click();
        loansObjects.button_OK.click();
        Thread.sleep(1000);
        if (loansObjects.Text_loanCancel.isDisplayed()) {
            log.info("Se canceló el préstamo");
            ExtentReportManager.captureScreenshot("Se cancela el préstamo: ");
        } else {
            log.info("No se logró cancelar el préstamo");
        }

    }

    public void renewLoans(List<String> NumLoans, String AffectiveDate) throws InterruptedException {
        String NewDate = "02112069";
        String Interest = "21000";
        for (String NumAccount : NumLoans) {
            wait.until(ExpectedConditions.visibilityOf(loansObjects.input_NuLoandRenew)).sendKeys(NumAccount);
            wait.until(ExpectedConditions.visibilityOf(loansObjects.input_affectiveDateRenew)).sendKeys(AffectiveDate);
            ExtentReportManager.captureScreenshot("Insertar número de préstamos a renovar: " + NumAccount);
            wait.until(ExpectedConditions.visibilityOf(loansObjects.button_OK)).click();

            //Fill form renew
            wait.until(ExpectedConditions.visibilityOf(loansObjects.input_NewDate)).sendKeys(NewDate);
            wait.until(ExpectedConditions.visibilityOf(loansObjects.input_NewInterest)).sendKeys(Interest);
            ExtentReportManager.captureScreenshot("Rellenar el formulario para renovar: ");
            wait.until(ExpectedConditions.visibilityOf(loansObjects.button_OK)).click();
            wait.until(ExpectedConditions.visibilityOf(loansObjects.Button_Autorized)).click();
            wait.until(ExpectedConditions.visibilityOf(loansObjects.Button_CallConsult)).click();
            SelectValue.selectOptionByValue("12");
            ExtentReportManager.captureScreenshot("Buscar el historial de la renovación: ");
            searchLoansHistory();

        }

    }

    public void searchLoansHistory() throws InterruptedException {
        int maxAttempts = 5;
        int attempts = 0;
        boolean found = false;

        while (attempts < maxAttempts) {
            Thread.sleep(1000);
            List<WebElement> rows = Browser.getWebDriver().findElements(By.xpath("//table[@class='HATSTABLE']//tbody//tr"));
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.xpath("//td[contains(@class,'HGREEN HF')][1]"));
                List<WebElement> nonEmptyCells = cells.stream()
                        .filter(cell -> !cell.getText().trim().isEmpty())
                        .toList();

                for (WebElement cell : nonEmptyCells) {
                    String history = cell.getText().trim();

                    String Text_validate = "RENOV DE PRSTMO";

                    if (history.contains(Text_validate)) {
                        ExtentReportManager.captureScreenshot("Movimiento de renovación encontrado en el préstamo.");
                        return;
                    }
                }
                wait.until(ExpectedConditions.visibilityOf(loansObjects.Button_PageDown)).click();
                break;
            }
            attempts++;
        }
        if (!found) {
            log.info("No se encontró el movimiento de renovación después de 5 intentos.");
        }
    }
    public Map<String, Long> getTheMinimumAndMaximumLoanAmount (String typeProduct){
        ConsultAccountPage consultAccountPage = new ConsultAccountPage();
        wait.until(ExpectedConditions.visibilityOf(consultAccountPage.mainOperationInput)).sendKeys("002525", Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_typeProduct)).sendKeys(typeProduct, Keys.ENTER);
        WebElement typeProductPresent = Browser.getWebDriver().findElement(By.xpath("//tr[@class='HATSTABLEODDROW']//td[contains(@class, 'HBROWN') and contains(., '" + typeProduct + "')]"));
        AssertionsAndChecks.Checks.isVisible(typeProductPresent);
        wait.until(ExpectedConditions.visibilityOf(loansObjects.input_Opc_1)).sendKeys("1", Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOf(loansObjects.btn_enter_OK)).click();
        wait.until(ExpectedConditions.visibilityOf(loansObjects.btn_enter_OK)).click();
        ExtentReportManager.captureScreenshot("Se muestra el monto minimo y maximo del prestamo.");
        AssertionsAndChecks.Checks.isVisible(loansObjects.input_MinimumLoanAmount);

        String MinimumLoanAmount_raw = loansObjects.input_MinimumLoanAmount.getAttribute("value");
        String MaximumLoanAmount_raw = loansObjects.input_MaximumLoanAmount.getAttribute("value");
        String MinimumLoanAmount = MinimumLoanAmount_raw.replaceAll("\\s", "");
        String MaximumLoanAmount = MaximumLoanAmount_raw.replaceAll("\\s", "");

        Random random = new Random();

        Map<String, Long> valorArray = new HashMap<>();
        valorArray.put("minimo", (Long.parseLong(MinimumLoanAmount) + random.nextInt(1000) + 1) * 100L);
        valorArray.put("maximo", (Long.parseLong(MaximumLoanAmount) + random.nextInt(1000) + 1) * 100L);

        return valorArray;
    }
}
