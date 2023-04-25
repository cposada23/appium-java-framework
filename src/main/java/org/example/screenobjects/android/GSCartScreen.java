package org.example.screenobjects.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.utils.actions.android.AndroidActions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GSCartScreen extends AndroidActions {


    @AndroidFindBy(id = "com.androidsample.generalstore:id/productName")
    List<WebElement> products;

    @AndroidFindBy(id = "com.androidsample.generalstore:id/productPrice")
    List<WebElement> prices;

    @AndroidFindBy(id = "com.androidsample.generalstore:id/totalAmountLbl")
    WebElement totalPrice;

    String xpathForCartItems = "//android.widget.TextView[@text='%s']";
    public GSCartScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public WebElement findFirstElement() {
        return products.get(0);
    }

    public void waitUntilProductIsVisible(String productName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(AppiumBy.xpath(String.format(xpathForCartItems, productName)))
        ));
    }

    public List<Number> getAllProductPrices() {
        List<Number> allPrices =  prices.stream().map(sPrice -> {
            try {
                return NumberFormat.getCurrencyInstance(Locale.US).parse(sPrice.getText());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        System.out.println("###################################################");
        System.out.println(allPrices);
        System.out.println("###################################################");

        return  allPrices;
    }

    public Number getTotalInScreen() throws ParseException {
        Number total = NumberFormat.getCurrencyInstance(Locale.US).parse(
            totalPrice.getText().replaceAll(" ", "")
        );
        System.out.println("###################################################");
        System.out.println(total);
        System.out.println("###################################################");
        return total;
    }
}
