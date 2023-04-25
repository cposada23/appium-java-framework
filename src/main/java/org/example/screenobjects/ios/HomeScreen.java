package org.example.screenobjects.ios;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.example.utils.actions.ios.IOSActions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class HomeScreen extends IOSActions {

    @iOSXCUITFindBy(accessibility = "Alert Views")
    WebElement alertViews;

    private IOSDriver driver;
    public HomeScreen(IOSDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void selectAlertViews() {
        alertViews.click();
    }

}
