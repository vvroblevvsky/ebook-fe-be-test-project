package frontend;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class eBookAppTest {

    // base URL of tested website (DO NOT CHANGE):
    private String baseUrl = "https://ta-ebookrental-fe.herokuapp.com/";

    // login and password to register new user and login to an account (CHANGE BEFORE TESTS):
    private String login = "log25";
    private String password = "log4";

    // Title data: title, author and year (CAN BE CHANGED BEFORE TESTS):
    private String title = "test title";
    private String author = "test author";
    private String year = "2000";

    // Title data edited
    private String titleEdited = "title edited";
    private String authorEdited = "author edited";
    private String yearEdited = "1995";

    @Test // 1.1.
    public void testAShouldCorrectlyRegisterNewUser(){
        registrationAllMethods("You have been successfully registered!");
        System.out.println("test A"); //TODO: delete
    }

    @Test // 1.2.
    public void testBShouldNotRegisterSameUserTwice(){
        registrationAllMethods("This user already exist!");
        System.out.println("Test B"); //TODO: delete
    }

    @Test // 2.1.
    public void testCShouldCorrectlyLoginIfAccountExists(){
        boolean alert = loginAllMethods(login, password);
        assertTrue(alert);
        System.out.println("Test C"); //TODO: delete
    }

    @Test // 2.2.
    public void testDShouldNotLoginIfLoginOrPasswordIsIncorrectOrAccountDoesNotExist(){
        boolean alert = loginAllMethods(login + "m1574k3", password + "m1574k3");
        assertFalse(alert);
        System.out.println("Test D"); //TODO: delete
    }

    @Test // 4.
    public void testEShouldCorrectlyAddTitleToList(){
        // call initWebDriverAndLogin() method
        WebDriver driver = initWebDriverAndLogin(login, password);

        // Check how many titles there were on the list at the beginning
        int howManyBooksBefore = driver.findElements(By.xpath("//li[@class='titles-list__item list__item']"))
                .size();

        // Select add new button and click on it
        selectAndClickOnButton(driver, "//button[@name='add-title-button']");

        // wait for page to reload
        waitUntil(driver);

        // select all input fields
        List<WebElement> inputFields = driver.findElements(By.xpath("//input"));

        // add title to list
        inputFields.get(0).sendKeys(title);
        inputFields.get(1).sendKeys(author);
        inputFields.get(2).sendKeys(year);

        // select submit button and click on it
        selectAndClickOnButton(driver, "//button[@name='submit-button']");

        // wait for all elements on the list to be visible
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By
                .xpath("//li[@class='titles-list__item list__item']"))));

        // check if the size of the list has increased
        int howManyBooksAfter = driver.findElements(By.xpath("//li[@class='titles-list__item list__item']"))
                .size();
        assertTrue(howManyBooksAfter > howManyBooksBefore);

        // Close WebDriver
        driver.close();
        System.out.println("TEST E"); //TODO: delete
    }

    @Test // 5.
    public void testFShouldCorrectlyEditTitleFromTheList(){
        // call initWebDriverAndLogin() method
        WebDriver driver = initWebDriverAndLogin(login, password);

        // Check title data before editing:
        List<String> titleBeforeEdit = checkTitle(driver);

        // Select edit button and click on it
        selectAndClickOnButton(driver, "//button[@class='edit-btn btn--small btn btn--warning']");

        // Select all input fields
        List<WebElement> inputFields = driver.findElements(By.xpath("//input[@class='input-field__input']"));

        // put new data into title, author, year input fields, and click edit title button
        List<String> editedTitleData = createAndFillStringList(titleEdited, authorEdited, yearEdited);
        fillListOfWebElements(inputFields, editedTitleData);


        // select edit title button and click on it:
        selectAndClickOnButton(driver, "//button[@name='submit-button']");

        // check title data after editing
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//ul[@class='titles-list list']"))));
        List<String> titleAfterEdit = checkTitle(driver);

        // assertion - check if data about title, author and year has changed after editing
        assertNotEquals(titleBeforeEdit, titleAfterEdit);
        assertEquals(editedTitleData.get(0).toUpperCase(Locale.ROOT), titleAfterEdit.get(0));
        assertEquals("by " + editedTitleData.get(1), titleAfterEdit.get(1));
        assertEquals("(" + editedTitleData.get(2) + ")", titleAfterEdit.get(2));

        // Close WebDriver
        driver.close();
        System.out.println("TEST F"); //TODO: delete
    }

    @Test // 6.
    public void testZShouldCorrectlyRemoveTitleFromTheList(){
        // call initWebDriverAndLogin() method
        WebDriver driver = initWebDriverAndLogin(login, password);

        // Check how many titles there were on the list at the beginning
        int howManyBooksBefore = driver.findElements(By.xpath("//li[@class='titles-list__item list__item']"))
                .size();

        // Select remove button and click on it:
        selectAndClickOnButton(driver, "//button[@class='remove-btn btn--small btn btn--error']");

        // Wait for page to reload after removing element:
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElement(By
                .xpath("//li[@class='titles-list__item list__item']"))));

        // Check how many elements there are on the list after clicking remove button
        int howManyBooksAfter = driver.findElements(By.xpath("//li[@class='titles-list__item list__item']"))
                .size();

        // Check if the size of the list has decreased
        assertTrue(howManyBooksBefore > howManyBooksAfter);

        // Close WebDriver
        driver.close();
        System.out.println("TEST Z"); //TODO: delete
    }

    @Test // 7.
    public void testGShouldCorrectlyMoveToTheCopiesScreen(){
        // call initWebDriverAndLogin() method
        WebDriver driver = initWebDriverAndLogin(login, password);

        // obtain title id
        String titleId = driver.findElement(By.xpath("//li[@class='titles-list__item list__item']")).getAttribute("id").replace("title-", "");

        // Select show copies button and click on it
        selectAndClickOnButton(driver, "//button[@class='show-copies-btn btn--small btn btn--primary']");

        // Wait for list of copies to be loaded
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@id='title-copies']"))));

        // select list of copies subtitle, check if site URL has changed to copies screen and if subtitle is
        // "LIST OF COPIES"
        WebElement listOfCopiesSubtitle = driver.findElement(By
                .xpath("//h2[@class='sub-title flex-grow--1 margin-right--1']"));
        assertEquals("LIST OF COPIES", listOfCopiesSubtitle.getText());
        assertEquals(baseUrl+"items/"+titleId, driver.getCurrentUrl());

        // Close WebDriver
        driver.close();
        System.out.println("TEST G"); //TODO: delete
    }

    // initialization method to be used in every test:
    public WebDriver init(String restOfTheUrl){
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium-drivers\\Chrome\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(baseUrl + restOfTheUrl);
        return driver;
    }

    public WebElement register(WebDriver driver){
        // select input fields: by Xpath:
        List<WebElement> inputFields = driver.findElements(By.xpath("//input"));
        // fill input fields with login and password
        List<String> registerData = createAndFillStringList(login, password, password);
        fillListOfWebElements(inputFields, registerData);
        // select register button and click on it:
        selectAndClickOnButton(driver, "//button[@id='register-btn']");
        // wait until alert message:
        waitUntil(driver);
        // check if there is an alert message
        WebElement alertMessage = driver.findElement(By.xpath("//p[@class='alert__content']"));
        // return alert message to be checked in assertion:
        return alertMessage;
    }

    public void registrationAllMethods(String expectedMessage){
        // WebDriver initialization
        WebDriver driver = init("register");
        // call register() method:
        WebElement alertMessage = register(driver);
        // assertion:
        assertEquals(expectedMessage, alertMessage.getText());
        // close WebDriver:
        driver.close();
    }

    public boolean login(WebDriver driver, String userLogin, String userPassword){
        // Select input fields:
        List<WebElement> inputFields = driver.findElements(By.xpath("//input"));
        // fill input fields with login and password + click loginBtn
        List<String> loginData = createAndFillStringList(userLogin, userPassword);
        fillListOfWebElements(inputFields, loginData);
        // select login button and click on it:
        selectAndClickOnButton(driver, "//button[@id='login-btn']");
        // wait until login is complete and page is reloaded:
        waitUntil(driver);
        // check if there is an error alert after trying to log in
        boolean alerts = driver.findElements(By.xpath("//div[@class='alert alert--error']")).size() == 0;
        // return information about any alerts
        return alerts;
    }

    public WebDriver initWebDriverAndLogin(String userLogin, String userPassword){
        // WebDriver initialization
        WebDriver driver = init("login");
        // Call login() function
        login(driver, userLogin, userPassword);
        return driver;
    }

    public boolean loginAllMethods(String userLogin, String userPassword){
        // WebDriver initialization
        WebDriver driver = init("login"); //TODO: new function
        // Call login() function with no mistake in login data
        boolean alerts = login(driver, userLogin, userPassword); //TODO: new function
        // Close WebDriver:
        driver.close();
        // return alerts
        return alerts;
    }

    public void waitUntil(WebDriver driver){
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    public void selectAndClickOnButton(WebDriver driver, String buttonXpath){
        WebElement btn = driver.findElement(By.xpath(buttonXpath));
        btn.click();
    }

    public List<String> createAndFillStringList(String... elements){
        List<String> stringList = new ArrayList<>();
        for(String element : elements){
            stringList.add(element);
        }
        return stringList;
    }

    public void fillListOfWebElements(List<WebElement> webElementList, List<String> stringList){
        for(int i = 0; i < webElementList.size(); i++){
            webElementList.get(i).clear();
            webElementList.get(i).sendKeys(stringList.get(i));
        }
    }

    public List<String> checkTitle(WebDriver driver){
        String title = driver.findElement(By
                .xpath("//div[@class='titles-list__item__title list__item__col list__item__col--primary']")).getText();
        String author = driver.findElement(By
                .xpath("//div[@class='titles-list__item__author list__item__col']")).getText();
        String year = driver.findElement(By
                .xpath("//div[@class='titles-list__item__year list__item__col']")).getText();
        List<String> titleData = createAndFillStringList(title, author, year);
        return titleData;
    }

}

//TODO:
// get rid of messy code