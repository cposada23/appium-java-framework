package org.example.android;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.example.utils.JsonUtils;
import org.example.utils.actions.android.AndroidActions;
import org.example.utils.assertions.CommonAssertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AndroidBaseTest {
    private String deviceName = "Nexus 6P API 31";
    private String appPath = System.getProperty("user.dir") + "/src/test/java/org/resources/General-Store.apk";
    private String nodeModulesAppiumPath = "/Users/camilo.posadaa/.nvm/versions/node/v18.13.0/lib/node_modules/appium/build/lib/main.js";
    private String appiumIPAddress = "127.0.0.1";
    int appiumPort = 4723;
    public AppiumDriverLocalService service;

    protected AndroidDriver driver;
    protected CommonAssertions commonAssertions;
    protected JsonUtils jsonUtils;
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
        UiAutomator2Options capabilities = new UiAutomator2Options();
        capabilities.setDeviceName(deviceName);
        capabilities.setApp(appPath);
        URL appiumSeverURL = new URL("http://".concat(appiumIPAddress.concat(":").concat(String.valueOf(appiumPort))));
        // Start the driver
        driver = new AndroidDriver(appiumSeverURL, capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        commonAssertions = new CommonAssertions(driver);
        jsonUtils = new JsonUtils();
    }
    @AfterClass
    public void tearDown () {
        // Clean
        driver.quit();
        service.close();
    }

}
