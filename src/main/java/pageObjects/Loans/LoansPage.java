package pageObjects.Loans;

import config.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoansPage {
    //input search document id
    @FindBy(xpath = "//tr[7]//td[@class='HCYAN HF']")
    public WebElement Text_NumLoans;

    @FindBy(xpath = "//tr[2]//td[@class='HCYAN HF']")
    public WebElement Text_NumLinecredit;

    @FindBy(xpath = "//tr[2]//td[@class='HCYAN HF']")
    public WebElement Text_NumLoansCreated;

    @FindBy(xpath = "//input[@id='in_193_20']")
    public WebElement setCustomer;

    @FindBy(xpath = "//input[@id='in_960_5']")
    public WebElement input_typeOfloans;

    @FindBy(xpath = "//input[@id='in_1092_10']")
    public WebElement input_distributors;

    @FindBy(xpath = "//input[@id='in_1224_5']")
    public WebElement input_NumOffice;

    @FindBy(xpath = "//input[@id='in_1488_1']")
    public WebElement input_SchemaLoans;

    @FindBy(xpath = "//input[@id='in_1884_8']")
    public WebElement input_NumOfPay;

    @FindBy(xpath = "//input[@id='in_2016_17']")
    public WebElement input_mountOfNomina;

    @FindBy(xpath = "//input[@id='in_2412_1']")
    public WebElement input_PayFrecuency;

    @FindBy(xpath = "//input[@id='in_2940_1']")
    public WebElement input_SU_VAR;

    @FindBy(xpath = "//input[@id='in_3072_17']")
    public WebElement AmountInmueble;

    @FindBy(xpath = "//input[@id='OKButton']")
    public WebElement button_OK;

    //work with relationship
    @FindBy(xpath = "//input[@id='in_1167_1']")
    public WebElement input_CIF;

    @FindBy(xpath = "//input[@id='in_1247_1']")
    public WebElement input_transacctions;


    //Screen add code loans
    @FindBy(xpath = "//input[@id='in_950_7']")
    public WebElement input_interestRate;

    @FindBy(xpath = "//input[@id='in_1338_1']")
    public WebElement input_warranty;

    @FindBy(xpath = "//input[@id='in_2550_7']")
    public WebElement input_loanValue;

    @FindBy(xpath = "//input[@id='in_1494_1']")
    public WebElement input_crèditoLine;

    @FindBy(xpath = "//input[@id='in_1734_1']")
    public WebElement input_aprobation;

    @FindBy(xpath = "//input[@id='in_1866_1']")
    public WebElement input_recursos;

    @FindBy(xpath = "//input[@id='in_1203_5']")
    public WebElement input_PlanLoans;

    //Sure Loans
    @FindBy(xpath = "//table[@class='HATSTABLE']")
    public WebElement table_Sureloans;

    @FindBy(xpath = "//input[@name='in_963_5']")
    public WebElement input_TypeSureLoans;

    @FindBy(xpath = "//input[@id='in_1623_30']")
    public WebElement input_NumPoliza;

    @FindBy(xpath = "//input[@id='in_1227_15']")
    public WebElement input_MinSalddo;

    @FindBy(xpath = "//input[@id='in_1755_17']")
    public WebElement input_fijaPrima;

    @FindBy(xpath = "//input[@id='in_1095_1']")
    public WebElement input_codeBilling;

    @FindBy(xpath = "//input[@id='in_1359_1']")
    public WebElement input_PeriodoPrima;

    @FindBy(xpath = "//input[@id='in_1491_3']")
    public WebElement input_FrecuencyPrima;

    @FindBy(xpath = "//input[@id='in_1323_2']")
    public WebElement input_OptionPoliza;

    //screen Information variable

    @FindBy(xpath = "//input[@id='in_1098_1']")
    public WebElement input_codigoTaza;

    @FindBy(xpath = "//input[@id='in_1098_1']")
    public WebElement input_RevisionTasa;

    @FindBy(xpath = "//input[@name='in_839_1']")
    public WebElement input_IndicadorVariacion;

    @FindBy(xpath = "//input[@type='button'][3]")
    public WebElement button_nextStep;

    @FindBy(xpath = "//input[contains(@value,'Cancelar')]")
    public WebElement button_Cancel;

    @FindBy(xpath = "//input[@id='in_1058_2']")
    public WebElement input_selectProgramLoans;

    @FindBy(xpath = "//input[@id='in_1623_12']")
    public WebElement input_NumAccountDeposit;

    @FindBy(xpath = "//input[@id='in_1227_7']")
    public WebElement input_perfilaccount;

    @FindBy(xpath = "//input[@type='button'][3]")
    public WebElement button_createwarranty;

    @FindBy(xpath = "//input[@name='button'][3]")
    public WebElement input_CUSIPNUM;

    @FindBy(xpath = "//input[@id='in_962_4")
    public WebElement input_CODIGOWARRANTY;

    @FindBy(xpath = "//input[@type='text'][@name='in_1322_2']")
    public WebElement select_warranty;

    @FindBy(xpath = "//input[@id='in_929_1']")
    public WebElement select_CustomerScreenConectLineCredit;

    @FindBy(xpath = "//input[@id='in_963_5']")
    public WebElement input_typelinea;

    @FindBy(xpath = "//input[@id='in_2262_1']")
    public WebElement input_OpCalSure;

    @FindBy(xpath = "//input[@id='in_1491_18']")
    public WebElement input_AmountLine;

    @FindBy(xpath = "//input[@id='in_2159_6']")
    public WebElement input_FechaRevition;

    @FindBy(xpath = "//input[@id='in_2291_3']")
    public WebElement input_WarningMaturityClient;

    @FindBy(xpath = "//input[@id='in_2423_3']")
    public WebElement input_warningMaturityOfficial;

    @FindBy(xpath = "//input[@name='in_2555_3']")
    public WebElement input_LimitWarningSopresa;

    @FindBy(xpath = "//input[@id='in_1367_1']")
    public WebElement input_commmitted;

    //posteo de transacciones de prestamos

    @FindBy(xpath = "//input[@id='in_2282_2']")
    public WebElement input_APL;

    @FindBy(xpath = "//input[@id='in_2291_12']")
    public WebElement input_DestinoAccount;

    @FindBy(xpath = "//input[@id='in_1058_2']")
    public WebElement Select_AccoutDest;

    @FindBy(xpath = "//input[@id='in_833_14']")
    public WebElement Input_ComitionALIANZ;

    @FindBy(xpath = "//input[@type='button'][3]")
    public WebElement button_PasarPorAlto;

    @FindBy(xpath = "//input[@type='button'][4]")
    public WebElement button_NextClient;

    @FindBy(xpath = "//input[@id='in_1484_18']")
    public WebElement Input_OrdenReference;

    //Destination Orden
    @FindBy(xpath = "//input[@id='in_1227_7']")
    public WebElement Input_PerfilAccountDest;

    @FindBy(xpath = "//input[@id='in_1623_12']")
    public WebElement Input_DestinationNumAccount;

    @FindBy(xpath = "//input[@id='in_1755_17']")
    public WebElement Amount;

    //screen search loans
    @FindBy(xpath = "//input[@id='in_413_12']")
    public WebElement input_SearchLoans;

    @FindBy(xpath = "//input[contains(@value,'Page Down')]")
    public WebElement button_PageDown;

    //screen loguot
    @FindBy(id = "in_1330_1")
    public WebElement pagelogout;

    //sublimite
    @FindBy(xpath = "//input[@id='in_1095_15']")
    public WebElement input_sublimite;

    //screenCancelLoans
    @FindBy(xpath = "//input[@id='in_827_12']")
    public WebElement input_NumLoansToCancel;

    @FindBy(xpath = "//input[@id='in_1091_25']")
    public WebElement input_DescriptionCancelLoan;

    @FindBy(xpath = "//input[@id='in_1494_6" +
            "']")
    public WebElement input_Dateaffective;

    @FindBy(xpath = "//input[@value='El préstamo no está activo.']")
    public WebElement Text_loanCancel;

    @FindBy(xpath = "//input[@id='in_1898_7']")
    public WebElement input_NewInterest;

    @FindBy(xpath = "//input[@id='in_2029_8']")
    public WebElement input_NewDate;

    @FindBy(xpath = "//input[@id='in_827_12']")
    public WebElement input_NuLoandRenew;

    @FindBy(xpath = "//input[@id='in_959_6']")
    public WebElement input_affectiveDateRenew;

    @FindBy(xpath = "//input[@value='Autorizar']")
    public WebElement Button_Autorized;

    @FindBy(xpath = "//input[@type='button'][2]")
    public WebElement Button_CallConsult;

    @FindBy(xpath = "//input[@value='Page Down']")
    public WebElement Button_PageDown;

    @FindBy(xpath = "//input[@name='in_331_5']")
    public WebElement input_typeProduct;

    @FindBy(xpath = "//input[@name='in_1586_2']")
    public  WebElement input_Opc_1;

    @FindBy(xpath = "//input[@name='in_2549_15']")
    public  WebElement input_MinimumLoanAmount;

    @FindBy(xpath = "//input[@name='in_2681_15']")
    public  WebElement input_MaximumLoanAmount;

    @FindBy(xpath = "//input[@name='[enter]']")
    public WebElement btn_enter_OK;

    @FindBy(xpath = "//input[@value='Salir']")
    public WebElement Button_Salir;

    @FindBy(xpath = "//input[@name='in_927_2']")
    public  WebElement input_SelectAgreements;

    @FindBy(xpath = "//input[@name='in_1059_2']")
    public  WebElement input_SelectNewAccount;

    @FindBy(xpath = "//input[@name='in_1059_2']")
    public  WebElement input_SelectpromiPyme;

    public LoansPage() {PageFactory.initElements(Browser.getWebDriver(), this);}
}