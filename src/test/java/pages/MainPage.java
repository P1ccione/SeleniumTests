package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.Assertions;

public class MainPage{

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final Actions actions;

    // Locators

    // Constructor
    public MainPage(WebDriver driver, WebDriverWait webDriverWait, Actions actions) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.actions = actions;
    }

    // Page interaction methods
    public void openTechnologyApp(String technologyName) {
        WebElement technologyLink = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(technologyName)));
        technologyLink.click();
    }

    // Assertions
    public void assertPageTitle() {
        Assertions.assertEquals("TodoMVC", driver.getTitle());
    }


}
