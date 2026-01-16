package stepDefinitions.Embargo;

import actions.Embargo.SearchEmbargoActions;
import io.cucumber.java.en.When;

public class SearchEmbargoSteps {

    private final SearchEmbargoActions searchEmbargoActions = new SearchEmbargoActions();

    @When("el usuario escribe el numero de embargo {string}")
    public void writeEmbargoNumber(String functionNumber) {
        searchEmbargoActions.enterFunctionNumber(functionNumber);
    }

    @When("el usuario hace click en el boton OK y Confirmar")
    public void clickOkButton() {
        searchEmbargoActions.clickOkButton();
    }

}
