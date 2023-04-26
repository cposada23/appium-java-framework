package org.example.testutils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppiumUtils {
    protected Properties properties;
    public AppiumDriverLocalService startAppiumServer(
            String appiumIPAddress,
            int appiumPort,
            String nodeModulesAppiumPath
    ) {
        // Start Appium Server programmatically
        AppiumDriverLocalService service = new AppiumServiceBuilder()
                .withAppiumJS(new File(nodeModulesAppiumPath))
                .withIPAddress(appiumIPAddress)
                .usingPort(appiumPort)
                .build();

        service.start();

        return service;
    }

    public void loadProperties() throws IOException {
        properties = new Properties();
        FileInputStream inputStream = new FileInputStream(
                System.getProperty("user.dir").concat(
                        "/src/main/java/org/example/resources/data.properties"
                )
        );

        properties.load(inputStream);
    }

    public String getScreenshot(String screenShotName, AppiumDriver driver) throws IOException {
        File source = driver.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") +
                "/reports/" + screenShotName + ".png";
        FileUtils.copyFile(source, new File(destination));
        return destination;
    }
}
