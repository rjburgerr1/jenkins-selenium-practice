package com.selenium;

import org.openqa.selenium.By;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class Practice {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }


    public void practiceExercise1(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Long.parseLong("1000"));
        try {
            driver.get("https://google.com/ncr");
            driver.findElement(By.name("q")).sendKeys("cheese" + Keys.ENTER);
            WebElement firstResult = wait.until(presenceOfElementLocated(By.cssSelector("h3")));
            System.out.println(firstResult.getAttribute("textContent"));
        } finally {
            driver.quit();
        }
    }

    // Practice browser manipulation by signing up for personal webapp
    public WebElement signUpWithAccount(WebDriver driver) {
        WebElement element = null;
        String randomEmail = randomString(15) + "@email.com";
        String randomUsername = randomString(8);
        String randomPassword = randomString(10);

        try {
            // Navigate to page
            driver.get("http://localhost:3000");

            // Find signup button on landing page and click it to open signup form
            driver.findElement(By.id("signUp")).click();
            // Alternate way to click signup button
            //driver.findElement(By.id("signUp")).sendKeys(Keys.ENTER);

            // Enter information in signup form
            driver.findElement(By.name("email")).sendKeys(randomEmail + Keys.TAB);
            driver.findElement(By.name("password")).sendKeys(randomPassword + Keys.TAB);
            driver.findElement(By.name("username")).sendKeys(randomUsername + Keys.TAB);

            // My signup button doesnt have a unique identifier, so exercising my right to find elements a different way, xpath
            //WebElement element = driver.findElement(By.xpath("//*[@id=\"container\"]/div[1]/form/button"));
            //element.sendKeys(Keys.ENTER);

            // Instead of using xpath, use cssSelector if possible because it has faster performance
           element = driver.findElement(By.cssSelector("#container > div.form-container.sign-up-container > form > button"));

          element.sendKeys(Keys.ENTER);


        } catch (Error error) {

            System.out.println("Error:");
            System.out.println(error);
        } finally {
          //driver.quit();
        }
        return element;
    }

    // Practice selenium browser maniupulation on automation practice site.
    public void addToCart(WebDriver driver) {
        WebElement cartElement = null;
        // Creating a list
        List<WebElement> elements
                = new ArrayList<WebElement>();

        try {
            // Navigate to page
            driver.get("http://automationpractice.com/index.php");
            WebElement addToCartElement = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[1]/ul[1]/li[1]/div/div[2]/div[2]/a[1]/span"));
            addToCartElement.click();
           // driver.get("http://automationpractice.com/index.php?controller=order");

          //  cartElement = driver.findElement(By.id("summary_products_quantity"));
            /*
            WebElement element = driver.findElement(By.className("button-container"));
            System.out.println(element);

            elements = (element.findElements(By.tagName("span")));
            //element = driver.findElement(By.linkText("Add to Cart"));

            // For loop for iterating over the List
            for (int i = 0; i < elements.size(); i++) {
                System.out.println(elements.get(i).getText());
                if (elements.get(i).getText() == "Add to Cart") {
                    addToCartElement = elements.get(i);
                        System.out.println("Found add to cart button");
                    addToCartElement.sendKeys(Keys.ENTER);
                }
                // Print all elements of List
                System.out.println(elements.get(i));
            }
            */
        } catch (Error error) {

            System.out.println("Error:");
            System.out.println(error);
        } finally {
            //driver.quit();
        }
    }

    /**
     * Finds the clothing navbar by cssSelector
     * @param driver
     * @return
     */
    public WebElement getClothingNavbar(WebDriver driver) {

        try {
            WebElement navbarElement = driver.findElement(By.cssSelector("#block_top_menu > ul"));
            return navbarElement;
        } catch (Exception error) {
            System.out.println(error.getCause());
        }
        return null;
    }

    /**
     * Returns all the content contained in the clothing navbar
     * @param driver
     * @return
     */
    public WebElement[] getClothingNavbarContent(WebDriver driver) {

        try {
            WebElement navbarElement = driver.findElement(By.cssSelector("#block_top_menu > ul"));
            WebElement[] navbarElementContent = navbarElement.findElements(By.tagName("li")).toArray(new WebElement[0]);

            return navbarElementContent;
        } catch (Exception error) {
            System.out.println(error.getCause());
        }
        return null;
    }










}
