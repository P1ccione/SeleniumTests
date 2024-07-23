import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;
import java.util.List;

public class FirstSeleniumTests {

        private WebDriver driver;
        private WebDriverWait webDriverWait;
        private Actions actions;

        @BeforeAll
        public static void setUpClass() {
                WebDriverManager.chromedriver().setup();
        }

        @BeforeEach
        public void setUp() {
                driver = new ChromeDriver();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                actions = new Actions(driver);
        }

        @Test
        public void openPageTest() throws InterruptedException  {
                driver.get("https://todomvc.com/");

                //Thread.sleep(5000);

                Assertions.assertEquals("TodoMVC", driver.getTitle());

                WebElement link = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Vue.js")));
                link.click();

                //Thread.sleep(5000);

                Assertions.assertTrue(driver.getPageSource().contains("What needs to be done?"));

                addNewItem("Item 1");
                addNewItem("Item 2");

                //WebElement items = driver.findElement(By.xpath("//label[text()='Item 1']/preceding-sibling::input"));
                //items.click();
                //System.out.println(items);

                Thread.sleep(5000);

                WebElement checkBox1 = getItemCheckbox("Item 1");
                checkBox1.click();

                Integer expectedItemsLeft = 1;

                WebElement actualItemsLeft = driver.findElement(By.cssSelector(".todo-count strong"));

                Assertions.assertEquals(expectedItemsLeft, Integer.parseInt(actualItemsLeft.getText()));
        }

        private void addNewItem(String item) {
                WebElement toDoInput = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.className("new-todo")));
                toDoInput.sendKeys(item);
                actions.click(toDoInput).sendKeys(Keys.ENTER).perform();
        }

        private WebElement getItemCheckbox(String item) {
                String xpathLocator = String.format("//label[text()='%s']/preceding-sibling::input", item);
                return webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator)));
        }

        @Test
        public void loginTest() throws InterruptedException {
                driver.get("https://katalon-demo-cura.herokuapp.com/");

                WebElement burgerMenu = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("menu-toggle")));
                burgerMenu.click();

                Thread.sleep(2000);

                WebElement loginLink = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Login")));
                loginLink.click();

                Thread.sleep(5000);

                Assertions.assertTrue(driver.getPageSource().contains("Please login to make appointment."));

                WebElement userNameInputValue = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"login\"]/div/div/div[2]/form/div[1]/div[1]/div/div/input")));
                String username = userNameInputValue.getAttribute("value");

                WebElement passwordInputValue = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"login\"]/div/div/div[2]/form/div[1]/div[2]/div/div/input")));
                String password = passwordInputValue.getAttribute("value");

                WebElement usernameInput = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("txt-username")));
                WebElement passwordInput = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("txt-password")));

                usernameInput.sendKeys(username);
                passwordInput.sendKeys(password);

                Thread.sleep(2000);

                WebElement loginBtn = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn-login")));
                loginBtn.click();

                Assertions.assertTrue(driver.getPageSource().contains("Logout"));

        }

        @AfterEach
        public void tearDown() {
                if(driver != null) {
                        driver.quit();
                }
        }

}
