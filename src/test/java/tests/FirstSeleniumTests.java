package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class FirstSeleniumTests {

        private WebDriver driver;
        private WebDriverWait webDriverWait;
        private Actions actions;

        @BeforeAll
        public static void setUpClass() {
                WebDriverManager.chromedriver().setup();
        }

        static Stream<Arguments> stringProvider() {
                return Stream.of(
                        arguments(List.of("item1", "item2"), List.of("item1")),
                        arguments(List.of("item1", "item2"), List.of("item2"))
                );
        }

        static Stream<Arguments> appointmentProvider() {
                return Stream.of(
                        arguments("Tokyo CURA Healthcare Center", true, "None", "23/07/2024", "A small comment"),
                        arguments("Seoul CURA Healthcare Center", false, "Medicaid", "12/07/2024", "")
                );
        }

        @BeforeEach
        public void setUp() {
                driver = new ChromeDriver();
                webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                actions = new Actions(driver);
        }

        @ParameterizedTest(name = " N° {index}")
        @MethodSource("stringProvider")
        public void openPageTest(
                List<String> itemsTodo,
                List<String> itemsToCheck
        ) throws InterruptedException {
                try {
                        driver.get("https://todomvc.com/");

                        //Thread.sleep(5000);

                        Assertions.assertEquals("TodoMVC", driver.getTitle());

                        WebElement link = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Vue.js")));
                        link.click();

                        //Thread.sleep(5000);

                        Assertions.assertTrue(driver.getPageSource().contains("What needs to be done?"));

                        itemsTodo.forEach(item -> {
                                addNewItem(item);
                        });

                        Thread.sleep(5000);

                        itemsToCheck.forEach(item -> {
                                WebElement checkBox = getItemCheckbox(item);
                                checkBox.click();
                        });

                        Integer expectedItemsLeft = 1;

                        WebElement actualItemsLeft = driver.findElement(By.cssSelector(".todo-count strong"));

                        Assertions.assertEquals(expectedItemsLeft, Integer.parseInt(actualItemsLeft.getText()));
                } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                }
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

        @ParameterizedTest(name = " N° {index}")
        @MethodSource("appointmentProvider")
        public void makeAppointmentTest(
                String facility,
                boolean isToCheck,
                String healthcareProgram,
                String visitDate,
                String comment
        ) throws InterruptedException {
                try {
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

                        WebElement facilitySelectElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("combo_facility")));
                        Select facilitySelect = new Select(facilitySelectElement);
                        facilitySelect.selectByVisibleText(facility);
                        WebElement selectedOption = facilitySelect.getFirstSelectedOption();
                        Assertions.assertEquals(facility, selectedOption.getText());

                        if (isToCheck) {
                                WebElement hospitalReadmissionCheckbox = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("chk_hospotal_readmission")));
                                hospitalReadmissionCheckbox.click();
                        }

                        String checkboxXpath = String.format("//input[@value='%s' and @type='radio']", healthcareProgram);
                        WebElement healthcareProgramCheckbox = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(checkboxXpath)));
                        healthcareProgramCheckbox.click();

                        WebElement dateInput = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("txt_visit_date")));
                        dateInput.sendKeys(visitDate);
                        Assertions.assertEquals(visitDate, dateInput.getAttribute("value"));

                        WebElement commentInput = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("txt_comment")));
                        commentInput.sendKeys(comment);

                        Thread.sleep(5000);

                        WebElement makeAppointmentBtn = webDriverWait.until((ExpectedConditions.presenceOfElementLocated(By.id("btn-book-appointment"))));
                        makeAppointmentBtn.click();

                        Assertions.assertTrue(driver.getPageSource().contains("Appointment Confirmation"));

                        WebElement appointmentFacility = webDriverWait.until((ExpectedConditions.presenceOfElementLocated(By.id("facility"))));
                        Assertions.assertEquals(facility, appointmentFacility.getText());

                        WebElement appointmentHospitalReadmission = webDriverWait.until((ExpectedConditions.presenceOfElementLocated(By.id("hospital_readmission"))));
                        boolean isChecked = Objects.equals(appointmentHospitalReadmission.getText(), "Yes");
                        Assertions.assertEquals(isToCheck, isChecked);

                        WebElement appointmentProgram = webDriverWait.until((ExpectedConditions.presenceOfElementLocated(By.id("program"))));
                        Assertions.assertEquals(healthcareProgram, appointmentProgram.getText());

                        WebElement appointmentVisitDate = webDriverWait.until((ExpectedConditions.presenceOfElementLocated(By.id("visit_date"))));
                        Assertions.assertEquals(visitDate, appointmentVisitDate.getText());

                        WebElement appointmentComment = webDriverWait.until((ExpectedConditions.presenceOfElementLocated(By.id("comment"))));
                        Assertions.assertEquals(comment, appointmentComment.getText());

                        Thread.sleep(5000);
                } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                }
        }

        @AfterEach
        public void tearDown() {
                if (driver != null) {
                        driver.quit();
                }
        }

}
