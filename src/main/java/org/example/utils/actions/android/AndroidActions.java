package org.example.utils.actions.android;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.example.utils.actions.WaitUtils;
import org.example.utils.enums.SwipeDirections;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public class AndroidActions {
    private AndroidDriver driver;
    protected WaitUtils waitUtils;

    public AndroidActions(AndroidDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public void longPress(WebElement element, int duration) {
        ((JavascriptExecutor)driver).executeScript(
                "mobile: longClickGesture",
                ImmutableMap.of(
                        "elementId", ((RemoteWebElement) element).getId(),
                        "duration", duration
                )
        );
    }


    public void scrollToEnd() {

        boolean canScrollMore;
        do {
            canScrollMore = (Boolean) ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of(
                    "left", 100, "top", 100, "width", 200, "height", 200,
                    "direction", "down",
                    "percent", 3.0
            ));
        } while (canScrollMore);

    }

    public void uiAutomatorScrollTextIntoView(String expectedText) {
        driver.findElement(
            AppiumBy.androidUIAutomator(
                String.format("new UiScrollable(new UiSelector().scrollable(true)).scrollTextIntoView(\"%s\")", expectedText)
            )
        );
    }

    public void swipe(WebElement element, SwipeDirections direction) {
        ((JavascriptExecutor) driver).executeScript(
                "mobile: swipeGesture",
                ImmutableMap.of(
                        "elementId", ((RemoteWebElement) element).getId(),
                        "direction", direction.getValue(),
                        "percent", 0.75
                )
        );

    }

    public void dragAndDrop(WebElement element, int xCoordinate, int yCoordinate) {
        ((JavascriptExecutor) driver).executeScript(
                "mobile: dragGesture",
                ImmutableMap.of(
                        "elementId", ((RemoteWebElement) element).getId(),
                        "endX", xCoordinate,
                        "endY", yCoordinate
                )
        );
    }
}
