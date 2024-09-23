import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class KioskModeTest {

    private static final Logger logger = LogManager.getLogger(KioskModeTest.class);
    private WebDriver driver;

    @BeforeAll
    public static void driverSetup() {
        WebDriverManager.chromedriver().setup();
        logger.info("Драйвер установлен");
    }

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--kiosk");
        logger.info("Открытие сайта в режиме киоск :)");
        driver = new ChromeDriver(options);
        String url = "https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/" +
                "685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818";
        driver.get(url);
    }


    @Test
    public void openBrowserInMaximizedSize() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Явное ожидание
        logger.info("Ждем загрузку");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@data-id='id-1']")));
        logger.info("ВЫбираем первую картинку");
        WebElement selectPic = driver.findElement(By.xpath("//li[@data-id='id-1']"));
        selectPic.click();
        logger.info("Кликаем на картинку");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='pp_hoverContainer']")));
        WebElement openedPic = driver.findElement(By.xpath("//*[@class='pp_hoverContainer']"));
        logger.info("Проверяем что картинка открылась в модальном окне");
        Assertions.assertTrue(openedPic.isDisplayed(), "Clicked picture is not opened");
    }

    @AfterEach
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
        logger.info("Закрытие драйвера и браузера");
    }
}