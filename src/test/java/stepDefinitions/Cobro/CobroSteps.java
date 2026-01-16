package stepDefinitions.Cobro;

import actions.Cobros.CobroActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import utils.ExtentReportManager;

import java.util.Map;

public class CobroSteps {

    private final CobroActions cobroActions = new CobroActions();

    @Given("el cliente posee {string} {string}")
    public void elClientePoseeComisionesPendientes(String pendingItemType, String statusItem) {
        int pendingItem = cobroActions.determinePendingItemType(pendingItemType);
        int status = cobroActions.determineStatusItem(statusItem);
        boolean clienteEncontrado = cobroActions.getClientWithPendingCommissions(pendingItem, status);
        Assertions.assertTrue(clienteEncontrado, "No se encontró un cliente con " + pendingItemType + " " + statusItem);

        ExtentReportManager.logStep("Cliente con " + pendingItemType + " " + statusItem + " encontrado con la cuenta: " + cobroActions.getAccountNumber()
                        + "<br><br><b>--------- INFORMACIÓN DEL COBRO PENDIENTE ANTES DEL PAGO ---------</b>"
                        + "<br><b>Fecha Original de Cobro:</b> " + cobroActions.getCollectionOriginalDate()
                        + "<br><b>Monto Original:</b> " + cobroActions.getOriginalAmountCollected()
                        + "<br><b>Monto Cobrado:</b> " + cobroActions.getInitialAmountCollected()
                        + "<br><b>Monto Pendiente:</b> " + cobroActions.getInitialPendingAmount()
                        + "<br><b>Referencia:</b> " + cobroActions.getCollectionId()
                        + "<br><b>Descripción:</b> " + cobroActions.getCollectionDescription()
                        + "<br><b>Estado:</b> " + cobroActions.getTargetStatusItem()
                , "pass");
    }

    @And("la cuenta no posee balance actual")
    public void laCuentaNoPoseeBalanceActual() {
        Assertions.assertTrue(cobroActions.verifyAccountWithoutBalance(), "La cuenta " + cobroActions.getAccountNumber() + " tiene balance disponible");
        ExtentReportManager.logStep("Cuenta sin balance verificada: "+ cobroActions.getAccountNumber(), "pass");
    }

    @When("la cuenta recibe un {string} cuyo Monto Transacción es {string} al Monto Pendiente")
    public void laCuentaRecibeUnaTransaccionCuyoMontoEsIgualOMayorOMenorAlMontoPendiente(String tipoTransaccion, String comparacionMonto) {
        // Establecer los parámetros de la transacción
        cobroActions.setTransactionParameters(tipoTransaccion, comparacionMonto);

        // Realizar la transacción según los parámetros
        Assertions.assertTrue(cobroActions.performAccountTransaction(), "No se pudo realizar la " + tipoTransaccion + " a la cuenta");
        ExtentReportManager.logStep(tipoTransaccion +" realizado y posteado a la cuenta: "+ cobroActions.getAccountNumber(), "pass");
    }

    @And("el código de transacción es adecuado para el tipo de cuenta")
    public void elCodigoDeTransaccionEsAdecuadoParaElTipoDeCuenta() {
        Assertions.assertTrue(cobroActions.determineTransactionCode(), "No se pudo determinar el código de transacción adecuado");
        ExtentReportManager.logStep("Código de transacción determinado: "+cobroActions.getTransactionCode(), "info");
    }

    @Then("se activa el cobro en línea")
    public void seActivaElCobroEnLinea() {
        // Esta validación es implícita, ya que se valida con los resultados posteriores
        ExtentReportManager.logStep("Cobro en línea activado", "info");
    }

    @And("la cuenta recibe un débito por el monto aplicado")
    public void laCuentaRecibeUnDebitoPorElMontoPendiente() {
        Assertions.assertTrue(cobroActions.verifyAccountDebit(), "No se encontró un débito en la cuenta por el monto esperado");
        ExtentReportManager.logStep("Débito verificado en la cuenta: "+cobroActions.getAccountNumber()+" por el monto: "+cobroActions.getPendingAmount(), "pass");
    }

    @And("la descripción de la transacción posee el numero de referencia de la partida pendiente")
    public void laDescripcionDeLaTransaccionPoseeElNumeroDeReferenciaDeLaPartidaPendiente() {
        Assertions.assertTrue(cobroActions.verifyTransactionDescription(),
                "La descripción de la transacción no contiene la referencia de la partida pendiente");
        ExtentReportManager.logStep("Descripción de transacción verificada correctamente con la referencia de partida pendiente para la cuenta: " + cobroActions.getAccountNumber(), "pass");
    }

    @And("el monto pendiente queda en {int} en el módulo de cobros")
    public void elMontoPendienteQuedaEnEnElModuloDeCobros(int montoPendienteEsperado) {
        Assertions.assertTrue(cobroActions.verifyPendingAmount(montoPendienteEsperado), "El monto pendiente no quedó en cero");
        ExtentReportManager.logStep("Monto pendiente verificado en cero para la cuenta: "+cobroActions.getAccountNumber(), "pass");
    }

