/**
 * This class defines Cucumber hooks for WebDriver setup and ExtentReports integration.
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */

package stepDefinitions;

import config.Browser;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ExtentReportManager;
import utils.RobustWebDriverWait;

import java.time.Duration;

public class Hooks {
    public static final Logger log = LogManager.getLogger(Hooks.class);

    /**
     * Initializes the WebDriver and the explicit wait before each scenario.
     */
    @Before(order = 0)
    public void setupWebDriver() {
        Browser.wrapperSetup();
        if (Browser.wait == null) {
            Browser.wait = new RobustWebDriverWait(Browser.getWebDriver(), Duration.ofSeconds(10));
            log.info("Wait initiated (from Hooks)");
        }
    }

    /**
     * Initializes the ExtentReports scenario node after WebDriver setup.
     *
     * @param scenario Cucumber scenario context
     */
    @Before(order = 1)
    public void setupExtentReporting(Scenario scenario) {
        ExtentReportManager.createScenario(scenario);
    }

    /**
     * Cleans up ExtentReports after each scenario.
     *
     * @param scenario Cucumber scenario context
     */
    @After
    public void teardownScenario(Scenario scenario) {
        ExtentReportManager.finishScenario(scenario);
        ExtentReportManager.attachLog(scenario);
        ExtentReportManager.clearScenario();
    }

    /**
     * Flushes the ExtentReports after the entire test suite finishes.
     */
    @AfterAll
    public static void teardownSuite() {
        ExtentReportManager.flush();
    }
}