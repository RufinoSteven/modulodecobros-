package stepDefinitions.ImportedFiles;

import actions.Batch.BatchActions;
import actions.ImportedFiles.ConsultImportedFilesActions;
import dataStorageModel.ACHData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.ACHExtractDATA;
import utils.ExtentReportManager;
import utils.ModifyFileAch;
import utils.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static utils.ExtentReportManager.getExtent;
import static utils.ExtentReportManager.logMessage;

public class ImportedFilesSteps {
    private final ConsultImportedFilesActions actions = new ConsultImportedFilesActions();
    ACHExtractDATA achExtractDATA = ACHExtractDATA.getInstance();
    private final BatchActions batchActions = new BatchActions();

    @Given("El usuario ingresa la ruta del archivo local {string} para el tipo de proceso {string} y luego la ruta remota {string} donde se enviará al servidor")
    public void sendFileFromLocalToServer(String expectedStatusImport, String typeProcess, String remoteFolderPath) throws IOException {
        String modifiedFilePath = ModifyFileAch.processAndModifyACHFile(typeProcess, expectedStatusImport);
        ACHData dto = achExtractDATA.extractACHInformation(modifiedFilePath, typeProcess);
        actions.sendFileToServer(remoteFolderPath, modifiedFilePath);
        List<String> cuentas = actions.getAccountsFromDto(dto);
        List<String> montos = actions.getAmountsFromDto(dto);
        List<String> cuentasConMontos = new ArrayList<>();
        for (int i = 0; i < Math.min(cuentas.size(), montos.size()); i++) {
            cuentasConMontos.add(cuentas.get(i) + " por el monto: " + montos.get(i));
        }
        ExtentReportManager.logStep(
                String.format("Las cuentas a acreditar son: %s", String.join(", ", cuentasConMontos)),
                "pass"
        );
        logMessage = String.format("La cuenta a debitar es: %s", dto.getAccountsDebited());

    }

    @Then("En los casos de {string} se valida la importación del archivo ACH {string}, los reportes generados en el SpoolFile {string} y las transacciones a cuentas del archivo")
    public void validationOfImportedFiles(String typeProcess, String expectedStatusImport,
                                          String spoolFileTransaction) throws IOException, InterruptedException {
        actions.achTransactionManagement(typeProcess, expectedStatusImport, spoolFileTransaction);
        ExtentReportManager.logStep("Al subir el archivo DBCRGEN los debitos y creditos fueron mostrados exitosamente", "pass");
    }

    @Given("El usuario ingresa la ruta del archivo local que generada {string} y luego la ruta remota {string} donde se enviará al servidor para el tipo de proceso {string}")
    public void sendRejectdFileFromLocalToServer(String expectedStatusImport, String remoteFolderPath, String typeProcess) throws IOException {
        String localFilePath = ModifyFileAch.getGeneratedACHFilePath(typeProcess, expectedStatusImport);
        actions.sendFileToServer(remoteFolderPath, localFilePath);
        ACHData dto = achExtractDATA.extractACHInformation(localFilePath, typeProcess);
        List<String> cuentas = actions.getAccountsFromDto(dto);
        List<String> montos = actions.getAmountsFromDto(dto);
        List<String> cuentasConMontos = new ArrayList<>();
        for (int i = 0; i < Math.min(cuentas.size(), montos.size()); i++) {
            cuentasConMontos.add(cuentas.get(i) + " por el monto: " + montos.get(i));
        }

        ExtentReportManager.logStep(
                String.format("Las cuentas a acreditar son: %s", String.join(", ", cuentasConMontos)),
                "pass"
        );
        logMessage = String.format("La cuenta a debitar es: %s", dto.getAccountsDebited());
    }

    @Then("En los casos de debito y credito se valida la importación del archivo en la funcion {string} y se confirma que el reporte generado tiene el estatus de cancelado en el SpoolFile")
    public void validationOfCancelImportedDCFiles(String transaction) {
        actions.dcCancelledTransaction(transaction);
        ExtentReportManager.logStep("La carga del archivo es cancelada porque es distinta a la de debitos y creditos", "pass");
    }

    @When("Se {string} del archivo ACH para el tipo de proceso {string}")
    public void inactiveAccountFromACHFile(String expectedStatusImport, String typeProcess) throws IOException {
        ModifyFileAch.processAndModifyACHFile(typeProcess, expectedStatusImport);
        String localFilePath = ModifyFileAch.getGeneratedACHFilePath(typeProcess, expectedStatusImport);
        ACHData dto = achExtractDATA.extractACHInformation(localFilePath, typeProcess);
        batchActions.selectEnvironment("QA");
        actions.deactivateAccounts(dto);
    }

    @Given("El usuario ingresa la ruta del archivo local con cuentas inactivas y luego la ruta remota {string} donde se enviará al servidor para el tipo de archivo {string}")
    public void sendFileWithInactiveAccountFromLocalToServer(String remoteFolderPath, String typeProcess) {
        String localFilePath = ModifyFileAch.getGeneratedACHFilePath(typeProcess, "cuentas inactivas");
        actions.sendFileToServer(remoteFolderPath, localFilePath);

    }

    @And("Se activan las cuentas del archivo ACH para el tipo de archivo {string}")
    public void activeAccountFromACHFile(String typeProcess) throws IOException {
        String localFilePath = ModifyFileAch.getGeneratedACHFilePath(typeProcess, "Activar cuentas");
        ACHData dto = achExtractDATA.extractACHInformation(localFilePath, typeProcess);
        actions.activateAccount(dto);
    }

    @And("Se validan los registros en la cuenta contable de las transacciones del archivo ACH para el tipo de proceso {string}")
    public void validationOfTheAccountungAccount(String typeProcess) throws IOException {
        ACHData dto = achExtractDATA.extractACHInformation(ModifyFileAch.getGeneratedACHFilePath(typeProcess, "Cuenta contable"), typeProcess);
        actions.consultAccountingAccount(dto);
    }

    @When("el usuario ingresa a la página de verificación de cuentas regionales")
    public void navigateRegionalAccountsPage() {
        WindowManager.openNewWindow();
        actions.navigateToRegionalAccountPage();
    }

    @Given("El usuario ingresa el archivo para el tipo de proceso {string} donde se encuentran las cuentas regionales a verificar")
    public void verifyRegionalAccounts(String typeProcess) throws IOException {
        String localFilePath = ModifyFileAch.getGeneratedACHFilePath(typeProcess, "Cuentas regionales");
        ACHData dto = achExtractDATA.extractACHInformation(localFilePath, typeProcess);
        actions.consultRegionalAccounts(dto);
        ExtentReportManager.logStep(String.format("Las cuentas regionales a consultar el formato son: %s",
                String.join(", ", actions.getRegionalAccountsFromDTO(dto))),"pass");
    }

    @Given("El usuario ingresa la ruta del archivo local valido {string} y luego la ruta remota {string} donde se enviará al servidor")
    public void sendFileFromLocalToServer(String localFilePath, String remoteFolderPath) throws IOException {
//        ACHData dto = achExtractDATA.extractACHInformation(localFilePath, "Tesoreria");
        actions.sendFileToServer(remoteFolderPath, localFilePath);
        ExtentReportManager.logStep("archivo cargado correctamente", "pass");
    }

}

