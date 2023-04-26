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


### Appium Utility Class
I created an Appium utils class to start the Appium server and load a properties file:

```java
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppiumUtils {
    private String nodeModulesAppiumPath = "/Users/camilo.posadaa/.nvm/versions/node/v18.13.0/lib/node_modules/appium/build/lib/main.js";

    protected Properties properties;
    public AppiumDriverLocalService startAppiumServer(String appiumIPAddress, int appiumPort) {
        // Start Appium Server programmatically
        AppiumDriverLocalService service = new AppiumServiceBuilder()
                .withAppiumJS(new File(nodeModulesAppiumPath))
                .withIPAddress(appiumIPAddress)
                .usingPort(appiumPort)
                .build();

        service.start();

        return service;
    }

    public void loadProperties() throws IOException {
        properties = new Properties();
        FileInputStream inputStream = new FileInputStream(
                System.getProperty("user.dir").concat(
                        "/src/main/java/org/example/resources/data.properties"
                )
        );

        properties.load(inputStream);
    }
}

```

Then in the baseTest Utility I extend it, something  like this:

> See that now i'm getting the app path, android device name, iosverion... etc. from the properties file

```java
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.example.utils.JsonUtils;
import org.example.utils.actions.android.AndroidActions;
import org.example.utils.assertions.CommonAssertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AndroidBaseTest extends AppiumUtils {
    protected AndroidDriver driver;
    protected CommonAssertions commonAssertions;
    protected JsonUtils jsonUtils;

    private AppiumDriverLocalService service;
    @BeforeClass
    public void configureAppium () throws IOException {
        loadProperties();

        service = startAppiumServer(
                properties.getProperty("appiumIPAddress"),
                Integer.parseInt(properties.getProperty("appiumPort"))
        );
        // Set the capabilities
        UiAutomator2Options capabilities = new UiAutomator2Options();
        capabilities.setDeviceName(properties.getProperty("androidDeviceName"));
        capabilities.setApp(properties.getProperty("androidAppPath"));
        // Start the driver
        driver = new AndroidDriver(service.getUrl(), capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        commonAssertions = new CommonAssertions(driver);
        jsonUtils = new JsonUtils();
    }
    @AfterClass
    public void tearDown () {
        // Clean
        driver.quit();
        service.close();
    }

}
```

### Extent Report
> Important: To see the report you need to run the test suite from the testNG.xml file. In IntelliJ you right click the xml file and select run.
```xml
<!-- https://mvnrepository.com/artifact/com.aventstack/extentreports -->
<dependency>
    <groupId>com.aventstack</groupId>
    <artifactId>extentreports</artifactId>
    <version>5.0.9</version>
</dependency>
```

Create a test util to manage the Extent Report initializing: `src/test/java/org/example/testutils/ExtendReporterNG.java`

```java
package org.example.testutils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
public class ExtendReporterNG {

    private static ExtentReports extentReports;
    public static ExtentReports getReporterObject() {
        String path = System.getProperty("user.dir") + "/reports/index.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("Mobile Automation Results");
        reporter.config().setDocumentTitle("Test Result");

        extentReports = new ExtentReports();
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Tester", "Camilo");

        return extentReports;
    }

}

```

#### Taking ScreenShots
We need an util to take screenshots in case of failure, we will put this in the AppiumUtils class

```java
    public String getScreenshot(String screenShotName, AppiumDriver driver) throws IOException {
        File source = driver.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") +
                "/reports/" + screenShotName + ".png";
        FileUtils.copyFile(source, new File(destination));
        return destination;
    }
```

### TestNG Listeners

We will use testNG listeners to helps us with the reporting

#### Available Listeners are:
- onTestStart
- onTestSuccess
- onTestFailure
- onTestSkipped
- onTestFailedButWithinSuccessPercentage
- onTestFailedWithTimeout
- onStart
- onFinish

Implement the `ITestListener` interface from TestNG like this:

