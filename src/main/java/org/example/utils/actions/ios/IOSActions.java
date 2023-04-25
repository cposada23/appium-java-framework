package org.example.utils.actions.ios;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.example.utils.actions.WaitUtils;
import org.example.utils.enums.SwipeDirections;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public class IOSActions {
    private IOSDriver driver;
    protected WaitUtils waitUtils;

    public IOSActions(IOSDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public void longPress(WebElement element, int duration) {

        ((JavascriptExecutor)driver).executeScript(
                "mobile: touchAndHold",
                ImmutableMap.of(
                        "elementId", ((RemoteWebElement) element).getId(),
                        "duration", duration
                )
        );
    }


    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "mobile: scroll",
            ImmutableMap.of(
                    "elementId", ((RemoteWebElement) element).getId(),
                    "direction", "down"
            )
        );
    }

}
