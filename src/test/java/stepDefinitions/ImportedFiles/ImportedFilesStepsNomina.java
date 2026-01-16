package stepDefinitions.ImportedFiles;

import actions.ImportedFiles.ConsultImportedFilesActionsNomina;
import config.Browser;
import dataStorageModel.NominaEntry;
import dataStorageModel.PayrollInfoDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.remote.DriverCommand;
import utils.ExtentReportManager;
import utils.ExtractDATANomina;
import utils.Navigator;

import java.io.IOException;

import static utils.ExtentReportManager.logMessage;

public class ImportedFilesStepsNomina {
    private final ConsultImportedFilesActionsNomina actions = new ConsultImportedFilesActionsNomina();
    ExtractDATANomina ExtractDataNomina = ExtractDATANomina.getInstance();

    @Given("El usuario ingresa la ruta del archivo nomina local valido {string} y luego la ruta remota {string} donde se enviará al servidor")
    public void sendFileFromLocalToServer(String localFilePath, String remoteFolderPath) throws IOException {
        PayrollInfoDTO dto = ExtractDataNomina.extractNominaInformation(localFilePath);
        actions.sendFileToServer(remoteFolderPath, localFilePath);
        logMessage = actions.buildNominaSummaryMessage(dto);
        ExtentReportManager.logStep("Se cargo el archivo correctamente.", "pass");
        Navigator.waitForLoad();
    }
    @Then("Se valida la importación del archivo Nomina {string}, los reportes generados y las transacciones a cuentas del archivo {string}")
    public void validationOfImportedFilesNomina(String expectedStatusImport, String localFilePath) throws IOException, InterruptedException {
        actions.nominaTransactionManagement(expectedStatusImport, localFilePath);
        ExtentReportManager.logStep("Se valido el reporte y la cuenta..", "pass");

    }
    @Given("El usuario ingresa la ruta del archivo nomina local que generada rechazos {string} y luego la ruta remota {string} donde se enviará al servidor")
    public void sendRejectdNominaFileFromLocalToServer(String localFilePath, String remoteFolderPath) throws IOException {
        actions.sendFileToServer(remoteFolderPath, localFilePath);
        PayrollInfoDTO dto = ExtractDataNomina.extractNominaInformation(localFilePath);
        StringBuilder builder = new StringBuilder("Transacciones encontradas:\n");
        for (NominaEntry entry : dto.getPayrollInfo()) {
            builder.append(String.format(
                    "Cuenta: %s | Monto: %.2f | Empresa: %s | ID Empresa: %s | Tipo de Cuenta: %s | Fecha: %s\n",
                    entry.getAccounts(),
                    entry.getAmount(),
                    entry.getCompany(),
                    entry.getCompanyID(),
                    entry.getTypeAccount(),
                    entry.getDate()
            ));
        }
        logMessage = builder.toString();
    }
    @Then("Se valida la importación del archivo {string} Nomina Rechazado {string}")
    public void validationOfRejectedImportedFilesNomina(String localFilePath, String expectedStatusImport) throws IOException, InterruptedException {
        actions.nominaTransactionManagement(expectedStatusImport, localFilePath);
    }
}
