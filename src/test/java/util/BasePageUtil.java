package util;

import driver.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BasePageUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(BasePageUtil.class);
    private static final int DEFAULT_TIMEOUT = 10;

    protected WebDriver getWebDriver() {
        return Driver.getDriver();
    }

    protected WebDriverWait getWait() {
        return new WebDriverWait(getWebDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }
    
    public String getTitle() {
        try {
            String title = getWebDriver().getTitle();
            return title;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get page title", e);
        }
    }

    public WebElement findElement(By by) {
        try {
            return getWebDriver().findElement(by);
        } catch (Exception e) {
            throw new RuntimeException("Element not found: " + by, e);
        }
    }

    public java.util.List<WebElement> findElements(By by) {
        try {
            return getWebDriver().findElements(by);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find elements: " + by, e);
        }
    }

    public void clickElement(By by) {
        try {
            WebElement element = findElement(by);
            element.click();
            logger.info("Element clicked successfully: " + by);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click element: " + by, e);
        }
    }

    public void sendKeysToElement(By by, String value) {
        try {
            WebElement element = findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Couldn't input: " + by);
        } catch (Exception e) {
            throw new RuntimeException("Failed to input", e);
        }
    }

    public boolean isDisplayed(By by) {
        try {
            WebElement element = findElement(by);
            return element.isDisplayed();
        } catch (Exception e) {
            logger.error("Failed to check if element is displayed: "+ e.getMessage());
            return false;
        }
    }

    public boolean isElementPresent(By by) {
        try {
            java.util.List<WebElement> elements = findElements(by);
            return !elements.isEmpty();
        } catch (Exception e) {
            logger.error("Failed to check if element is present: {}", e.getMessage());
            return false;
        }
    }

    public WebElement waitForElementClickable(By by) {
        try {
            WebDriverWait wait = getWait();
            return wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            throw new RuntimeException("Element is not clickable: " + by, e);
        }
    }

    public void waitForLoadJavaScript() {
        try {
            WebDriverWait wait = getWait();
            wait.until((ExpectedCondition<Boolean>) driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return "complete".equals(js.executeScript("return document.readyState"));
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to Js wait", e);
        }
    }

    public void waitByMilliSeconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Wait interrupted", e);
        }
    }


}
