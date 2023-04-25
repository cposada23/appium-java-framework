package org.example.screenobjects.ios;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class HomeScreen {

    @iOSXCUITFindBy(accessibility = "Alert Views")
    WebElement alertViews;

    IOSDriver driver;
    public HomeScreen(IOSDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void selectAlertViews() {
        alertViews.click();
    }

}
