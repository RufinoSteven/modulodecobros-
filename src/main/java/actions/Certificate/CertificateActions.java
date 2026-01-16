package actions.Certificate;

import actions.Embargo.ProcessEmbargoActions;
import actions.dbActions.EntriesActions;
import org.openqa.selenium.JavascriptExecutor;
import pageObjects.Certificate.CertificateObjects;
import utils.ExtentReportManager;

import java.util.List;
import java.util.Objects;

import static config.Browser.getWebDriver;

public class CertificateActions {
    private final ProcessEmbargoActions processEmbargoActions = new ProcessEmbargoActions();
    private static final CertificateObjects certificatePage = new CertificateObjects();
    private static final EntriesActions BDactions = new EntriesActions();
    String Date = BDactions.getAffectiveDate();

    public void CancelCertificate(List<String> cuentas, String Process) throws InterruptedException {
        String codTransaccion;
        if (Process.equals("Renovacion")) {
            codTransaccion = "C81N";
        } else {
            codTransaccion = "C92N";
        }
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        String Description = "Cancelado por automatizaciòn.";
        double Amount2;
        for (String NumAccount : cuentas) {
            // Ingresar número de cuenta
            js.executeScript(
                    "const input = document.getElementById('in_567_12');" +
                            "input.value = arguments[0];" +
                            "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                            "input.dispatchEvent(new Event('change', { bubbles: true }));",
                    NumAccount
            );

            // Ingresar código de transacción
            js.executeScript(
                    "const input = document.getElementById('in_699_4');" +
                            "input.value = arguments[0];" +
                            "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                            "input.dispatchEvent(new Event('change', { bubbles: true }));",
                    codTransaccion
            );
            // Click en el botón OK
            js.executeScript("document.getElementById('OKButton').click();");

            // Esperar a que se cargue respuesta (puedes usar WebDriverWait idealmente)
            Thread.sleep(1000); // o usar WebDriverWait para esperar visibilidad
            Boolean isVisible = (Boolean) js.executeScript(
                    "var el = document.querySelector(\"input[value='No se pudo encontrar la cuenta.']\");" +
                            "return el !== null && el.offsetParent !== null;"
            );
            if (Boolean.FALSE.equals(isVisible)) {
                ExtentReportManager.captureScreenshot("Se pudo consultar la cuenta: " + NumAccount);
                Number amountCertificate = (Number) js.executeScript(
                        "var xpath = \"//td[@class='HCYAN HF'][@colspan='22']\";" +
                                "var result = document.evaluate(xpath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null);" +
                                "var node = result.singleNodeValue;" +
                                "if (node) {" +
                                "    var text = node.textContent.trim();" +
                                "    var cleaned = text.replace(/,/g, '');" +
                                "    return parseFloat(cleaned);" +
                                "} else {" +
                                "    return null;" +
                                "}"
                );
                if (Process.equals("Renovacion")) {
                    String tasa = "0180000";
                    String DescriptionRenew = "Renew process automation.";
                    js.executeScript(
                            "const input = document.getElementById('in_1749_7');" +
                                    "input.value = arguments[0];" +
                                    "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                                    "input.dispatchEvent(new Event('change', { bubbles: true }));",
                            tasa
                    );

                    js.executeScript(
                            "const input = document.getElementById('in_1881_30');" +
                                    "input.value = arguments[0];" +
                                    "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                                    "input.dispatchEvent(new Event('change', { bubbles: true }));",
                            DescriptionRenew
                    );
                    js.executeScript("document.getElementById('OKButton').click();");
                    // Verificar actualización
                    Thread.sleep(1000);
                    js.executeScript(
                            "const input = document.getElementById('in_567_12');" +
                                    "input.value = arguments[0];" +
                                    "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                                    "input.dispatchEvent(new Event('change', { bubbles: true }));",
                            NumAccount
                    );

                    // Ingresar código de transacción
                    js.executeScript(
                            "const input = document.getElementById('in_699_4');" +
                                    "input.value = arguments[0];" +
                                    "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                                    "input.dispatchEvent(new Event('change', { bubbles: true }));",
                            codTransaccion
                    );
                    // Click en el botón OK
                    js.executeScript("document.getElementById('OKButton').click();");

                    // Obtener tasa
                    String getTasa = (String) js.executeScript(
                            "var xpath = \"//td//td[@class='HCYAN HF'][@colspan='8']\";" +
                                    "var result = document.evaluate(xpath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null);" +
                                    "var node = result.singleNodeValue;" +
                                    "return node ? node.textContent.trim() : null;"
                    );
                    if (Objects.equals(getTasa, "18.0000")) {
                        ExtentReportManager.captureScreenshot("Se renovo el certificado.: " + NumAccount);
                    } else {
                        ExtentReportManager.captureScreenshot("No se logro renovar el certificado.: " + NumAccount);
                    }
                } else {
                    if (amountCertificate != null && amountCertificate.doubleValue() <= 0) {
                        ExtentReportManager.captureScreenshot("Este certificado esta cancelado: " + NumAccount);
                    } else {
                        js.executeScript(
                                "const input = document.getElementById('in_2409_30');" +
                                        "input.value = arguments[0];" +
                                        "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                                        "input.dispatchEvent(new Event('change', { bubbles: true }));",
                                Description

                        );
                        ExtentReportManager.captureScreenshot("Pantalla para cancelar certificado: " + NumAccount);
                        js.executeScript("document.getElementById('OKButton').click();");
                        try {
                            // Consultar la cuenta nuevamente para validar.
                            js.executeScript(
                                    "const input = document.getElementById('in_567_12');" +
                                            "input.value = arguments[0];" +
                                            "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                                            "input.dispatchEvent(new Event('change', { bubbles: true }));",
                                    NumAccount
                            );

                            // Insertar código de transacción
                            js.executeScript(
                                    "const input = document.getElementById('in_699_4');" +
                                            "input.value = arguments[0];" +
                                            "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                                            "input.dispatchEvent(new Event('change', { bubbles: true }));",
                                    codTransaccion
                            );
                            js.executeScript("document.getElementById('OKButton').click();");
                            if (amountCertificate != null && amountCertificate.doubleValue() <= 0) {
                                ExtentReportManager.captureScreenshot("Este certificado esta cancelado: " + NumAccount);
                            }
                            ExtentReportManager.captureScreenshot("Cuenta cancelada.: " + NumAccount);
                        } catch (Exception e) {
                            ExtentReportManager.captureScreenshot("Hubo un error al ingreesar al certificado cancelado.: " + NumAccount);
                            ExtentReportManager.logStep("Error", "fail");
                        }
                        break; // Salir si la cuenta fue válida
                    }
                }
            } else {
                ExtentReportManager.captureScreenshot("No se logro  consultar el certificado.: " + NumAccount);
                ExtentReportManager.logStep("Error", "fail");
            }
        }
    }
}
