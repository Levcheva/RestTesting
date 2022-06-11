package seleniumTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class HerokuAppTests {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().maximize();
        actions = new Actions(driver);
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

    @Test
    public void addRemoveElements() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        List<WebElement> elementsContainer = driver.findElements(By.xpath("//div[@id='elements']/descendant::*"));
        Assert.assertTrue(elementsContainer.isEmpty());

        WebElement addElementButton = driver.findElement(By.xpath("//button[@onclick='addElement()']"));

        for (int i = 0; i < 2; i++) {
            addElementButton.click();
        }
        elementsContainer = driver.findElements(By.xpath("//div[@id='elements']/descendant::*"));

        Assert.assertEquals(elementsContainer.size(), 2);
//        WebElement deleteButton = driver.findElement(By.cssSelector(".added-manually"));
    }

    @Test
    public void basicAuth() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");
        WebElement message = driver.findElement(By.xpath("//div[@class='example']/p"));
        Assert.assertEquals(message.getText(), "Congratulations! You must have the proper credentials.");
    }

    @Test
    public void dragAndDrop() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/drag_and_drop");

        WebElement elementA = driver.findElement(By.xpath("//div[@id='column-a']"));
        WebElement elementB = driver.findElement(By.xpath("//div[@id='column-b']"));

//        actions.moveToElement(elementA).clickAndHold(elementA).moveToElement(elementB).release(elementB).build().perform();

        actions.dragAndDrop(elementA, elementB).perform();
//        To do assert

    }

    @Test
    public void contextMenu() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/drag_and_drop");

        WebElement contextBox = driver.findElement(By.id("hot-spot"));
        actions.contextClick(contextBox);
        Alert alert = driver.switchTo().alert();

        String alertText = alert.getText();
        Assert.assertEquals(alertText, "You selected a context menu");
        alert.dismiss();
    }

    @Test
    public void checkBoxes() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/checkboxes");
        WebElement checkbox1 = driver.findElement(By.xpath("//form[@id='checkboxes']/input[1]"));
        WebElement checkbox2 = driver.findElement(By.xpath("//form[@id='checkboxes']/input[2]"));
        boolean checkbox1State = checkbox1.isSelected();
        boolean checkbox2State = checkbox2.isSelected();

        if (checkbox1State) {
            checkbox1.click();
        }else {
            checkbox1.click();

        }
        Assert.assertEquals(checkbox1State, checkbox1.isSelected());
    }

    @Test
    public void hovers() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/hovers");
        WebElement image1 = driver.findElement(By.xpath("(//div[@class='figure']/img)[1]"));
        WebElement viewProfileLink = driver.findElement(By.cssSelector("a[href='/users/1']"));
        Actions action = new Actions(driver);
        action.moveToElement(image1).perform();
        Assert.assertTrue(viewProfileLink.isDisplayed());
    }

    @Test
    public void multipleWindows() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/windows");
        WebElement clickHereLink = driver.findElement(By.cssSelector("a[href='/windows/new']"));
        clickHereLink.click();
        String parentWindow= driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for(String curWindow : allWindows){
            driver.switchTo().window(curWindow);
        }
        driver.switchTo().window(parentWindow);
    }

    @Test
    public void redirectLink() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/redirector");
        WebElement hereLink = driver.findElement(By.cssSelector("#redirect"));
        hereLink.click();
        String expectedURL = "https://the-internet.herokuapp.com/status_codes";
        String actualURL = driver.getCurrentUrl();
        Assert.assertEquals(actualURL, expectedURL);
    }
}
