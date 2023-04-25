> See this repo for the basics of Appium with Java: https://github.com/cposada23/appium-java

# Framework
In this framework we are going to use:
- TestNG
- PageObjects with PageFactory

### TODO Framework structure

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

## Test NG

### Using Data Provider
Use the `@DataProvider` annotation. In the method you put the data that the test will use for the execution. Then, in the method marked for test, in the annotation you indicate the dataProvider to be used `@Test(dataProvider =  "replace with dataprovider name")` :

```java

	@DataProvider
    public Object[][] getData() {
        return new Object[][] {{"Camilo", "Colombia", Gender.MALE}};
    }

    @Test(dataProvider = "getData")
    public void fillFormExample(String name, String country, Gender gender) throws InterruptedException {
        GeneralStoreFormScreen formScreen = new GeneralStoreFormScreen(driver);
        // Type the name
        formScreen.setNameField(name);
        // Select gender in the radio button
        formScreen.setGender(gender);
        formScreen.selectCountry(country);
        // Click letsShop button
        formScreen.clickLetsShop();
        // Validate you are in the Products screen
        commonAssertions.assertIAmInScreen("Products");
    }
```

### Running test with multiple data sets
You can add multiple data sets to the data provider like this:

```java
    @DataProvider
    public Object[][] getData() {
        return new Object[][] {
                {"Camilo", "Colombia", Gender.MALE},
                {"Natalia", "Argentina", Gender.FEMALE}
        };
    }

```

### Get data from json file

We will use `Apache Commons IO` and `Jackson DataBind` to parse JSONs files.
> Parse Json file -> Json String we use `Commons-io`

> Json String -> Hash Map we use `Jackson`

```xml
<!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.11.0</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.0</version>
</dependency>
```

I created a JsonUtils class in the folder `src/main/java/org/example/utils/JsonUtils.java` with a method that will convert the Json into a List of HasMaps:

```java
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class JsonUtils {
    public List<HashMap<String, String>> getJsonData(String jsonFilePath) throws IOException {

        // Get the JSON String
        String jsonContent = FileUtils.readFileToString(
            new File(jsonFilePath)
        );

        // Map the Json string to HashMap
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> data = mapper.readValue(
                jsonContent, new TypeReference<>() {
                }
        );

        return data;

    }
}
```


And I use it like this in the DataProvider:

```java
    @DataProvider
    public Object[][] getDataFromJson() throws IOException {
        List<HashMap<String, String>> data = jsonUtils.getJsonData(
                System.getProperty("user.dir").concat(
                      "/src/test/java/org/example/testdata/eCommerce.json"
                )
        );
        Object [][] object = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, String> row = data.get(i);
            object[i][0] = row;
        }
        return object;
    }
```

And then the test will receive a HashMap instead of the parameters that we previously had

```java
	@Test(dataProvider = "getDataFromJson")
    public void fillFormFromJsonExample(HashMap<String, String> input) throws Exception {
        GeneralStoreFormScreen formScreen = new GeneralStoreFormScreen(driver);
        // Type the name
        formScreen.setNameField(input.get("name"));
        // Select gender in the radio button
        formScreen.setGender(Gender.of(input.get("gender")));
        formScreen.selectCountry(input.get("country"));
        // Click letsShop button
        formScreen.clickLetsShop();
        // Validate you are in the Products screen
        commonAssertions.assertIAmInScreen("Products");
    }
```