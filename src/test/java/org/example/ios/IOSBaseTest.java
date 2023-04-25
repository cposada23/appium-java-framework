package org.example.ios;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class IOSBaseTest {


    private String deviceName = "iPhone11ios14";
    private String appPath = "/Users/camilo.posadaa/Documents/personal/framworks/java/Appium-java-framework/src/test/java/org/resources/UIKitCatalog.app";
    private String nodeModulesAppiumPath = "/Users/camilo.posadaa/.nvm/versions/node/v18.13.0/lib/node_modules/appium/build/lib/main.js";
    private String appiumIPAddress = "127.0.0.1";
    int appiumPort = 4723;
    public AppiumDriverLocalService service;

    protected IOSDriver driver;
    @BeforeClass
    public void configureAppium () throws MalformedURLException {
        // Start Appium Server programmatically
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File(nodeModulesAppiumPath))
                .withIPAddress(appiumIPAddress)
                .usingPort(appiumPort)
                .build();

        service.start();
        // Set the capabilities
        XCUITestOptions capabilities = new XCUITestOptions();
        capabilities.setDeviceName(deviceName);
        capabilities.setApp(appPath);
        capabilities.setPlatformVersion("14.5");
        // In IOS Appium will install the WebDriver Agent
        // So we will need to wait until it is installed an available
        capabilities.setWdaLaunchTimeout(Duration.ofSeconds(60));

        URL appiumSeverURL = new URL("http://".concat(appiumIPAddress.concat(":").concat(String.valueOf(appiumPort))));
        // Start the driver
        driver = new IOSDriver(appiumSeverURL, capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    @AfterClass
    public void tearDown () {
        // Clean
        driver.quit();
        service.close();
    }
}
