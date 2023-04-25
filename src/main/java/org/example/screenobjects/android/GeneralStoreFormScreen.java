package org.example.screenobjects.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.utils.actions.android.AndroidActions;
import org.example.utils.dto.FormFields;
import org.example.utils.enums.Gender;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class GeneralStoreFormScreen extends AndroidActions {
    @AndroidFindBy(id = "com.androidsample.generalstore:id/nameField")
    private WebElement nameField;

    @AndroidFindBy(xpath = "//android.widget.RadioButton[@text='Female']")
    private WebElement femaleOption;

    @AndroidFindBy(xpath = "//android.widget.RadioButton[@text='Male']")
    private WebElement maleOption;

    @AndroidFindBy(id = "android:id/text1")
    private WebElement countrySelectButton;

    @AndroidFindBy(id = "com.androidsample.generalstore:id/btnLetsShop")
    private WebElement btnLetsShop;

    public GeneralStoreFormScreen(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    // Actions

    public void setNameField(String name) {
        nameField.sendKeys(name);
        driver.hideKeyboard();
    }

    public void setGender(Gender gender) {
        if ((gender.equals(Gender.FEMALE))) {
            femaleOption.click();
        } else {
            maleOption.click();
        }
    }

    public void selectCountry(String country) throws InterruptedException {
        // Open the select for the country
        countrySelectButton.click();
        Thread.sleep(1000);
        uiAutomatorScrollTextIntoView(country);
        driver.findElement(
            AppiumBy.xpath(
                    String.format("//android.widget.TextView[@text='%s']", country)
            )
        ).click();
    }

    public void clickLetsShop() {
        btnLetsShop.click();
    }

    public GSProductCatalogScreen fillFormAndGoShopping(FormFields fields) throws InterruptedException {

        setGender(fields.getGender());
        setNameField(fields.getUserName());
        selectCountry(fields.getCountry());
        clickLetsShop();

        return new GSProductCatalogScreen(driver);
    }
}
