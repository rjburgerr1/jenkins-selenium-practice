package com.selenium;

import com.sun.source.tree.AssertTree;
import org.apache.openmeetings.util.stringhandlers.StringComparer;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Test
public class Tests {
    private ChromeDriver driver;
    private Practice p1;
    @BeforeClass
    public void getDriver() {
        // Set system property for allowing use of chrome driver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\RJ - Laptop\\Desktop\\Selenium Driver\\chromedriver.exe");
        // Instantiate headless chrome driver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        // Instantiate the practice class
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

    /**
     * Tests the presence of a clothing navbar at "www.automationpractice.com"
     */
    public void verifyClothingNavbar() {
        // Navigate to page
        driver.get("http://automationpractice.com/index.php");

        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);


        WebElement clothingNavbar = wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return    p1.getClothingNavbar(driver);
            }
        });

        // Pass test if the clothingNavbar element is a WebElement and not null
        Assert.assertTrue(clothingNavbar instanceof WebElement);

    }


    /**
     * Tests the presence of a clothing navbar at "www.automationpractice.com"
     */
    public void verifyClothingNavbarContent() {
        try {
            // Navigate to page
            driver.get("http://automationpractice.com/index.php");

            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofSeconds(10))
                    .pollingEvery(Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class);


            WebElement[] clothingNavbarContent = wait.until(new Function<WebDriver, WebElement[]>() {
                @Override
                public WebElement[] apply(WebDriver driver) {
                    return p1.getClothingNavbarContent(driver);
                }
            });
            List elementTexts = new ArrayList();

            for (WebElement element : clothingNavbarContent) {
                elementTexts.add(element.getText());
            }

            Assert.assertTrue(containsIgnoreCase(elementTexts, "dresses"));
            Assert.assertTrue(containsIgnoreCase(elementTexts, "t-shirts"));
            Assert.assertTrue(containsIgnoreCase(elementTexts, "women"));

        } catch (Exception error) {
            System.out.println(error.getCause());
        }


        // Pass test if the clothingNavbar element is a WebElement and not null
       // Assert.assertTrue(clothingNavbar instanceof WebElement);

    }


    /**
     * Method to find string match in list, ignoring case
     * @param list
     * @param soughtFor
     * @return
     */
    private boolean containsIgnoreCase(List<String> list, String soughtFor) {
        for (String current : list) {
            if (current.equalsIgnoreCase(soughtFor)) {
                return true;
            }
        }
        return false;
    }


    // Closing the browser session after completing each test case
    @AfterTest
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }



}
