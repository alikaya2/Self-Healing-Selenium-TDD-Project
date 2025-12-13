package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import util.ConfigManager;

import java.time.Duration;

public final class Driver implements ITestListener {
    
    private static final Logger logger = LoggerFactory.getLogger(Driver.class);
    private static volatile WebDriver driver;
    private static boolean isInitialized = false;
    private static boolean isHealeniumEnabled = false;

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: "+ result.getMethod().getMethodName());
        
        if (driver == null || !isInitialized) {
            setup();
        }
        
        if (isInitialized()) {
            if (isHealeniumEnabled) {
                logger.info("Healenium self-healing is active");
                logger.info("Healenium configuration: heal-enabled={}, score-cap={}, recovery-tries={}", 
                    ConfigManager.getProperty("healenium.enabled"), 
                    ConfigManager.getProperty("healenium.score.threshold"), 
                    ConfigManager.getProperty("healenium.recovery.tries"));
                logger.info("Self-healing driver type: {}", driver.getClass().getSimpleName());
            } else {
                logger.info("Using standard Selenium WebDriver");
                logger.info("Driver type: {}", driver.getClass().getSimpleName());
            }
        } else {
            logger.warn("Driver is not available");
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Finished test: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test suite finished");
        if (driver != null) {
                driver.quit();
                logger.info("Driver closed successfully");
        }
        driver = null;
        isInitialized = false;
        isHealeniumEnabled = false;
    }

    public static synchronized void setup() {
        try {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    logger.error("Error closing existing driver: " + e.getMessage());
                }
                driver = null;
                isInitialized = false;
            }

            try {
                java.nio.file.Path allureResults = java.nio.file.Paths.get("target/allure-results");
                if (java.nio.file.Files.exists(allureResults)) {
                    java.nio.file.Files.walk(allureResults)
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(java.nio.file.Path::toFile)
                        .forEach(java.io.File::delete);
                }
            } catch (Exception e) {
                logger.info("Allure folder could not cleared: {}", e.getMessage());
            }

            String browserName = ConfigManager.getProperty("browser");
            boolean headless = Boolean.parseBoolean(ConfigManager.getProperty("headless"));
            String testUrl = ConfigManager.getProperty("base.url");
            browserName = browserName.trim().toLowerCase();
            int implicitWait = Integer.parseInt(ConfigManager.getProperty("implicit.wait"));
            int pageLoadTimeout = Integer.parseInt(ConfigManager.getProperty("page.load.timeout"));

            WebDriver baseDriver = createLocalDriver(browserName, headless);

            baseDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            baseDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
            baseDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(pageLoadTimeout));

            String healEnabled = ConfigManager.getProperty("healenium.enabled");
            isHealeniumEnabled = Boolean.parseBoolean(healEnabled);
            
            if (isHealeniumEnabled) {
                logger.info("************* Test Started with Healenium Self-Healing *************");
                try {
                    driver = com.epam.healenium.SelfHealingDriver.create(baseDriver);
                } catch (Exception e) {
                    logger.error("Healenium is failed," +
                            " WebDriver is started: {}", e.getMessage());
                    driver = baseDriver;
                    isHealeniumEnabled = false;
                }
            } else {
                logger.info("************* Test Started with WebDriver *************");
                driver = baseDriver;
            }

            if (driver != null) {
                driver.get(testUrl);
                isInitialized = true;
            } else {
                throw new RuntimeException("Driver is null");
            }
        } catch (Exception e) {
            isInitialized = false;
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ex) {
                    logger.error("Error closing driver after failure: {}", ex.getMessage());
                }
                driver = null;
            }
            throw new RuntimeException("Driver initialization failed", e);
        }
    }
    

    private static WebDriver createLocalDriver(String browserName, boolean headless) {

        switch (browserName) {
            case "chrome":
                return new ChromeDriver(createChromeOptions(headless));
            case "edge":
                return new EdgeDriver(createEdgeOptions(headless));
            default:
                return new ChromeDriver(createChromeOptions(headless));
        }
    }

    private static ChromeOptions createChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
            "--disable-gpu",
            "--disable-popup-blocking", 
            "--ignore-certificate-errors",
            "--disable-translate", 
            "--disable-notifications", 
            "--start-maximized",
            "--no-sandbox",
            "--disable-dev-shm-usage"
        );
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        return options;
    }

    private static EdgeOptions createEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        if (headless) {
            options.addArguments("--headless");
        }
        return options;
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            throw new RuntimeException("Driver is null");
        }
        return driver;
    }

    public static synchronized void closeDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
            } finally {
                driver = null;
                isInitialized = false;
            }
        }
    }
    public static boolean isInitialized() {
        return isInitialized && driver != null;
    }

}
