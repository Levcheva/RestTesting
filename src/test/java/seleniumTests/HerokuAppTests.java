package seleniumTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class HerokuAppTests {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    JavascriptExecutor js;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        // implicit wait - wait certain amount of time before throwing no such element exception
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().maximize();
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
        //explicit wait - add custom conditions, which should be met.
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void addRemoveElements() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        // List is now 0.
        List<WebElement> elementsContainer = driver.findElements(By.cssSelector(".added-manually"));
        Assert.assertTrue(elementsContainer.isEmpty());

        WebElement addElementButton = driver.findElement(By.xpath("//button[@onclick='addElement()']"));

        for (int i = 0; i < 3; i++) {
            addElementButton.click();
        }

        // Same list is now not 0 but 3.
        elementsContainer = driver.findElements(By.cssSelector(".added-manually"));

        // Click the delete elements button to create delete buttons
        for (WebElement deleteButton : elementsContainer
        ) {
            deleteButton.click();
        }

        List<WebElement> deleteButtonsAfter = driver.findElements(By.cssSelector(".added-manually"));

        Assert.assertEquals(deleteButtonsAfter.size(), 0);
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
    public void dropdown() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/dropdown");
        WebElement testDropDown = driver.findElement(By.id("dropdown"));
        Select dropdown = new Select(testDropDown);
        dropdown.selectByValue("1");
        WebElement selectedOption = driver.findElement(By.xpath("//select[@id='dropdown']/option[@selected='selected']"));
        Assert.assertEquals(selectedOption.getText(), "Option 1");
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
        } else {
            checkbox1.click();

        }
        Assert.assertEquals(checkbox1State, checkbox1.isSelected());
    }

    @Test
    public void floatingMenu() {
        driver.get("http://the-internet.herokuapp.com/floating_menu");
        // assert floating element is there when opening the page
        WebElement homeButton = driver.findElement(By.xpath("//div[@id='menu']//a[text()='Home']"));
        Assert.assertTrue(homeButton.isDisplayed());

        // scroll the page down
        js.executeScript("window.scrollBy(0,2000)");

        // assert floating element is still displayed after the scroll
        Assert.assertTrue(homeButton.isDisplayed());

        js.executeScript("window.scrollBy(0,-1000)");

        js.executeScript("arguments[0].click()", homeButton);

        wait.until(ExpectedConditions.visibilityOf(homeButton));

        // assert floating element is still displayed after the scroll
        Assert.assertTrue(homeButton.isDisplayed());
    }

    @Test
    public void dynamicControls() {
        driver.get("http://the-internet.herokuapp.com/dynamic_controls");

        // assert the checkbox is present after loading the page
        WebElement checkBox = driver.findElement(By.cssSelector("#checkbox"));
        Assert.assertTrue((checkBox.isDisplayed()));

        // click the remove button
        WebElement removeButton = driver.findElement(By.xpath("//button[text()='Remove']"));
        removeButton.click();

        // wait until the animation for removing the checkbox is gone
        WebElement loadingAnimation = driver.findElement(By.xpath("//div[@id='loading']"));
        wait.until(ExpectedConditions.invisibilityOf(loadingAnimation));

        WebElement message = driver.findElement(By.xpath("//p[@id='message']"));
        wait.until(ExpectedConditions.invisibilityOf(checkBox));
        Assert.assertEquals(message.getText(), "It's gone!");

        // fluent wait example
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement loadingBar = wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.xpath("//div[@id='loading']"));
            }
        });
    }

    @Test
    public void dynamicLoading() {
        driver.get("http://the-internet.herokuapp.com/dynamic_loading/2");

        By startButton = By.xpath("//div[@id='start']/button");
        By helloWorldText = By.xpath("//div[@id='finish']");

        WebElement startButtonWebElement = driver.findElement(startButton);
        startButtonWebElement.click();

        WebElement hellowWorldText = driver.findElement(helloWorldText);
    }

    @Test
    public void switchWindows() {
        driver.get("http://the-internet.herokuapp.com/windows");
        String firstWindowHandle = driver.getWindowHandle();
        WebElement clickHereLink = driver.findElement(By.linkText("Click Here"));
        clickHereLink.click();

        for (String windHandle:driver.getWindowHandles()
        ) {
            driver.switchTo().window(windHandle);
        }
    }

    @Test
    public void iFrames() {
        driver.get("http://the-internet.herokuapp.com/iframe");

        // step into the frame in which the web element is located
        driver.switchTo().frame("mce_0_ifr");
        // now that we are in the frame, we are able to save the locator to a web element
        WebElement textElement = driver.findElement(By.xpath("//*[@id='tinymce']//p"));
        textElement.clear();
        textElement.sendKeys("some text");

        // switch back to the main document or first frame
        driver.switchTo().defaultContent();
        WebElement headerText = driver.findElement(By.xpath("//div[@class='example']//h3"));
        Assert.assertTrue(headerText.isDisplayed());
    }

    @Test
    public void nestedFrames() {
        driver.get("http://the-internet.herokuapp.com/nested_frames");

        // step into the frame in which the web element is located
        driver.switchTo().frame("frame-top").switchTo().frame("frame-left");

        WebElement leftFrameBodyText = driver.findElement(By.xpath("//body"));

        Assert.assertEquals(leftFrameBodyText.getText(), "LEFT");

        driver.switchTo().parentFrame();
        driver.switchTo().frame("frame-middle");
        WebElement middleFrameBodyText = driver.findElement(By.xpath("//body"));

        Assert.assertEquals(middleFrameBodyText.getText(), "MIDDLE");
    }


    @Test
    public void disappearingElements() {
        driver.get("https://admin:admin@the-internet.herokuapp.com/disappearing_elements");
        WebElement galleryButton = driver.findElement(By.cssSelector("a[href='/gallery/']"));

        // while element is not visible -> refresh
        boolean displayed = false;
        do {
            try {
                displayed = driver.findElement(By.cssSelector("a[href='/gallery/']")).isDisplayed();
            } catch (NoSuchElementException e) {
                System.out.println(e);
                driver.navigate().refresh();
            }
        } while (!displayed);

        Assert.assertEquals(galleryButton.getText(), "Gallery");
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
        String parentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String curWindow : allWindows) {
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
