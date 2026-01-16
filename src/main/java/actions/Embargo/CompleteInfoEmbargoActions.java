package actions.Embargo;

import dataStorageModel.Embargo.EmbargoPageData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.Embargo.CompleteInfoEmbargoPage;
import utils.DataGenerator.EmbargoDataGenerator;

import java.util.Arrays;
import java.util.Random;

import static config.Browser.wait;

/**
 * Esta clase proporciona acciones que se pueden realizar en la página de completado de información de embargo.
 *
 * <p>Ejemplo de uso:</p>
 *
 * <pre>
 * CompleteInfoEmbargoActions embargoActions = new CompleteInfoEmbargoActions();
 * embargoActions.navigateToEmbargoReviewPage();
 * embargoActions.completeEmbargoInformation("12345", embargoData);
 * </pre>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */
public class CompleteInfoEmbargoActions {
    private final CompleteInfoEmbargoPage completeInfoEmbargoPage = new CompleteInfoEmbargoPage();
    private static final Logger log = LogManager.getLogger(CompleteInfoEmbargoActions.class);
    public String embargoAmount;

    public void navigateToEmbargoReviewPage() throws InterruptedException {
        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.transactionSelectionInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.transactionSelectionInput)).sendKeys("209460");
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.transactionSelectionInput)).sendKeys(Keys.ENTER);
    }

    public void positionOnEmbargo(String embargoNumber) throws InterruptedException {
        Thread.sleep(4000);
        WebElement input = completeInfoEmbargoPage.getPositionOperationInput();
        wait.until(ExpectedConditions.visibilityOf(input)).sendKeys(embargoNumber);
        wait.until(ExpectedConditions.visibilityOf(input)).sendKeys(Keys.ENTER);
    }

    public void selectConsultOperation(String operationString) {
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationConsultInput)).click();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationConsultInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationConsultInput)).sendKeys(operationString);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationConsultInput)).sendKeys(Keys.ENTER);
    }

    public void selectOption2() {
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.optionInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.optionInput)).sendKeys("2");
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.optionInput)).sendKeys(Keys.ENTER);
    }

    public String[] extractRequesterName() {
        String[] nameParts = wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorFullNameText)).getText().trim().split("\\s+");
        if (nameParts.length == 1) {
            return new String[]{nameParts[0], ""};
        } else {
            String firstName = nameParts[0];
            String lastName = String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length));
            return new String[]{firstName, lastName.substring(0, Math.min(lastName.length(), 20))};
        }
    }

    public void enterPreviousOperationNumber(String previousEmbargoNumber, String previousAmmount) {
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.previousOperationInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.previousOperationInput)).sendKeys(previousEmbargoNumber);
        String operationAmount = (previousAmmount.isEmpty()) ? EmbargoDataGenerator.generateRandomAmount() : previousAmmount;
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).sendKeys(operationAmount);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.previousOperationInput)).sendKeys(Keys.ENTER);
    }

    public void enterPreviousOperationCloseNumber(String previousEmbargoNumber){
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.previousOperationInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.previousOperationInput)).sendKeys(previousEmbargoNumber);
        wait.until(ExpectedConditions.elementToBeClickable(completeInfoEmbargoPage.okBtn)).click();
        WebElement refreshedValueElement = wait.until(
                ExpectedConditions.refreshed(
                        ExpectedConditions.visibilityOf(completeInfoEmbargoPage.valueApliedPreviusOperation)
                )
        );
        String previousOperationValue = refreshedValueElement.getText().trim();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).sendKeys(previousOperationValue);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).sendKeys(Keys.ENTER);
    }

    public void enterPreviousOperationLiftingNumber(String previousEmbargoNumber) {
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.previousOperationInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.previousOperationInput)).sendKeys(previousEmbargoNumber);
        wait.until(ExpectedConditions.elementToBeClickable(completeInfoEmbargoPage.okBtn)).click();
        WebElement refreshedValueElement = wait.until(
                ExpectedConditions.refreshed(
                        ExpectedConditions.visibilityOf(completeInfoEmbargoPage.valueApliedPreviusOperation)
                )
        );
        String previousOperationValue = refreshedValueElement.getText().trim();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).sendKeys(previousOperationValue);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).sendKeys(Keys.ENTER);
    }


    public EmbargoPageData buildEmbargoDataFromPage(String previousEmbargoNumber) {
        String[] nameArray = extractRequesterName();
        String requesterIdType = wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorIdTypeText)).getText().trim();
        String requesterIdNumber = wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorIdNumberText)).getText().trim();
        String randomAmount = EmbargoDataGenerator.generateRandomAmount();

        if (!(CreateEmbargoActions.lastCreatedEmbargoType.equals("6") || CreateEmbargoActions.lastCreatedEmbargoType.equals("8") || CreateEmbargoActions.lastCreatedEmbargoType.equals("4")  || CreateEmbargoActions.lastCreatedEmbargoType.equals("3"))) {
            embargoAmount = randomAmount;
            log.info("Monto del Embargo Creado: {}", randomAmount);
        }

        String insuranceCompany = null;
        String policyCode = null;

        if (CreateEmbargoActions.lastCreatedEmbargoType.equals("6")) {
            insuranceCompany = EmbargoDataGenerator.generateInsuranceCompany();
            policyCode = EmbargoDataGenerator.generatePolicyCode();
        }

        return EmbargoPageData.builder()
                .actNumber(String.valueOf(100000 + new Random().nextInt(900000)))
                .requesterIdType(requesterIdType)
                .requesterIdNumber(requesterIdNumber)
                .requesterFirstName(nameArray[0])
                .requesterLastName(nameArray[1])
                .requesterSocialReason(requesterIdType.equals("3") ? EmbargoDataGenerator.generateSocialReasons() : null)
                .attorneyIdType("6")
                .attorneyIdNumber(EmbargoDataGenerator.generateDominicanId())
                .attorneyFirstName(EmbargoDataGenerator.generateRandomFirstName())
                .attorneyLastName(EmbargoDataGenerator.generateRandomLastName())
                .operationAmount(randomAmount)
                .previousOperationNumber(previousEmbargoNumber)
                .insuranceCompany(insuranceCompany)
                .policyCode(policyCode)
                .build();
    }

    public void completeInformationForm(EmbargoPageData embargoData) {

        //Número de Acta
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.actNumberInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.actNumberInput)).sendKeys(embargoData.getActNumber());

        //Datos del Solicitante del Embargo
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorIdTypeInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorIdTypeInput)).sendKeys(embargoData.getRequesterIdType());

        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorIdNumberInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorIdNumberInput)).sendKeys(embargoData.getRequesterIdNumber());

        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorFirstNameInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorFirstNameInput)).sendKeys(embargoData.getRequesterFirstName());

        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorLastNameInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorLastNameInput)).sendKeys(embargoData.getRequesterLastName());

        // Completar la razón social solo si el tipo de ID es 3 (RNC) o si se proporciona explícitamente
        if (embargoData.getRequesterIdType().equals("3") || embargoData.getRequesterSocialReason() != null) {
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.okBtn)).click();
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorSocialReasonInput)).clear();
            String socialReason = embargoData.getRequesterSocialReason() != null ? embargoData.getRequesterSocialReason() :
                    embargoData.getRequesterFirstName() + " " + embargoData.getRequesterLastName() + " SRL";
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.requestorSocialReasonInput)).sendKeys(socialReason);
        }

        String previousOperationNumber = embargoData.getPreviousOperationNumber();
        if (!CreateEmbargoActions.lastCreatedEmbargoType.equals("2")) {
            if (previousOperationNumber != null) {
                wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.previousOperationInput)).clear();
                wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.previousOperationInput)).sendKeys(previousOperationNumber);
            }
        }

        //Datos del Abogado
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.attorneyIdTypeInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.attorneyIdTypeInput)).sendKeys(embargoData.getAttorneyIdType());

        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.attorneyIdNumberInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.attorneyIdNumberInput)).sendKeys(embargoData.getAttorneyIdNumber());

        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.attorneyFirstNameInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.attorneyFirstNameInput)).sendKeys(embargoData.getAttorneyFirstName());

        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.attorneyLastNameInput)).clear();
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.attorneyLastNameInput)).sendKeys(embargoData.getAttorneyLastName());

        //Monto
        if (CreateEmbargoActions.lastCreatedEmbargoType.equals("8") || CreateEmbargoActions.lastCreatedEmbargoType.equals("6") ||
                CreateEmbargoActions.lastCreatedEmbargoType.equals("4") || CreateEmbargoActions.lastCreatedEmbargoType.equals("11") || CreateEmbargoActions.lastCreatedEmbargoType.equals("10") || CreateEmbargoActions.lastCreatedEmbargoType.equals("12") ||
                CreateEmbargoActions.lastCreatedEmbargoType.equals("9")) {
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).clear();
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).sendKeys(String.valueOf(Double.parseDouble(embargoAmount) * 2));
        } else {
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput)).clear();
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.operationAmountInput))
                    .sendKeys(embargoData.getOperationAmount());
        }

        if (CreateEmbargoActions.lastCreatedEmbargoType.equals("6")) {
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.insuranceCompany)).clear();
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.insuranceCompany)).sendKeys(embargoData.getInsuranceCompany());

            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.policyCode)).clear();
            wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.policyCode)).sendKeys(embargoData.getPolicyCode());
        }
    }

    public void completeEmbargoInformation(String embargoNumber, EmbargoPageData data) throws InterruptedException {
        positionOnEmbargo(embargoNumber);
        selectOption2();
        completeInformationForm(data);
        wait.until(ExpectedConditions.visibilityOf(completeInfoEmbargoPage.okBtn)).click();
    }
}