```java
package org.example.testutils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import lombok.SneakyThrows;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class NGListeners extends AppiumUtils implements ITestListener {

    private ExtentTest test;

    private ExtentReports extentReports = ExtendReporterNG.getReporterObject();
    private AppiumDriver driver;
    @Override
    public void onTestStart(ITestResult result) {
        test = extentReports.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.fail(result.getThrowable());
        try {
            driver = (AppiumDriver) result
                    .getTestClass()
                    .getRealClass()
                    .getField("driver")
                    .get(result.getInstance());
            test.addScreenCaptureFromPath(
                    getScreenshot(result.getMethod().getMethodName(), driver),
                    result.getMethod().getMethodName()
            );
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
    }
}

```

- We use `onTestStart` to initialize the test report for the particular test case, we obtain from the `ITestResult` the method name, this will be the test case name in our report
- We use `onTestSuccess` to report the success of the test that is running
- We use `onTestFailure` to report the test failure and attach a screenshot of the failure to the report
- We use `onFinish` to generate the report once all the test have ran

#### Grouping tests
For example you want to mark some test for Smoke or some to run only in the regression pipeline. For this you use groups `@Test(groups = { "smoke" })` and in the testng_smoke.xml you use the groups tag like this:
> Warning: This will run the TestNG annotations that are marked with the group tag, that means that in our case it will not run the methods marked as @BeforeClass or @BeforeMethod or @AfterClass... etc. One way to overcome this is to add the `alwaysRun = true` to the annotation like this: `@BeforeClass(alwaysRun = true)`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Testing mobile Apps">
    <listeners>
        <listener class-name="org.example.testutils.NGListeners"></listener>
    </listeners>
    <test name="Android Tests">
        <groups>
            <run>
                <include name="smoke"></include>
            </run>
        </groups>
        <classes>
            <class name="org.example.android.GeneralStoreAPKExampleTests"/>
        </classes>
    </test> <!-- Test -->
</suite> <!-- Suite -->
```


### Trigger test using maven

> More info here: https://maven.apache.org/surefire/maven-surefire-plugin/examples/testng.html

Create a new profile in your pom.xml file for each suite (testNG xml file) that you have, in my case I have two xml one that runs all the test and another one that runs test in the smoke group:

The complete pom.xml file looks like this

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>Appium-java-framework</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Appium-java-framework</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>15</maven.compiler.source>
    <maven.compiler.target>15</maven.compiler.target>
  </properties>

  <dependencies>
    <dependency>
      <groupId>io.appium</groupId>
      <artifactId>java-client</artifactId>
      <version>8.3.0</version>
      <exclusions>

        <exclusion>
          <groupId>org.seleniumhq.selenium</groupId>
          <artifactId>selenium-remote-driver</artifactId>
        </exclusion>

        <exclusion>
          <groupId>org.seleniumhq.selenium</groupId>
          <artifactId>selenium-support</artifactId>
        </exclusion>

        <exclusion>
          <groupId>org.seleniumhq.selenium</groupId>
          <artifactId>selenium-api</artifactId>
        </exclusion>
      </exclusions>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.testng/testng -->
    <dependency>
      <groupId>org.testng</groupId>
      <artifactId>testng</artifactId>
      <version>7.7.1</version>
    </dependency>

    <dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-java</artifactId>
      <version>4.8.1</version>
    </dependency>

    <dependency>
      <groupId>io.github.bonigarcia</groupId>
      <artifactId>webdrivermanager</artifactId>
      <version>5.3.2</version>
    </dependency>

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

    <!-- https://mvnrepository.com/artifact/com.aventstack/extentreports -->
    <dependency>
      <groupId>com.aventstack</groupId>
      <artifactId>extentreports</artifactId>
      <version>5.0.9</version>
    </dependency>

  </dependencies>

  <profiles>
    <profile>
      <id>Regression</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.0.0</version>
            <configuration>
              <suiteXmlFiles>
                <suiteXmlFile>testng.xml</suiteXmlFile>
              </suiteXmlFiles>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>

    <profile>
      <id>Smoke</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.0.0</version>
            <configuration>
              <suiteXmlFiles>
                <suiteXmlFile>testng_smoke.xml</suiteXmlFile>
              </suiteXmlFiles>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
</project>

```

Run the profile you want like this (yes without a space between  the -p and the profile id)

```bash
mvn test -PRegression
```

