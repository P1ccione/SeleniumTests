package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;
import pages.ToDoPage;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PageObjectModelTests {

    private WebDriver driver;
    private WebDriverWait webDriverWait;
    private Actions actions;
    private MainPage mainPage;
    private ToDoPage toDoPage;

    @BeforeAll
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        mainPage = new MainPage(driver, webDriverWait, actions);
        toDoPage = new ToDoPage(driver, webDriverWait, actions);
    }

    static Stream<Arguments> stringProvider() {
        return Stream.of(
                arguments("Vue.js", List.of("item1", "item2"), List.of("item1"), 1),
                arguments("provaErrore", List.of("item1", "item2"), List.of("item2"), 1),
                arguments("Vue.js", List.of("item1", "item2"), List.of("item2"), 2)
        );
    }

    @ParameterizedTest(name = " N° {index}")
    @Description("Questo test prova a creare degli item in una ToDo list, spuntarne alcuni e assicurarsi che siano stati creati con successo")
    @Owner("Simone Piscozzo")
    @Link(name = "Website", url = "https://todomvc.com/")
    @MethodSource("stringProvider")
    @Epic("TodoMVC")
    @Feature("ToDo List")
    @Story("Utente crea e spunta delle task")
    @Severity(SeverityLevel.CRITICAL)
    public void openPageTest(
            String technology,
            List<String> itemsTodo,
            List<String> itemsToCheck,
            Integer expectedItemsLeft
    ) throws InterruptedException {
        try {
            Allure.parameter("technology", technology);
            Allure.parameter("prova", "prova");
            Allure.parameter("itemsTodo", itemsTodo);
            Allure.parameter("itemsToCheck", itemsToCheck);
            Allure.parameter("expectedItemsLeft", expectedItemsLeft);

            Allure.step("Navigare a TodoMVC");
            driver.get("https://todomvc.com/");

            Thread.sleep(2000);

            Allure.step("Assertion - controllo titolo pagina");
            mainPage.assertPageTitle();

            Allure.step("Aprire la tecnologia specifica " + technology);
            mainPage.openTechnologyApp(technology);

            Thread.sleep(2000);

            Allure.step("Assertion - controllo presenza testo nella pagina");
            toDoPage.assertTodoPageSource("What needs to be done?");

            Allure.step("Aggiungo delle task alla lista", step -> {
                itemsTodo.forEach(item -> {
                    Allure.step("Aggiungo un nuovo item " + item);
                    toDoPage.addNewItem(item);
                });
            });

            Thread.sleep(5000);

            Allure.step("Spunto delle task nella lista", step -> {
                itemsToCheck.forEach(item -> {
                    Allure.step("Spunto l'item " + item);
                    toDoPage.checkItem(item);
                });
            });

            Allure.step("Verificare il numero di item rimasti");
            toDoPage.assertChecked(expectedItemsLeft);

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
