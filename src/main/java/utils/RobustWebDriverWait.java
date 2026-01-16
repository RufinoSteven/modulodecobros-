package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.function.Function;

public class RobustWebDriverWait extends WebDriverWait {
    public RobustWebDriverWait(WebDriver driver, Duration timeout) {
        super(driver, timeout);
    }

    @Override
    public <T> T until(Function<? super WebDriver, T> isTrue) {
        int retries = 5;
        for (int i = 0; i < retries; i++) {
            try {
                return super.until(isTrue);
            } catch (StaleElementReferenceException e) {
                if (i == retries - 1) throw e;
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            } catch (WebDriverException e) {
                if (e.getMessage() != null && e.getMessage().contains("Node with given id does not belong to the document")) {
                    if (i == retries - 1) throw e;
                    try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                } else {
                    throw e;
                }
            }
        }
        throw new TimeoutException("Elemento no encontrado después de reintentos");
    }
}