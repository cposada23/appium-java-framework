> See this repo for the basics of Appium with Java: https://github.com/cposada23/appium-java

# Framework
In this framework we are going to use:
- TestNG
- PageObjects with PageFactory


### Page Objects ( Screen Objects )
We can say that in mobile we have screens not pages, so we'll call the 'page object' -> 'screen object'

Create a new packages:
- One for Android: `src/main/java/org/example/screenobjects/android`
- One for iOS: `src/main/java/org/example/screenobjects/ios`

#### Page Object Example with Page factory:

#### For Android
You'll have to initialize the element with the help of `PageFactory` and use the annotation `@AndoidFindBy` to tell the elements that are going to be initialized.

```java
public class GeneralStoreFormScreen {
    @AndroidFindBy(id = "com.androidsample.generalstore:id/nameField")
    private WebElement nameField;

    private AndroidDriver driver;
    public GeneralStoreFormScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
}
```

#### For iOS
You'll have to initialize the element with the help of `PageFactory` and use the annotation `@iOSXCUITFindBy` to tell the elements that are going to be initialized.


```java
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
```