import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;


public class Tests {
    WebDriver _globalDriver;
    String _email = new String();
    String _password = new String();
    Actions actions;
    @BeforeClass
    public static String generateRandomEmail() {
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "example.com"};
        String[] characters = {"abcdefghijklmnopqrstuvwxyz", "0123456789"};

        Random random = new Random();
        StringBuilder email = new StringBuilder();

        // Generate username part
        int usernameLength = random.nextInt(10) + 5; // Random length between 5 to 14 characters
        for (int i = 0; i < usernameLength; i++) {
            String characterSet = characters[random.nextInt(2)]; // Selecting either alphabets or numbers
            char randomChar = characterSet.charAt(random.nextInt(characterSet.length()));
            email.append(randomChar);
        }

        // Adding '@' symbol
        email.append("@");

        // Selecting random domain
        String randomDomain = domains[random.nextInt(domains.length)];
        email.append(randomDomain);

        return email.toString();
    }

    @BeforeClass
    public static String generateRandomUserame() {
        String[] characters = {"abcdefghijklmnopqrstuvwxyz", "0123456789"};

        Random random = new Random();
        StringBuilder username = new StringBuilder();

        // Generate username part
        int usernameLength = random.nextInt(10) + 5; // Random length between 5 to 14 characters
        for (int i = 0; i < usernameLength; i++) {
            String characterSet = characters[random.nextInt(2)]; // Selecting either alphabets or numbers
            char randomChar = characterSet.charAt(random.nextInt(characterSet.length()));
            username.append(randomChar);
        }
        return username.toString();
    }

    @BeforeClass
    public void SetupUserDetails() {
        _email = generateRandomEmail();
        _password = generateRandomUserame();

    }


    /*public static CompletableFuture<Boolean> asyncClick(WebDriver driver, By locator) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                WebElement element = new WebDriverWait(driver, Duration.ofSeconds(30))
                        .until(ExpectedConditions.elementToBeClickable(locator));
                element.click();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }*/

    @BeforeTest
    public void SetupWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        _globalDriver = new ChromeDriver(options);
        _globalDriver.get("https://www.audimas.lt/");
        _globalDriver.manage().window().maximize();
        snoozeUntilXpathClickable("/html/body/div[1]/div/div[4]/div[1]/div[2]/button[4]").click();//close cookies
        snoozeUntilXpathClickable("/html/body/div[8]/div/div/div/button").click();//close popup ad
//        asyncClick(_globalDriver, By.xpath("/html/body/div[8]/div/div/div/button")).thenAccept(x -> {
//            System.out.println("Ad is closed");
//        });
        actions = new Actions(_globalDriver);
    }

    public WebElement snoozeUntilID(String elementId){
        WebDriverWait wait = new WebDriverWait(_globalDriver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementId)));
        return element;
    }
    public WebElement snoozeUntilXpath(String elementXP){
        WebDriverWait wait = new WebDriverWait(_globalDriver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXP)));
        return element;
    }

    public WebElement snoozeUntilXpathClickable(String elementXP){
        WebElement element = snoozeUntilXpath(elementXP);
        WebDriverWait wait = new WebDriverWait(_globalDriver, Duration.ofSeconds(10));
       wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementXP)));
        return element;
    }

    @Test//register
    public void testTC0101() {
        _globalDriver.findElement(By.xpath("/html/body/div[4]/header/div/div[3]/div[2]/a")).click();//open register window
        snoozeUntilXpathClickable("/html/body/div[11]/div/div/div/div/div/div/div/div/div[2]/a").click();//open create new account
        snoozeUntilXpathClickable("/html/body/div[11]/div/div/div/div/div/div/form/div[1]/input").sendKeys(_email);//write random email
        _globalDriver.findElement(By.xpath("/html/body/div[11]/div/div/div/div/div/div/form/div[2]/input")).sendKeys(_password);//write random password
        _globalDriver.findElement(By.xpath("/html/body/div[11]/div/div/div/div/div/div/form/div[3]/input")).sendKeys(_password);//write random password
        _globalDriver.findElement(By.id("i_privacy")).click();//agree with terms and conditions
        _globalDriver.findElement(By.xpath("/html/body/div[11]/div/div/div/div/div/div/form/div[6]/button")).click();//register
        _globalDriver.close();
    }

    @Test//filter by discount
    public void testTC0201() {
        snoozeUntilXpathClickable("/html/body/div[4]/header/div/nav/div/ul/li[5]/div/h4").click();//open sale
        _globalDriver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div/div/div/div[3]/div[1]/div[1]/div/div[1]/div/div[1]/div[1]/h5")).click();//open filter
        _globalDriver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div/div/div/div[3]/div[1]/div[1]/div/div[1]/div/div[1]/div[1]/ul/li[2]")).click();//discount decreasing
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int firstDisc = Integer.parseInt(_globalDriver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div/div/div/div[3]/div[2]/div[1]/div[1]/a/span[2]/span[1]/span[2]")).getText().replaceAll("[-% ]", ""));
        int secondDisc = Integer.parseInt(_globalDriver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div/div/div/div[3]/div[2]/div[1]/div[9]/a/span[2]/span[1]/span[2]")).getText().replaceAll("[-% ]", ""));
        int thirdDisc = Integer.parseInt(_globalDriver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div/div/div/div[3]/div[2]/div[1]/div[17]/a/span[2]/span[1]/span[2]")).getText().replaceAll("[-% ]", ""));
        int forthDisc = Integer.parseInt(_globalDriver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div/div/div/div[3]/div[2]/div[1]/div[26]/a/span[2]/span[1]/span[2]")).getText().replaceAll("[-% ]", ""));
        Assert.assertTrue(firstDisc >= secondDisc && secondDisc >= thirdDisc && thirdDisc >= forthDisc);
    }

    @Test//reassure that women's jackets can be opened
    public void testTC0301() {
        WebElement element = _globalDriver.findElement(By.xpath("/html/body/div[4]/header/div/nav/div/ul/li[1]/div[2]/h4"));
        Actions actions1 = actions.moveToElement(element);
        actions1.perform();
        _globalDriver.findElement(By.xpath("/html/body/div[4]/header/div/nav/div/ul/li[1]/div[2]/div/div/div/div/div[2]/div/div/div/ul/li[3]/a")).click();//open womens' jackets
        WebElement result = _globalDriver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div/div/div/h1"));
        String jacketTitle = result.getText();
        Assert.assertEquals("Striukės moterims", jacketTitle);
        _globalDriver.close();
    }
    


}
