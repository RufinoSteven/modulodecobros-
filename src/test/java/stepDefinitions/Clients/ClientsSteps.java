package stepDefinitions.Clients;

import actions.Clients.ClientsActions;
import actions.ImportedFiles.ConsultImportedFilesActions;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Accounts.ClientsPage;

import java.util.Map;

import static actions.ImportedFiles.ConsultImportedFilesActions.log;
import static config.Browser.wait;
import static utils.ExtentReportManager.logMessage;


public class ClientsSteps {
    private final ClientsActions clientsActions = new ClientsActions();
    private final ConsultImportedFilesActions actions = new ConsultImportedFilesActions();
    private final ClientsPage clientsPage = new ClientsPage();


    @Then("Se ingresa a la transacción de clientes")
    public void createNewCliente() {
        clientsActions.accessTo_CIF_Transaction();
        clientsActions.accessTo_901020_transaction();
    }

    @And("Se busca la cédula del cliente {string}")
    public void findClient(String id) {
        clientsActions.findClientToCreateIt(id);
    }

    @When("Se completan los formularios del cliente con los siguientes datos:")
    public void completeClientForms(DataTable dataTable) {
        Map<String, String> datos = dataTable.asMap(String.class, String.class);
        clientsActions.completeCustomerShortName(datos.get("Nombre corto"));
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputClientName)).sendKeys(datos.get("Nombre"));
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputTitleClient)).sendKeys(datos.get("Título"));
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputMiddleName)).sendKeys(datos.get("Segundo nombre"));
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputLastName)).sendKeys(datos.get("Apellido"));
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputSecondLastName)).sendKeys(datos.get("Segundo apellido"));
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputApartmentNumber)).sendKeys(datos.get("Apartamento"));
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputHouseName)).sendKeys(datos.get("Nombre casa"));
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputHouseNumber)).sendKeys(datos.get("Número casa"));
        wait.until(ExpectedConditions.visibilityOf(clientsPage.inputStreet)).sendKeys(datos.get("Calle"));
        clientsActions.completeAddressInfo(datos.get("Sector"), datos.get("Tipo dirección"), datos.get("Vive aquí desde"), datos.get("Confirmar fecha"));
        clientsActions.completeEmploymentInfo(datos.get("Post"), datos.get("Patrón"), datos.get("Dirección 1"), datos.get("Dirección 2"), datos.get("Código postal trabajo"), datos.get("Tipo dirección"), datos.get("SIC Code"),
                datos.get("Email"), datos.get("Teléfono"), datos.get("Extensión trabajo"));
        clientsActions.completeWorkExperience(datos.get("Ingresos"), datos.get("Fecha inicio"), datos.get("Años empleo"), datos.get("Número nómina"), datos.get("Código profesión"));
        clientsActions.completeClienteTaxInfo(datos.get("Número seguro social"), datos.get("Número identificación"), datos.get("Clasificación cliente"), datos.get("Impuesto extranjero"), datos.get("Número sucursal"), datos.get("Oficial principal"), datos.get("Segmento mercado"), datos.get("Fecha nacimiento"), datos.get("Género"), datos.get("Estado civil"), datos.get("Nombre corto"));
    }

    @Then("Se ingresa a la transacción {} para buscar el cliente y crearle una nueva cuenta")
    public void findClientToCreateNewAccount(String transaction) {
        clientsActions.accessToCreateNewAccountTransaction(transaction);
    }

    @And("Se ingresan las informaciones de la cuenta a crear para el cliente")
    public void completeAccountInfo(DataTable dataTable) {
        Map<String, String> datos = dataTable.asMap(String.class, String.class);
        clientsActions.findClientForCreateAccount(datos.get("Número identificación"), datos.get("Nombre corto"));
        String accountNumber = clientsActions.completeAccountInfo(datos.get("Tipo de cuenta"), datos.get("Código producto"), datos.get("Código sucursal cuenta"), datos.get("Código oficina cuenta"), datos.get("Título cuenta"), datos.get("Código moneda"));
        String cardNumber = clientsActions.completeCardInfo(datos.get("¿Tendrá tarjeta?"), datos.get("Número tarjetas a generar"), datos.get("Fecha vencimiento tarjeta"), datos.get("Fecha próxima revisión"), datos.get("Fecha emisión próximo PIN"));
        actions.validateAccountTransaction(accountNumber, "0", "Consulta");
        String message = String.format("Cuenta creada: %s", accountNumber);
        if (!cardNumber.isEmpty()) {
            message += String.format(" | Tarjeta creada: %s", cardNumber);
        }
        logMessage = message;
        log.info(logMessage);
    }

    @Then("Se ingresa a la transacción de depósito a plazo")
    public void accessToCreteNewTermDepositTransaction() {
        clientsActions.accessTo_301090_transaction();
    }


    @And("Se ingresan las informaciones para crear un depósito a plazos")
    public void createTermDeposit(DataTable dataTable) {
        Map<String, String> datos = dataTable.asMap(String.class, String.class);
        String depositTermNumber = clientsActions.completeAccountInfoTermDeposit(datos.get("Número identificación"), datos.get("Tipo de la cuenta nueva"), datos.get("Número sucursal"), datos.get("Issue Amount"));
        clientsActions.completeRenewalInfo(datos.get("Opción renovación"), datos.get("Período de renovación"), datos.get("Día específico de renovación"), datos.get("Código disposición"), datos.get("Tipo cuenta destino"));
        clientsActions.completeCreteDestinyOrderTransfer(datos.get("Referencia de la orden"), datos.get("Monto depósito a plazo"), datos.get("Código de moneda"), depositTermNumber);
        logMessage = String.format("Numero de deposito a plazo creado: %s", depositTermNumber);

    }
}
