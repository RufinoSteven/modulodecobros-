package utils;

import config.Browser;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WindowManager {

    public static void openNewWindow() {
        Browser.getWebDriver().switchTo().newWindow(WindowType.WINDOW);
        Browser.getWebDriver().manage().window().maximize();
    }

    public static void switchToChildWindow() {
        new WebDriverWait(Browser.getWebDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfWindowsToBe(2));
        String[] opened = Browser.getWebDriver().getWindowHandles().toArray(new String[0]);
        Browser.getWebDriver().switchTo().window(opened[1]);
    }

    public static void switchToParentWindow() {
        new WebDriverWait(Browser.getWebDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfWindowsToBe(2));
        String[] opened = Browser.getWebDriver().getWindowHandles().toArray(new String[0]);
        Browser.getWebDriver().switchTo().window(opened[0]);
    }

    public static void closeChildWindow() {
        String[] opened = Browser.getWebDriver().getWindowHandles().toArray(new String[0]);
        Browser.getWebDriver().switchTo().window(opened[1]);
        Browser.getWebDriver().close();
        Browser.getWebDriver().switchTo().window(opened[0]);
    }

    public static void closeParentWindow() {
        String[] opened = Browser.getWebDriver().getWindowHandles().toArray(new String[0]);
        Browser.getWebDriver().switchTo().window(opened[0]);
        Browser.getWebDriver().close();
        Browser.getWebDriver().switchTo().window(opened[1]);
        Browser.getWebDriver().manage().deleteAllCookies();
    }

}
