package com.tus.individual_project.CoffeeShop.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8084"})
public class OrderTest {

    WebDriver driver;

    @BeforeEach
    void setup() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:8084");
    }

    @Test
    @DisplayName("Import not allowed if no file selected")
    public void testImportFailNoFile() {
        driver.findElement(By.id("orderB")).click();
        driver.findElement(By.id("customer-name")).sendKeys("Alex");
        driver.findElement(By.id("customer-email")).sendKeys("alex@gmail.com");
        driver.findElement(By.id("DOPPIO")).sendKeys("1");
        driver.findElement(By.id("confirmChoiceButton")).click();

        WebElement firstResult = new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(By.id("modal-header-text")));
        assertEquals("Success!", firstResult.getAttribute("innerHTML"));

        driver.findElement(By.className("close")).click();

        WebElement payOrder = new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(By.id("payOrderButton")));
        driver.findElement(By.id("cname")).sendKeys("A MARJANOVIC");
        driver.findElement(By.id("ccnum")).sendKeys("12345678954");
        driver.findElement(By.id("expmonth")).sendKeys("September");
        driver.findElement(By.id("expmonth")).sendKeys("September");
        driver.findElement(By.id("expyear")).sendKeys("2026");
        driver.findElement(By.id("cvv")).sendKeys("123");

        payOrder.click();

        WebElement orderPayed = new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(By.id("modal-header-text")));
        assertEquals("Success!", firstResult.getAttribute("innerHTML"));
    }

    @AfterEach
    public void tearDownClass() {
        driver.quit();
    }

}
