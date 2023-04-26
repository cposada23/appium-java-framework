package org.example.testutils;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class IOSBaseTest extends AppiumUtils {
    protected IOSDriver driver;
    private AppiumDriverLocalService service;
    @BeforeClass
    public void configureAppium () throws IOException {
        loadProperties();
        // Start Appium Server programmatically
        service = startAppiumServer(
                properties.getProperty("appiumIPAddress"),
                Integer.parseInt(properties.getProperty("appiumPort")),
                properties.getProperty("nodeModulesAppiumPath")
        );
        // Set the capabilities
        XCUITestOptions capabilities = new XCUITestOptions();
        capabilities.setDeviceName(properties.getProperty("iosDeviceName"));
        capabilities.setApp(properties.getProperty("iosAppPath"));
        capabilities.setPlatformVersion(properties.getProperty("iosPlatformVersion"));
        // In IOS Appium will install the WebDriver Agent
        // So we will need to wait until it is installed an available
        capabilities.setWdaLaunchTimeout(Duration.ofSeconds(60));

        // Start the driver
        driver = new IOSDriver(service.getUrl(), capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    @AfterClass
    public void tearDown () {
        // Clean
        driver.quit();
        service.close();
    }
}
