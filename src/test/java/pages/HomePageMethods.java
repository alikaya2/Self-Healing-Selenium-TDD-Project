package pages;

import constants.Constants;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.BasePageUtil;

import java.time.Duration;

import static constants.Constants.OPENING_WIDGET;

public class HomePageMethods extends BasePageUtil {
    private static WebDriver staticDriver;
    private static boolean isInitialized = false;

    public HomePageMethods() {
    }

    public static WebDriver getStaticDriver() {
        return staticDriver;
    }

    public static void setStaticDriver(WebDriver driver) {
        staticDriver = driver;
    }

    public static boolean isInitialized() {
        return isInitialized;
    }

    private static final Logger logger = LoggerFactory.getLogger(HomePageMethods.class);

    public String getTitle() {
        logger.info("Getting page title");
        return getWebDriver().getTitle();
    }


    public String getCurrentUrl() {
        logger.info("Getting current URL");
        return getWebDriver().getCurrentUrl();
    }

    public boolean isPageLoaded() {
        try {
            Thread.sleep(10000);
            return isDisplayed(Constants.MAIN_CONTENT);
        } catch (Exception e) {
            logger.error("Error checking if page is loaded: {}", e.getMessage());
            return false;
        }
    }
    public boolean isSearchBoxDisplayed() {
        logger.info("Checking if search box is displayed");
        try {
            return isDisplayed(Constants.SEARCH_BOX);
        } catch (Exception e) {
            logger.error("Error checking search box: {}", e.getMessage());
            return false;
        }
    }


    public boolean isDisplayed(By locator, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            logger.error("Element not found: {}", locator);
            return false;
        }
    }

    public boolean isDisplayed(By locator) {
        return isDisplayed(locator, Constants.DEFAULT_TIMEOUT);
    }

    public void searchForProduct(String searchTerm) {
        try {
            sendKeysToElement(Constants.SEARCH_BOX, searchTerm);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search", e);
        }
    }


    public void openCart() {
        try {
            clickElement(Constants.CART_BUTTON);
        } catch (Exception e) {
            throw new RuntimeException("Failed to open cart", e);
        }
    }

    public void waitForLoadingToComplete() {
        logger.info("Waiting for loading to complete");
        try {
            waitForLoadJavaScript();
            waitByMilliSeconds(2000);
        } catch (Exception e) {
            logger.error("Error waiting for loading: " + e.getMessage());
        }
    }

    public void deleteCookies() {
        getWebDriver().manage().deleteAllCookies();
        logger.info("Deleting all cookies into page");

    }

    public void closeOpeningWindows() {
        int retry = 0;
        while (!isDisplayed(OPENING_WIDGET) && retry < 10) {
            waitByMilliSeconds(500);
            retry++;
        }
        if (isDisplayed(OPENING_WIDGET)) {
            clickElement(OPENING_WIDGET);
            retry = 0;
            while (isDisplayed(OPENING_WIDGET) && retry < 5) {
                waitByMilliSeconds(500);
                retry++;
            }
            Assertions.assertFalse(isDisplayed(OPENING_WIDGET), "Pop-up couldn't close!");
        }
    }
}

