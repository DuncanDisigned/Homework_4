import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;

public class OtusAuthorization {
    private final static Logger logger = LogManager.getLogger(OtusAuthorization.class);
    private WebDriver driver;

    private Properties props = new Properties(); // Объект Properties в классе



    @BeforeAll
    public static void driverSetup() {
        WebDriverManager.chromedriver().setup();
    }


    @BeforeEach
    public void setup() {
        loadProperties();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        logger.info("Открытие сайта в режиме полного экрана");
        String url =  "https://otus.ru";
        driver.get(url);

    }
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Не удалось найти файл config.properties");
            } else {
                props.load(input); // Загрузка свойств
                logger.info("Свойства успешно загружены");
            }
        } catch (Exception e) {
            logger.error("Ошибка при загрузке свойств: {}", e.getMessage());
        }
    }

    @Test
    public void loginToOtus() {
        String username = props.getProperty("username"); // Получаем имя пользователя из properties
        String password = props.getProperty("password"); // Получаем пароль из properties

        WebElement signInButton = driver.findElement(By.xpath("//button[text()='Войти']"));
        signInButton.click();
        logger.info("Нажимаем на кнопку 'Войти'");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement email = driver.findElement(By.xpath("//*[@name='email']"));
        email.click();
        email.sendKeys(username);
        logger.info("Вводим емайл");

        WebElement password2 = driver.findElement(By.xpath("//input[@type='password']"));
        password2.click();
        password2.sendKeys(password);
        logger.info("Вводим пароль");

        WebElement signInButton2 = driver.findElement(By.cssSelector(".sc-9a4spb-0.eQlGvH.sc-11ptd2v-2-Component.cElCrZ"));
        signInButton2.click();
        logger.info("Авторизация прошла успешно");
        logger.trace(driver);
        logAllCookies();

    }

    private void logAllCookies() {
    Set<Cookie> cookies = driver.manage().getCookies(); // Получение всех cookie

        if (cookies.isEmpty()) {
            logger.info("Нет cookie для текущего сайта.");
        } else {
            for (Cookie cookie : cookies) {
                logger.info("Cookie: " +
                        "Name: " + cookie.getName() +
                        ", Value: " + cookie.getValue() +
                        ", Domain: " + cookie.getDomain() +
                        ", Path: " + cookie.getPath() +
                        ", Expiry: " + cookie.getExpiry() +
                        ", Is Secure: " + cookie.isSecure());

                logger.info("Куки загружены");

            }
        }
    }

    @AfterEach
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
        logger.info("Закрытие драйвера");

    }
}
