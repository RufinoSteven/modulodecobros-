package pageObjects.Reports;

import config.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AssertionsAndChecks.Checks;

import java.time.Duration;
import java.util.List;

import static config.Browser.wait;

public class ImportedFilesPages {
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 1]")
    public WebElement inputOption1;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 11]")
    public WebElement inputOption10;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 9]")
    public WebElement inputOption9;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 5]")
    public WebElement inputOption5;
    @FindBy(xpath = "//input[@name = 'in_2515_60']")
    public WebElement inputTransactions;
    @FindBy(xpath = "//input[@id = 'in_1607_73']")
    public WebElement inputParametersOrCommand;
    @FindBy(xpath = "//input[@id = 'in_437_10']")
    public WebElement inputSpooledFileUser;
    @FindBy(xpath = "//input[@id = 'in_837_10']")
    public WebElement inputSpooledFile;
    @FindBy(xpath = "//input[@value = 'Renovar']")
    public WebElement btnRenew;
    @FindBy(xpath = "//input[@value = 'Cancelar']")
    public WebElement btnCancel;
    @FindBy(xpath = "//input[@value = 'Salir']")
    public WebElement btnGoOut;
    @FindBy(xpath = "//input[@id = 'OKButton']")
    public WebElement btnOk;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), 'Exportación fracasó')]")
    public WebElement failedExport;
    @FindBy(xpath = "//td[contains(text(), 'Exportado')]")
    public WebElement exportedStatus;
    @FindBy(xpath = "//td[contains(text(), 'Importación')]")
    public WebElement failedimport;
    @FindBy(xpath = "//td[contains(text(), 'Cancelada')]")
    public WebElement cancelImport;
    @FindBy(xpath = "//td[contains(text(), 'Procesadas')]")
    public WebElement processedImport;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), 'Listo para exportar')]")
    public WebElement ReadyToImport;
    @FindBy(xpath = "//td[contains(text(), 'Validada')]")
    public WebElement validatedImport;
    @FindBy(xpath = "(//td[contains(text(), 'CTA')])[position() = 1]")
    public WebElement tdNameReport;
    @FindBy(xpath = "(//td[contains(text(), 'ACH005')])[position() = 1]")
    public WebElement tdNameReportAffiliatePayment;
    @FindBy(xpath = "(//td[contains(text(), 'CTA140PT')])[position() = 1]")
    public WebElement tdNameTransACH;
    @FindBy(xpath = "(//td[contains(text(), 'CTA062')])[position() = 1]")
    public WebElement tdNameTransDC;
    @FindBy(xpath = "(//td[contains(text(), 'CTA710R')])[position() = 1]")
    public WebElement tdNameDuplicateReportDebitosCreditos;
    @FindBy(xpath = "(//td[contains(text(), 'CTA062CLR0')])[position() = 1]")
    public WebElement tdNameReportDomiciliation;
    @FindBy(xpath = "(//td[contains(text(), 'PD0')])[position() = 1]")
    public WebElement tdNameReportReverseACH;
    @FindBy(xpath = "(//td[contains(text(), 'CTA1011')])[position() = 1]")
    public WebElement tdNameDuplicateReportReverseACH;
    @FindBy(xpath = "(//td[contains(text(), 'ACH0005')])[position() = 1]")
    public WebElement tdNameDuplicateReportAffiliatePayment;
    @FindBy(xpath = "//input[contains(translate(@value, ' ', ' '), 'Page Up')]")
    public WebElement btnPageUp;
    @FindBy(xpath = "//input[contains(translate(@value, ' ', ' '), 'Page Down')]")
    public WebElement btnPageDown;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), '************** Reporte sin registros *************')]")
    public WebElement tdReportWithoutRecords;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), 'Reporte sin Registros')]")
    public WebElement tdReportWithoutRecordsAndWithoutAsterisk;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), 'Cuenta Maestra no tiene fondos disponibles')]")
    public WebElement unfundedAccount;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), 'Proceso duplicado')]")
    public WebElement duplicateProcess;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), 'Proceso Duplicado')]")
    public WebElement duplicateProcessCapitalLetter;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), '*****       NO EXISTEN DATOS          *****')]")
    public WebElement noData;
    @FindBy(xpath = "//input[@value = 'Exit']")
    public WebElement btnExit;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), 'No se pudo encontrar la cuenta.')]")
    public WebElement tdAccountNotFound;
    @FindBy(xpath = "//td[contains(translate(text(), ' ', ' '), 'La información de la cuenta está restringida')]")
    public WebElement tdRestrictedAccount;
    @FindBy(xpath = "//input[@value = 'Account not found']")
    public WebElement inoutAccountNotFound;
    @FindBy(xpath = "//input[contains(translate(@value, ' ', ' '), 'Cuenta Corriente')]")
    public WebElement btnCurrentAccount;
    @FindBy(xpath = "//input[contains(translate(@value, ' ', ' '), 'Ver Cuenta Restringida Si Autorizado')]")
    public WebElement btnSeeRestrictedAccount;
    @FindBy(xpath = "//input[contains(translate(@value, ' ', ' '), 'Cuenta de Ahorros')]")
    public WebElement btnSavingsAccount;
    @FindBy(xpath = "//input[@id = 'in_417_10']")
    public WebElement inputAccount;
    @FindBy(xpath = "//input[@name = 'in_200_12']")
    public WebElement inputAccountingAccount;
    @FindBy(xpath = "//input[@id = 'in_833_10']")
    public WebElement inputAccountChangeStatus;
    @FindBy(xpath = "//input[@id = 'in_966_2']")
    public WebElement inputAccountCondition;
    @FindBy(xpath = "//h2[contains(text(), 'Generador y verificador de cuentas estandarizadas')]")
    public WebElement tdConfirmRegionalAccountPage;
    @FindBy(xpath = "//input[@id = 'txtCtaParaVerificar']")
    public WebElement inputRegionalAccount;
    @FindBy(xpath = "//button[@id = 'btVerificaNroCta']")
    public WebElement btnVerify;
    @FindBy(xpath = "//div[contains(text(), 'La cuenta estándar es válida.')]")
    public WebElement alertValidAccount;
    @FindBy(xpath = "//button[contains(text(), '¡Entendido!')]")
    public WebElement btnUnderstood;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 9]")
    public WebElement inputLastReport;
    @FindBy(xpath = "(//input[contains(@class, 'HATSINPUT')])[position() = 1]")
    public WebElement inputFirstReport;
    @FindBy(xpath = "//td[contains(text(), 'Cancelada')]")
    public WebElement cancelledText;
    @FindBy(xpath = "(//td[contains(@class, 'HGREEN HF')])[position() = 3]")
    public WebElement fileName;
    @FindBy(xpath = "//td[contains(text(), 'Cancelada')]")
    public WebElement inputNumberPage;
    @FindBy(xpath = "//input[@value=' OK ']")
    public WebElement Button_OK;

    private static final Logger log = LogManager.getLogger(ImportedFilesPages.class);
    Actions actions = new Actions(Browser.getWebDriver());

    /**
     * Retrieves a WebElement for the FileName element based on the provided file name.
     *
     * @param fileName The name of the file to search for.
     * @return The WebElement representing the FileName.
     */
    public WebElement getFileName(String fileName) {
        return Browser.getWebDriver().findElement(By.xpath("//td[contains(text(), '" + fileName + "')]"));
    }

    /**
     * Retrieves a WebElement for the rejectionMessage element based on the provided file name.
     *
     * @param rejectionMessage The name of the file to search for.
     * @return The WebElement representing the rejectionMessage.
     */
    public WebElement getRejectionMessage(String rejectionMessage) {
        return Browser.getWebDriver().findElement(By.xpath("//td[contains(translate(text(), ' ', ' '), '" + rejectionMessage + "')]"));
    }

    /**
     * Selects an option in a <select> dropdown by its value.
     *
     * @param value The value of the option to be selected.
     * @return The WebElement of the <select> dropdown element.
     */
    public WebElement selectOptionByValue(String value) {
        WebElement selectElement = Browser.getWebDriver().findElement(By.xpath("//select[@name = 'in_142_2']"));
        Select select = new Select(selectElement);
        select.selectByValue(value);
        return selectElement;
    }

    /**
     * Selects an option in a <select> dropdown by its value.
     *
     * @param value The value of the option to be selected.
     * @return The WebElement of the <select> dropdown element.
     */
    public WebElement selectCountryByValue(String value) {
        WebElement selectElement = Browser.getWebDriver().findElement(By.xpath("//select[@id = 'slPais']"));
        Select select = new Select(selectElement);
        select.selectByValue(value);
        return selectElement;
    }

    /**
     * Selects an option in a <select> dropdown by its value.
     *
     * @param value The value of the option to be selected.
     * @return The WebElement of the <select> dropdown element.
     */
    public WebElement selectBankByValue(String value) {
        WebElement selectElement = Browser.getWebDriver().findElement(By.xpath("//select[@id = 'selEntidad']"));
        Select select = new Select(selectElement);
        select.selectByValue(value);
        return selectElement;
    }

    /**
     * Retrieves a WebElement for the Account element based on the provided account number.
     *
     * @param account The account number to search for.
     * @return The WebElement representing the Account.
     */
    public WebElement getAccountsElement(String account) {
        return Browser.getWebDriver().findElement(By.xpath("//td[contains(text(), '" + account + "')]"));
    }

    /**
     * Retrieves a WebElement for the Amount element based on the provided amount.
     *
     * @param amount The amount to search for.
     * @return The WebElement representing the Amount.
     */
    public WebElement getAmountElement(String amount) {
        return Browser.getWebDriver().findElement(By.xpath("//td[contains(text(), '" + amount + "')]"));
    }

    /**
     * Returns the number of elements found for a given file name.
     *
     * @param fileName The name of the file to search for.
     * @return The number of elements matching the given file name.
     */
    public int numberElements(String fileName) {
        List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath("//td[contains(text(), '" + fileName + "')]"));
        return elements.size();
    }

    /**
     * Selects the last 5 report input fields and enters the value "5" into them.
     */
    public void selectLast5Report() {
        List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath("//input[contains(@class, 'HATSINPUT')]"));
        int countElements = elements.size();
        if (countElements >= 5) {
            for (int i = countElements - 5; i < countElements; i++) {
                Browser.getWebDriver().findElement(By.xpath("(//input[contains(@class, 'HATSINPUT')])[" + (i + 1) + "]")).sendKeys("5");
            }
        }
    }

    /**
     * Selects the last 4 report input fields and enters the value "5" into them.
     */
    public void selectLast4Report() {
        List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath("//input[contains(@class, 'HATSINPUT')]"));
        int countElements = elements.size();
        if (countElements >= 4) {
            for (int i = countElements - 4; i < countElements; i++) {
                Browser.getWebDriver().findElement(By.xpath("(//input[contains(@class, 'HATSINPUT')])[" + (i + 1) + "]")).sendKeys("5");
            }
        }
    }

    /**
     * Selects the last 2 report input fields and enters the value "5" into them.
     */
    public void selectLastXReport(int countReports) {
        List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath("//input[contains(@class, 'HATSINPUT')]"));
        int countElements = elements.size();
        if (countElements >= countReports) {
            for (int i = countElements - countReports; i < countElements; i++) {
                Browser.getWebDriver().findElement(By.xpath("(//input[contains(@class, 'HATSINPUT')])[" + (i + 1) + "]")).sendKeys("5");
            }
        }
    }

    /**
     * Selects the last report input fields and enters the value "5" into them.
     */
    public void selectLastReport() {
        inputLastReport.clear();
        inputLastReport.click();
        inputLastReport.sendKeys("5");
        inputLastReport.sendKeys(Keys.ENTER);
    }

    /**
     * Selects the first report input fields and enters the value "5" into them.
     */
    public void selectFirstReport() {


        boolean matched = false;
        int attempts = 0;
        int maxAttempts = 5;

        while (!matched && attempts < maxAttempts) {
            try {
                matched = wait.until(d -> "AHDOEV280725".equals(fileName.getText()));
                if (matched) {
                    inputFirstReport.clear();
                    inputFirstReport.click();
                    inputFirstReport.sendKeys("5");
                    inputFirstReport.sendKeys(Keys.ENTER);
                }
            } catch (Exception e) {
                wait.until(ExpectedConditions.visibilityOf(btnRenew)).click();
            }
            attempts++;
        }
    }

    /**
     * Selects the first 4 report input fields and enters the value "5" into them.
     */
    public void selectFirst4Report() {
        List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath("//input[contains(@class, 'HATSINPUT')]"));
        int countElements = elements.size();
        if (countElements >= 4) {
            for (int i = 0; i < 4; i++) {
                Browser.getWebDriver().findElement(By.xpath("(//input[contains(@class, 'HATSINPUT')])[" + (i + 1) + "]")).sendKeys("5");
            }
        }
    }

    /**
     * Selects the first 6 report input fields and enters the value "5" into them.
     */
    public void selectFirst6Report() {
        List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath("//input[contains(@class, 'HATSINPUT')]"));
        int countElements = elements.size();
        if (countElements >= 6) {
            for (int i = 0; i < 6; i++) {
                Browser.getWebDriver().findElement(By.xpath("(//input[contains(@class, 'HATSINPUT')])[" + (i + 1) + "]")).sendKeys("5");
            }
        }
    }

    /**
     * Retrieves the name of the report based on the provided file name.
     *
     * @param fileName The name of the file to search for.
     * @return The name of the report.
     */
    public String getReportName(String fileName) {
        return getFileName(fileName).getText();
    }

    /**
     * Retrieves the state of the report, checking various possible states.
     *
     * @return The state of the report, or a default message if no state is found.
     */
    public String stateOfReport() {
        String state = "No status found for this imported file";
        try {
            if (shortWait(processedImport)) {
                state = processedImport.getText();
            } else if (shortWait(exportedStatus)) {
                state = exportedStatus.getText();
            } else if (shortWait(cancelImport)) {
                state = cancelImport.getText();
            } else if (shortWait(failedimport)) {
                state = failedimport.getText();
            } else if (shortWait(failedExport)) {
                state = failedExport.getText();
            } else if (shortWait(ReadyToImport)) {
                state = ReadyToImport.getText();
            } else if (shortWait(validatedImport)) {
                state = validatedImport.getText();
            }
        } catch (TimeoutException e) {
            log.error("Timeout while waiting for status element: {}", e.getMessage());
        }
        return state;
    }

    /**
     * Checks if the given web element is visible within a short wait period.
     * The method does not interrupt the flow of the execution even if the element is not found.
     *
     * @param element The WebElement to be checked for visibility.
     * @return true if the element is visible within the wait period, false otherwise.
     */
    public boolean shortWait(WebElement element) {
        try {
            WebDriverWait shortWait = new WebDriverWait(Browser.getWebDriver(), Duration.ofMillis(500));
            shortWait.pollingEvery(Duration.ofMillis(50));
            shortWait.ignoring(NoSuchElementException.class);
            shortWait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Retrieves the name of the report.
     *
     * @return The name of the report or a message if not found.
     */
    public String nameOfReportAffiliatePayment() {
        return Checks.isVisible(tdNameReportAffiliatePayment) ? tdNameReportAffiliatePayment.getText() : null;
    }

    /**
     * Retrieves the name of the report.
     *
     * @return The name of the report or a message if not found.
     */
    public String nameOfTransACH() {
        return Checks.isVisible(tdNameTransACH) ? tdNameTransACH.getText() : null;
    }

    /**
     * Retrieves the name of the report.
     *
     * @return The name of the report or a message if not found.
     */
    public String nameOfTransDC() {
        return Checks.isVisible(tdNameTransDC) ? tdNameTransDC.getText() : null;
    }


    /**
     * Retrieves the name of the report.
     *
     * @return The name of the report or a message if not found.
     */
    public String nameOfDuplicateDCTrans() {
        return Checks.isVisible(tdNameDuplicateReportDebitosCreditos) ? tdNameDuplicateReportDebitosCreditos.getText() : null;
    }

    /**
     * Retrieves the name of the report.
     *
     * @return The name of the report or a message if not found.
     */
    public String nameOfReportDomiciliation() {
        return Checks.isVisible(tdNameReportDomiciliation) ? tdNameReportDomiciliation.getText() : null;
    }

    /**
     * Retrieves the name of the report.
     *
     * @return The name of the report or a message if not found.
     */
    public String nameOfReportReverseACH() {
        return Checks.isVisible(tdNameReportReverseACH) ? tdNameReportReverseACH.getText() : null;
    }

    /**
     * Retrieves the name of the report.
     *
     * @return The name of the report or a message if not found.
     */
    public String nameOfDuplicateReportReverseACH() {
        return Checks.isVisible(tdNameDuplicateReportReverseACH) ? tdNameDuplicateReportReverseACH.getText() : null;
    }

    /**
     * Retrieves the name of the report.
     *
     * @return The name of the report or a message if not found.
     */
    public String nameOfDuplicateReportAffiliatePayment() {
        return Checks.isVisible(tdNameDuplicateReportAffiliatePayment) ? tdNameDuplicateReportAffiliatePayment.getText() : null;
    }

    /**
     * Retrieves the name of the report.
     *
     * @return The name of the report or a message if not found.
     */
    public String nameOfReport() {
        return Checks.isVisible(tdNameReport) ? tdNameReport.getText() : null;
    }

    /**
     * Consults the total number of imported files for the given file name.
     */
    public void consultTotalNumberImportedFiles(String fileName, int countOfElements) {
        String xpath = "//td[normalize-space()='" + fileName + "']/preceding-sibling::td//input[contains(@class, 'HATSINPUT')]";
        List<WebElement> elements = Browser.getWebDriver().findElements(By.xpath(xpath));
        elements.stream().limit(countOfElements).forEach(e -> e.sendKeys("5"));
    }

    /**
     * Retrieves the WebElement for the debited account element based on the provided account number.
     *
     * @param accountDebited The account number of the debited account.
     * @return The WebElement representing the debited account.
     */
    public WebElement checkAccountDebited(String accountDebited) {
        return Browser.getWebDriver().findElement(By.xpath("//td[contains(text(), '" + accountDebited + "')]"));
    }

    /**
     * Simulates pressing Shift + F2 keys on the keyboard.
     */
    public void pressShiftAndF2() {
        actions.keyDown(Keys.SHIFT).sendKeys(Keys.F2).keyUp(Keys.SHIFT).perform();
    }

    /**
     * Simulates pressing Shift + F6 keys on the keyboard.
     */
    public void pressShiftAndF6() {
        try {
            Thread.sleep(3000);
            actions.keyDown(Keys.SHIFT).sendKeys(Keys.F6).keyUp(Keys.SHIFT).perform();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void pressF6() {
        try {
            Thread.sleep(3000);
            actions.sendKeys(Keys.F6).perform();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void pressF12() {
        try {
            Thread.sleep(3000);
            actions.sendKeys(Keys.F12).perform();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Simulates pressing Shift + F4 keys on the keyboard.
     */
    public void pressShiftAndF4() {
        try {
            Thread.sleep(3000);
            actions.keyDown(Keys.SHIFT).sendKeys(Keys.F4).keyUp(Keys.SHIFT).perform();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Simulates pressing F10 keys on the keyboard.
     */
    public void pressF10() {
        try {
            Thread.sleep(3000);
            actions.sendKeys(Keys.F10).perform();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Simulates pressing the Enter key on the keyboard.
     */
    public void pressEnter() {
        try {
            Thread.sleep(4000);
            actions.keyDown(Keys.ENTER).keyUp(Keys.ENTER).perform();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ImportedFilesPages() {
        PageFactory.initElements(Browser.getWebDriver(), this);
    }
}
