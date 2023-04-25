package org.example.utils.assertions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class CommonAssertions {

    private AndroidDriver driver;
    public CommonAssertions(AndroidDriver driver) {
        this.driver = driver;
    }

    public void assertIAmInScreen(String expectedScreen) throws InterruptedException {
        Thread.sleep(500);
        WebElement screenBar = driver.findElement(
            AppiumBy.xpath(
                String.format(
                    "//android.widget.TextView[@text='%s']",
                    expectedScreen
                )
            )
        );

        Assert.assertEquals(screenBar.isDisplayed(), true);
    }

    public void assertToastMessage(String expectedMessage) {
        String toastMessage = driver.findElement(AppiumBy.xpath("(//android.widget.Toast)[1]")).getText();
        Assert.assertEquals(toastMessage, expectedMessage);
    }

    public void assertTextEquals(String text, String expectedText) {
        Assert.assertEquals(text, expectedText);
    }
}
