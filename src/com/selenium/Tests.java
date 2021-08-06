package com.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Test
public class Tests {
    private ChromeDriver driver;
    private Practice p1;
    @BeforeClass
    public void getDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\rjbra\\Desktop\\Selenium Jars and Drivers\\Selenium Driver Chrome\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
         driver = new ChromeDriver(options);

        p1 = new Practice();
    }

    public void signUpTest() {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        p1.signUpWithAccount(driver);

        WebElement signUpCompletion = wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.className("dashboard-container"));
            }
        });


        String URL = driver.getCurrentUrl();
        Assert.assertEquals(URL, "http://localhost:3000/" );
    }

    public void addToCartTest() {
       p1.addToCart(driver);

       Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);


        WebElement addToCartCompletion = wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return   driver.findElement(By.xpath("/html/body/div/div[1]/header/div[3]/div/div/div[4]/div[1]/div[1]/h2"));
            }
        });

        if (addToCartCompletion != null) {
            Assert.assertTrue(true);
        }

    }




    // Closing the browser session after completing each test case
    @AfterTest
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }



}
