package org.example.testutils;

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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AndroidBaseTest extends AppiumUtils {
    public AndroidDriver driver;
    protected CommonAssertions commonAssertions;
    protected JsonUtils jsonUtils;

    private AppiumDriverLocalService service;
    @BeforeClass(alwaysRun = true)
    public void configureAppium () throws IOException {
        loadProperties();

        service = startAppiumServer(
                properties.getProperty("appiumIPAddress"),
                Integer.parseInt(properties.getProperty("appiumPort")),
                properties.getProperty("nodeModulesAppiumPath")
        );
        // Set the capabilities
        UiAutomator2Options capabilities = new UiAutomator2Options();
        capabilities.setDeviceName(properties.getProperty("androidDeviceName"));
        capabilities.setApp(properties.getProperty("androidAppPath"));
        // Start the driver
        driver = new AndroidDriver(service.getUrl(), capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        commonAssertions = new CommonAssertions(driver);
        jsonUtils = new JsonUtils();
    }
    @AfterClass(alwaysRun = true)
    public void tearDown () {
        // Clean
        driver.quit();
        service.close();
    }

}
