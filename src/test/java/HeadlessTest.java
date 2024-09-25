import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class HeadlessTest {
    private static final Logger logger = LogManager.getLogger(HeadlessTest.class);
    private org.openqa.selenium.WebDriver driver;

    @BeforeAll
    public static void driverSetup() {
        WebDriverManager.chromedriver().setup();
        logger.info("Установка драйвера");

    }

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        logger.info("Установка режима открытия");
        driver = new ChromeDriver(options);
        String url = "https://duckduckgo.com/";
        logger.info("Значение url");
        driver.get(url);
    }


    @Test
    public void openBrowserInHeadlessFormat() {
        WebElement searchInput = driver.findElement(By.xpath("//input[@name='q']"));
        logger.info("Поле ввода выбрано");
        searchInput.sendKeys("ОТУС");
        logger.info("Вставка текста");
        WebElement enterButton = driver.findElement(By.xpath("//*[@type='submit']"));
        enterButton.click();
        logger.info("Поиск");
        WebElement firstResult = driver.findElement(By.xpath("//a[contains(span/text(), " +
                "'Онлайн‑курсы для профессионалов, дистанционное обучение современным ...')]"));
        logger.info("Получение первого результата на странице");
        String firstResultText = firstResult.getText();
        logger.info("Передаем текст из ссылки в текстовую переменную");
        String expectedResult = "Онлайн‑курсы для профессионалов, дистанционное обучение современным ...";
        Assertions.assertEquals(expectedResult, firstResultText);
        logger.info("Сравнение ожидаемого и фактического результатов");
        logger.info("Конец теста");

    }

    @AfterEach
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
        logger.info("Закрытие драйвера");
    }

}
