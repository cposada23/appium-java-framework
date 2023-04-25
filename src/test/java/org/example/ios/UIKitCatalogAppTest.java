package org.example.ios;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.annotations.Test;

public class UIKitCatalogAppTest extends IOSBaseTest{


    @Test
    public void basicTest() {
        driver.findElement(AppiumBy.accessibilityId("Alert Views")).click();
    }

    @Test
    public void classChainExample() {
        driver.findElement(AppiumBy.accessibilityId("Alert Views")).click();
        driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Text Entry'`]")).click();
        // Write in the input
        driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeCell")).sendKeys("Hello World");
        driver.findElement(AppiumBy.accessibilityId("OK")).click();
    }

    @Test
    public void predicateStringExample() {
        driver.findElement(AppiumBy.accessibilityId("Alert Views")).click();
        driver.findElement(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND value == 'Confirm / Cancel'")).click();
        // Example with value begins with
        // driver.findElement(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND value BEGINSWITH[c] 'Confirm'")).click();
        // Example with value ends with
        // driver.findElement(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND value ENDSWITH[c] 'Confirm'")).click();
        driver.findElement(AppiumBy.iOSNsPredicateString("label == 'Confirm'")).click();

        String text = driver.findElement(AppiumBy.iOSNsPredicateString("name BEGINSWITH[c] 'A message'")).getText();
        System.out.println(String.format("Text: %s", text));
    }

    @Test
    public void longPressExample() {
        driver.findElement(AppiumBy.accessibilityId("Steppers")).click();
        WebElement element = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Increment'`][3]"));

        ((JavascriptExecutor)driver).executeScript(
                "mobile: touchAndHold",
                ImmutableMap.of(
                        "elementId", ((RemoteWebElement) element).getId(),
                        "duration", 5
                )
        );
    }

    @Test
    public void scrollExample() {
        WebElement element = driver.findElement(AppiumBy.accessibilityId("Web View"));

        ((JavascriptExecutor)driver).executeScript(
                "mobile: scroll",
                ImmutableMap.of(
                        "elementId", ((RemoteWebElement) element).getId(),
                        "direction", "down"
                )
        );

        element.click();
    }

}