    @And("el monto pendiente disminuye en el módulo de cobros")
    public void elMontoPendienteDisminuyeEnElModuloDeCobros() {
        Assertions.assertTrue(cobroActions.verifyPendingAmountDecreased(),"El monto pendiente no disminuyó como se esperaba");
        ExtentReportManager.logStep("Monto pendiente verificado que disminuyó para la cuenta: " + cobroActions.getAccountNumber() +
                " de " + cobroActions.getInitialPendingAmount() + " a un valor menor pero mayor que cero", "pass");
    }

    @And("el campo monto cobrado coincide con el monto del débito a la cuenta")
    public void elCampoMontoCobradoCoincideConElMontoDelDebitoALaCuenta() {
        Assertions.assertTrue(cobroActions.verifyCollectedAmount(), "El monto cobrado no coincide con el monto del débito");
        ExtentReportManager.logStep("Monto cobrado verificado correctamente para la cuenta: "+cobroActions.getAccountNumber(), "pass");
    }

    @And("el estado de la partida queda en {string}")
    public void elEstadoDeLaPartidaQuedaEn(String estadoEsperado) {
        Assertions.assertTrue(cobroActions.verifyEntryStatus(estadoEsperado), "El estado de la partida no es el esperado");
        ExtentReportManager.logStep("Estado de partida verificado: "+estadoEsperado+ " para la cuenta: "+cobroActions.getAccountNumber(), "pass");

        Map<String, String> afterCollectionInfo = cobroActions.getUpdatedCollectionDetails();
        if (afterCollectionInfo != null) {
            ExtentReportManager.logStep("<b>--------- INFORMACIÓN DEL COBRO PENDIENTE DESPUÉS DEL PAGO ---------</b>"
                            + "<br><b>Fecha Original de Cobro:</b> " + cobroActions.getCollectionOriginalDate()
                            + "<br><b>Monto Original:</b> " + afterCollectionInfo.get("OriginalAmount")
                            + "<br><b>Monto Cobrado:</b> " + afterCollectionInfo.get("CollectedAmount")
                            + "<br><b>Monto Pendiente:</b> " + afterCollectionInfo.get("PendingAmount")
                            + "<br><b>Referencia:</b> " + cobroActions.getCollectionId()
                            + "<br><b>Descripción:</b> " + cobroActions.getCollectionDescription()
                            + "<br><b>Estado:</b> " + afterCollectionInfo.get("CollectionStatus")
                    , "pass");
        } else {
            ExtentReportManager.logStep("No se pudo obtener la información de la partida después del cobro.", "warn");
        }
    }

    @And("el pago vencido del prestamo se muestra en cero")
    public void elPagoVencidoSeMuestraEnCero(){
        boolean validationResult = cobroActions.validateLastPayLoansExpired(cobroActions.getCollectionId());
        Assertions.assertTrue(validationResult, "La validación del pago vencido del préstamo no fue exitosa para la cuenta: " + cobroActions.getAccountNumber());
        ExtentReportManager.logStep("Pago vencido del prestamo: "+validationResult, "pass");
    }

    @And("se muestran los detalles del préstamo antes de hacer el pago parcial")
    public void vistaPrestamoPrevioAlPagoParcial(){
        String amountExpiredLoans = cobroActions.validateOverdueLoanPaymen(cobroActions.getCollectionId(),false);
        Assertions.assertNotNull(amountExpiredLoans, "El monto del pago vencido no es el esperado");
        ExtentReportManager.logStep("Monto del pago vencido del prestamo previo al pago parcial: " +amountExpiredLoans, "pass");
    }

    @And("se muestra los detalles del préstamo tras el pago parcial")
    public void vistaPrestamoTrasPagoParcial(){
        String amountExpiredLoans = cobroActions.validateOverdueLoanPaymen(cobroActions.getCollectionId(),true);
        Assertions.assertNotNull(amountExpiredLoans, "El monto del pago vencido no es el esperado");
        ExtentReportManager.logStep("Monto del pago vencido del prestamo tras al pago parcial: " +amountExpiredLoans, "pass");
    }

    @And("se muestran los detalles del préstamo antes de hacer el pago en la plataforma de signature")
    public void vistaPrestamoPrevioAlPagoParcialSignature(){
        String amountExpiredLoans = cobroActions.validateOverdueLoanPaymen(cobroActions.getCollectionId(),false);
        Assertions.assertNotNull(amountExpiredLoans, "El monto del pago vencido no es el esperado");
        ExtentReportManager.logStep("Monto del pago vencido del prestamo previo al pago parcial: " +amountExpiredLoans, "pass");
    }

    @And("se muestran los detalles del préstamo y se prepara el pago")
    public void seMuestranLosDetallesDelPrestamoYSePreparaElPago(){
        // Este paso realiza la obtención de datos en la interfaz, compara los montos y registra el resultado.
        cobroActions.prepareAndLogLoanDetails(false);
    }

}
