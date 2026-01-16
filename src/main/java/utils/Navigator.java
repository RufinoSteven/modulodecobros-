package utils;

import config.Browser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class Navigator {
    public static void waitForLoad() {
        new WebDriverWait(Browser.getWebDriver(), Duration.ofSeconds(60)).until((ExpectedCondition<Boolean>) wd ->
        {
            assert wd != null;
            return Objects.equals(((JavascriptExecutor) wd).executeScript("return document.readyState"), "complete");
        });
    }
}
