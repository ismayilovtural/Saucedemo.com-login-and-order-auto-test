package MonolitSauceDemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Locale;

public class SauceDemoOrderTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void beforeClass() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("-incognito");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }


    @AfterClass
    public void afterClass() {
        driver.quit();
    }


    @BeforeMethod
    public void beforeMethod(ITestResult iTestResult) {
        System.out.println(iTestResult.getMethod().getMethodName() + "started");
        driver.get("https://www.saucedemo.com/");

        By username = By.id("user-name");
        By password = By.id("password");

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(username));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(password));
    }

    @AfterMethod
    public void afterMothod(Method method) {
        System.out.println(method.getName() + " finished.");
    }


    @Test
    public void orderTest() throws InterruptedException {
        WebElement userName = driver.findElement(By.id("user-name"));
        WebElement passWord = driver.findElement(By.id("password"));
        WebElement logIn = driver.findElement(By.id("login-button"));


        userName.clear();
        userName.sendKeys("standard_user");
        passWord.clear();
        passWord.sendKeys("secret_sauce");

        logIn.click();


        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("item_5_title_link")));


        WebElement jacketModule = driver.findElement(By.xpath("//div[text()='Sauce Labs Fleece Jacket']"));

        jacketModule.click();

        WebElement jacketNameInAddCard = driver.findElement(By.xpath("//div[text()='Sauce Labs Fleece Jacket']"));
        WebElement jacketPriceInAddCard = driver.findElement(By.xpath("//div[text()='49.99']"));

        String jacketNameInAddCardText = jacketNameInAddCard.getText();
        String jacketPriceInAddCardText = jacketPriceInAddCard.getText();

        System.out.println(jacketNameInAddCardText);
        System.out.println(jacketPriceInAddCardText);

        Assert.assertEquals(jacketNameInAddCardText , "Sauce Labs Fleece Jacket");
        Assert.assertEquals(jacketPriceInAddCardText , "$49.99");



        driver.findElement(By.id("add-to-cart")).click();

        driver.findElement(By.id("shopping_cart_container")).click();

        driver.findElement(By.id("checkout")).click();

        driver.findElement(By.id("first-name")).sendKeys("Tural");
        driver.findElement(By.id("last-name")).sendKeys("Ismayilov");
        driver.findElement(By.id("postal-code")).sendKeys("AZ1000");
        driver.findElement(By.id("continue")).click();

        WebElement endOrderPrice = driver.findElement(By.xpath("//div[text()='49.99']"));
        WebElement endOrderTax = driver.findElement(By.xpath("//div[text()='4.00']"));
        WebElement totalPrice = driver.findElement(By.xpath("//div[text()='53.99']"));

        String endOrderPriceText = endOrderPrice.getText();
        String endOrderTaxText = endOrderTax.getText();
        String totalPriceText = totalPrice.getText();

        String priceCleanText = endOrderPriceText.replace("$","").trim();
        String taxCleanText = endOrderTaxText.replace("Tax:","").replace("$","").trim();
        String totalCleanText = totalPriceText.replace("Total:","").replace("$","").trim();

        System.out.println(priceCleanText);
        System.out.println(taxCleanText);
        System.out.println(totalCleanText);

        double pirce = Double.parseDouble(priceCleanText);
        double tax = Double.parseDouble(taxCleanText);
        double total = Double.parseDouble(totalCleanText);

        Assert.assertEquals(pirce+tax, total, "Xeta");

        if (pirce+tax==total) {
            driver.findElement(By.id("finish")).click();
        }else {
            driver.findElement(By.id("cancel")).click();
        }
        driver.findElement(By.id("back-to-products")).click();
    }
}
