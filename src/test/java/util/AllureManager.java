package util;

import driver.Driver;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.openqa.selenium.OutputType.BYTES;


public final class AllureManager {


    @Attachment(value = "{0}", type = "image/png")
    public static byte[] takeScreenshot(String name) {
        try {
            WebDriver driver = Driver.getDriver();
            if (driver instanceof TakesScreenshot) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(BYTES);
                return screenshot;
            } else {
                return new byte[0];
            }
        } catch (Exception e) {
            return new byte[0];
        }
    }
    public static void addFileAttachment(String filePath, String attachmentName) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
                Allure.addAttachment(attachmentName, Files.probeContentType(file.toPath()), 
                                   new String(fileContent));
            }
        } catch (IOException ignored) {
        }
    }

} 