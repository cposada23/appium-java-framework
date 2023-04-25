package org.example.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.example.android.AndroidBaseTest;
import org.example.screenobjects.android.GSCartScreen;
import org.example.screenobjects.android.GSProductCatalogScreen;
import org.example.screenobjects.android.GeneralStoreFormScreen;
import org.example.utils.dto.FormFields;
import org.example.utils.enums.Gender;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

public class GeneralStoreAPKExampleTests extends AndroidBaseTest {
    @Test
    public void fillFormExample() throws InterruptedException {
        GeneralStoreFormScreen formScreen = new GeneralStoreFormScreen(driver);
        // Type the name
        formScreen.setNameField("Camilo");
        // Select gender in the radio button
        formScreen.setGender(Gender.MALE);
        // Select Colombia
        String country = "Colombia";
        formScreen.selectCountry(country);
        // Click letsShop button
        formScreen.clickLetsShop();
        // Validate you are in the Products screen
        commonAssertions.assertIAmInScreen("Products");
    }

    @Test
    public void validateToastMessage() {
        GeneralStoreFormScreen formScreen = new GeneralStoreFormScreen(driver);
        // Click letsShop button
        formScreen.clickLetsShop();
        // Validate the toast message
        commonAssertions.assertToastMessage("Please enter your name");
    }


    @Test
    public void addToCartExample() throws InterruptedException {

        GeneralStoreFormScreen formScreen = new GeneralStoreFormScreen(driver);
        FormFields formFields = new FormFields(
                "Camilo",
                Gender.MALE,
                "Colombia"
        );
        GSProductCatalogScreen catalogScreen = formScreen.fillFormAndGoShopping(formFields);
        // Validate you are in the Products screen
        commonAssertions.assertIAmInScreen("Products");

        // Scroll down to product
        String elementText = "Jordan 6 Rings";

        catalogScreen.addItemToCart(elementText);
        Thread.sleep(1000);
        // Validate the text of the button changed to ADDED TO CART
        commonAssertions.assertTextEquals(
                catalogScreen.getAddItemButtonText(elementText),
    "ADDED TO CART"
        );
        // Go to the cart and validate the product is there
        GSCartScreen cartScreen = catalogScreen.goToCartScreen();
        // Validate I'm in the cart screen
        commonAssertions.assertIAmInScreen("Cart");

        // We only have one product, so we can validate it directly
        commonAssertions.assertTextEquals(
                cartScreen.findFirstElement().getText(),
                elementText
        );
    }

    @Test
    public void validateTotalCartAmount() throws InterruptedException, ParseException {

        GeneralStoreFormScreen formScreen = new GeneralStoreFormScreen(driver);
        FormFields formFields = new FormFields(
                "Camilo",
                Gender.MALE,
                "Argentina"
        );
        GSProductCatalogScreen catalogScreen = formScreen.fillFormAndGoShopping(formFields);
        // Validate you are in the Products screen
        commonAssertions.assertIAmInScreen("Products");

        // Two products to add to cart
        String product1 = "Air Jordan 4 Retro";
        String product2 = "Jordan 6 Rings";
        catalogScreen.addItemsToCart(new String[]{product1, product2});
        Thread.sleep(1000);
        GSCartScreen cartScreen = catalogScreen.goToCartScreen();

        commonAssertions.assertIAmInScreen("Cart");
        cartScreen.waitUntilProductIsVisible(product1);

        // Calculate total
        List<Number> prices =  cartScreen.getAllProductPrices();
        Number total = prices.stream().reduce(0, (a, b) -> a.doubleValue() + b.doubleValue());
        System.out.println("###################################################");
        System.out.println(total);
        System.out.println("###################################################");
        Number totalInScreen = cartScreen.getTotalInScreen();
        Assert.assertEquals(total, totalInScreen);
    }


    @Test
    public void webViewExample() throws ParseException, InterruptedException {
        GeneralStoreFormScreen formScreen = new GeneralStoreFormScreen(driver);
        FormFields formFields = new FormFields(
                "Camilo",
                Gender.MALE,
                "Argentina"
        );
        GSProductCatalogScreen catalogScreen = formScreen.fillFormAndGoShopping(formFields);
        // Validate you are in the Products screen
        commonAssertions.assertIAmInScreen("Products");

        // Two products to add to cart
        String product1 = "Air Jordan 4 Retro";
        String product2 = "Jordan 6 Rings";
        catalogScreen.addItemsToCart(new String[]{product1, product2});
        Thread.sleep(1000);
        GSCartScreen cartScreen = catalogScreen.goToCartScreen();
        commonAssertions.assertIAmInScreen("Cart");
        cartScreen.waitUntilProductIsVisible(product1);

        // Calculate total
        List<Number> prices =  cartScreen.getAllProductPrices();
        Number total = prices.stream().reduce(0, (a, b) -> a.doubleValue() + b.doubleValue());
        System.out.println("###################################################");
        System.out.println(total);
        System.out.println("###################################################");
        Number totalInScreen = cartScreen.getTotalInScreen();
        Assert.assertEquals(total, totalInScreen);


        // Click Proceed button, this will take me to a webView
        driver.findElement(AppiumBy.id("com.androidsample.generalstore:id/btnProceed")).click();
        Thread.sleep(6000);

        // Get the context handles
        Set<String> handles =  driver.getContextHandles();

        for (String handle : handles) {
            System.out.println("################### Handle");
            System.out.println(handle);
        }

        driver.context("WEBVIEW_com.androidsample.generalstore");

        // You can continue using selenium to automate the webView
        driver.findElement(By.xpath("//input[@name=\"q\"]")).sendKeys("test");

        // Go back to the app
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        driver.context("NATIVE_APP");

    }
}
