package runners;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * This class configures and executes Cucumber test scenarios in JUnit 5.
 *
 * <p>Important configurations:</p>
 * <ul>
 *     <li>Suite: Indicates that this class is a test suite.</li>
 *     <li>IncludeEngines: Specifies that the Cucumber engine should be used.</li>
 *     <li>SelectClasspathResource: Specifies where to find the .feature files.</li>
 *     <li>ConfigurationParameter: Configures the packages where the step implementations are located.</li>
 * </ul>
 *
 * @author Cristofer Nuñez
 * @version 1.0
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepDefinitions,config,runners,utils")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,utils.reporting.ExtentCucumberPlugin,com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:")
@IncludeTags({"test2"})
@ExcludeTags("AuxiliaryScenarios")
public class TestRunner {
}
