package MonolitSauceDemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.lang.reflect.Method;
import java.time.Duration;

public class SauceDemoLoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void beforeClass(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("-incognito");

       driver = new ChromeDriver(options);
       wait = new WebDriverWait(driver , Duration.ofSeconds(10));
    }

    @AfterClass
    public void afterClass(){
        driver.quit();
    }


    @BeforeMethod
    public void beforeMethod(Method method){
        System.out.println(method.getName()+" : "+"started");
        driver.get("https://www.saucedemo.com/");
    }

    @AfterMethod
    public void afterMethod(ITestResult iTestResult){
        System.out.println(iTestResult.getMethod().getMethodName()+" : "+ "Test finished");
    }


    @Test(priority = 1)
    public void positiveLoginWithStandartUser(){
        WebElement userName = driver.findElement(By.id("user-name"));
        WebElement passWord = driver.findElement(By.id("password"));
        WebElement logIn = driver.findElement(By.id("login-button"));

        userName.clear();
        userName.sendKeys("standard_user");
        passWord.clear();
        passWord.sendKeys("secret_sauce");

        logIn.click();

        WebElement title = driver.findElement(By.className("title"));
        String titleText = title.getText();
        Assert.assertEquals(titleText, "Products", "Error");
    }


    @Test(priority = 2)
    public void negativeLoginWithEmptyUserName(){
        WebElement userName = driver.findElement(By.id("user-name"));
        WebElement passWord = driver.findElement(By.id("password"));
        WebElement logIn = driver.findElement(By.id("login-button"));

        userName.clear();
        userName.sendKeys("");
        passWord.clear();
        passWord.sendKeys("secret_sauce");
        logIn.click();

        WebElement error = driver.findElement(By.cssSelector("h3[data-test='error']"));
        String errorText = error.getText();
        Assert.assertEquals(errorText, "Epic sadface: Username is required", "Error");
    }


    @Test(priority = 3)
    public void negativeLoginWithEmptyPassword(){
        WebElement userName = driver.findElement(By.id("user-name"));
        WebElement passWord = driver.findElement(By.id("password"));
        WebElement logIn = driver.findElement(By.id("login-button"));

        userName.clear();
        userName.sendKeys("standard_user");
        passWord.clear();
        passWord.sendKeys("");

        logIn.click();

        WebElement error = driver.findElement(By.cssSelector("h3[data-test='error']"));
        String errorText = error.getText();
        Assert.assertEquals(errorText, "Epic sadface: Password is required", "Error");
    }


    @Test(priority = 4)
    public void negativeloginWithWrongUsername(){
        WebElement userName = driver.findElement(By.id("user-name"));
        WebElement passWord = driver.findElement(By.id("password"));
        WebElement logIn = driver.findElement(By.id("login-button"));

        userName.clear();
        userName.sendKeys("test");
        passWord.clear();
        passWord.sendKeys("secret_sauce");
        logIn.click();

        WebElement error = driver.findElement(By.cssSelector("h3[data-test='error']"));
        String errorText= error.getText();
        Assert.assertEquals(errorText, "Epic sadface: Username and password do not match any user in this service", "Error");
    }


    @Test(priority = 5)
    public void negativeLoginWithWrongPassword(){
        WebElement userName = driver.findElement(By.id("user-name"));
        WebElement passWord = driver.findElement(By.id("password"));
        WebElement logIn = driver.findElement(By.id("login-button"));

        userName.clear();
        userName.sendKeys("standard_user");
        passWord.clear();
        passWord.sendKeys("test");
        logIn.click();

        WebElement error = driver.findElement(By.cssSelector("h3[data-test='error']"));
        String errorText = error.getText();
        Assert.assertEquals(errorText, "Epic sadface: Username and password do not match any user in this service");
    }




    
    @DataProvider (name = "Login")
    public Object[][] getData(){
        return new Object[][]{
                {"standard_user","secret_sauce"},
                {"locked_out_user","secret_sauce"},
                {"problem_user","secret_sauce"},
                {"performance_glitch_user","secret_sauce"},
                {"error_user","secret_sauce"},
                {"visual_user","secret_sauce"},
        };
    }
    @Test(priority = 6, dataProvider = "Login")
    public void loginAllUsers(String username, String password) throws InterruptedException {

        WebElement userName = driver.findElement(By.id("user-name"));
        WebElement passWord = driver.findElement(By.id("password"));
        WebElement logIn = driver.findElement(By.id("login-button"));

        userName.clear();
        userName.sendKeys(username);
        passWord.clear();
        passWord.sendKeys(password);
        logIn.click();
//        Thread.sleep(3000);

        if (username.equals("locked_out_user")) {
            WebElement error = driver.findElement(By.cssSelector("h3[data-test='error']"));
            String errorText = error.getText();
            Assert.assertEquals(errorText, "Epic sadface: Sorry, this user has been locked out.", "error");
        }
    }
}
