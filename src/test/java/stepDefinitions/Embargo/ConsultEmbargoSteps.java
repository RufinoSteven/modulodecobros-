package stepDefinitions.Embargo;

import actions.Embargo.CompleteInfoEmbargoActions;
import actions.Embargo.ConsultEmbargoActions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;


public class ConsultEmbargoSteps {

    private final ConsultEmbargoActions consultEmbargoActions = new ConsultEmbargoActions();
    private final CompleteInfoEmbargoActions completeInfoEmbargoActions = new CompleteInfoEmbargoActions();

    // Step: Enter the function number
    @Given("el usuario ingresa el número de función {string}")
    public void theUserEntersTheFunctionNumber(String functionNumber) {
        consultEmbargoActions.enterFunctionNumber(functionNumber);
    }
}