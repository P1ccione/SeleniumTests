package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ToDoPage{

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final Actions actions;

    // Locators
    private final By toDoInputLocator = By.className("new-todo");
    private final By itemsLeftCountLocator = By.cssSelector(".todo-count strong");

    // Constructor
    public ToDoPage(WebDriver driver, WebDriverWait webDriverWait, Actions actions) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.actions = actions;
    }

    // Page interaction methods
    public void addNewItem(String item) {
        WebElement toDoInput = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(toDoInputLocator));
        toDoInput.sendKeys(item);
        actions.click(toDoInput).sendKeys(Keys.ENTER).perform();
    }
    public void checkItem(String item) {
        String xpathLocator = String.format("//label[text()='%s']/preceding-sibling::input", item);
        WebElement checkbox = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator)));
        checkbox.click();
    }

    // Assertions
    public void assertTodoPageSource(String text) {
        Assertions.assertTrue(driver.getPageSource().contains(text));
    }
    public void assertChecked(Integer expectedItemsLeft) {
        WebElement actualItemsLeft = driver.findElement(itemsLeftCountLocator);
        Assertions.assertEquals(expectedItemsLeft, Integer.parseInt(actualItemsLeft.getText()));
    }

}
