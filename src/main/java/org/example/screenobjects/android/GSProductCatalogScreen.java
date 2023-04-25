package org.example.screenobjects.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.utils.actions.android.AndroidActions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class GSProductCatalogScreen extends AndroidActions {

    @AndroidFindBy(id = "com.androidsample.generalstore:id/appbar_btn_cart")
    private WebElement btnGoToCart;
    private String addItemButtonXpath = "//android.widget.TextView[@text='%s']/..//*[@resource-id='com.androidsample.generalstore:id/productAddCart']";
    private AndroidDriver driver;

    public GSProductCatalogScreen(AndroidDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void addItemToCart(String itemDescription) {
        uiAutomatorScrollTextIntoView(itemDescription);
        // Generate the selector
        String xpath = String.format(
                addItemButtonXpath,
                itemDescription
        );
        // Add to cart
        driver.findElement(
                AppiumBy.xpath(xpath)
        ).click();
    }

    public void addItemsToCart(String [] items) throws InterruptedException {
        for (String item : items) {
            addItemToCart(item);
            Thread.sleep(1000);
        }
    }

    public String getAddItemButtonText(String itemDescription) {
        String xpath = String.format(
                addItemButtonXpath,
                itemDescription
        );
        // Add to cart
        return driver.findElement(
                AppiumBy.xpath(xpath)
        ).getText();
    }

    public GSCartScreen goToCartScreen() {
        btnGoToCart.click();
        return new GSCartScreen(driver);
    }
}
