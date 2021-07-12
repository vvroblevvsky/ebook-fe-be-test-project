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
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class eBookAppTest {

    // base URL of tested website (DO NOT CHANGE):
    private final String baseUrl = "https://ta-ebookrental-fe.herokuapp.com/";

    // login and password to register new user and login to an account (CHANGE BEFORE TESTS):
    private final String login = "log32";
    private final String password = "log4";

    // Operations on titles:
    // Title data: title, author and year (CAN BE CHANGED BEFORE TESTS):
    private final String title = "test title";
    private final String author = "test author";
    private final String year = "2000";

    // Title data edited
    private final String titleEdited = "title edited";
    private final String authorEdited = "author edited";
    private final String yearEdited = "1995";

    // Operations on copies:
    // Date to be changed while editing copy
    private LocalDate now = LocalDate.now();

    @Test // 1.1.
    public void testAShouldCorrectlyRegisterNewUser() throws InterruptedException {
        registrationAllMethods("You have been successfully registered!");
        System.out.println("test A"); //TODO: delete
    }

    @Test // 1.2.
    public void testBShouldNotRegisterSameUserTwice() throws InterruptedException {
        registrationAllMethods("This user already exist!");
        System.out.println("Test B"); //TODO: delete
    }

    @Test // 2.1.
    public void testCShouldCorrectlyLoginIfAccountExists() throws InterruptedException {
        boolean alert = loginAllMethods(login, password);
        assertTrue(alert);
        System.out.println("Test C"); //TODO: delete
    }

    @Test // 2.2.
    public void testDShouldNotLoginIfLoginOrPasswordIsIncorrectOrAccountDoesNotExist() throws InterruptedException {
        boolean alert = loginAllMethods(login + "m1574k3", password + "m1574k3");
        assertFalse(alert);
        System.out.println("Test D"); //TODO: delete
    }

    @Test
    public void testEShouldCorrectlyAddTitleToList() throws InterruptedException {
        // call initWebDriverAndLogin() method
        WebDriver driver = initWebDriverAndLogin(login, password);

        // Check how many titles there were on the list at the beginning
        int howManyBooksBefore = howManyElements(driver, "//li[@class='titles-list__item list__item']");

        // Select add new button and click on it
        selectAndClickOnElement(driver, "//button[@name='add-title-button']");

        // wait for page to reload
        waitUntil(driver);

        // select all input fields
        List<WebElement> inputFields = driver.findElements(By.xpath("//input"));

        // add title to list
        inputFields.get(0).sendKeys(title);
        inputFields.get(1).sendKeys(author);
        inputFields.get(2).sendKeys(year);

        // select submit button and click on it
        selectAndClickOnElement(driver, "//button[@name='submit-button']");

        // wait for all elements on the list to be visible
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By
                .xpath("//li[@class='titles-list__item list__item']"))));

        // check if the size of the list has increased
        int howManyBooksAfter = howManyElements(driver, "//li[@class='titles-list__item list__item']");
        assertTrue(howManyBooksAfter > howManyBooksBefore);

        // Close WebDriver
        driver.close();
        System.out.println("TEST E"); //TODO: delete
    }

    @Test
    public void testFShouldCorrectlyEditTitleFromTheList() throws InterruptedException {
        // call initWebDriverAndLogin() method
        WebDriver driver = initWebDriverAndLogin(login, password);

        // Check title data before editing:
        List<String> titleBeforeEdit = checkTitle(driver);

        // Select edit button and click on it
        selectAndClickOnElement(driver, "//button[@class='edit-btn btn--small btn btn--warning']");

        // Select all input fields
        List<WebElement> inputFields = driver.findElements(By.xpath("//input[@class='input-field__input']"));

        // put new data into title, author, year input fields, and click edit title button
        List<String> editedTitleData = createAndFillStringList(titleEdited, authorEdited, yearEdited);
        fillListOfWebElements(inputFields, editedTitleData);

        // select edit title button and click on it:
        selectAndClickOnElement(driver, "//button[@name='submit-button']");

        // check title data after editing
        sleepBeforeChecking();
        List<String> titleAfterEdit = checkTitle(driver);

        // assertion - check if data about title, author and year has changed after editing
        assertNotEquals(titleBeforeEdit, titleAfterEdit);
        assertEquals("by " + editedTitleData.get(1), titleAfterEdit.get(1));
        assertEquals("(" + editedTitleData.get(2) + ")", titleAfterEdit.get(2));
        assertEquals(editedTitleData.get(0).toUpperCase(Locale.ROOT), titleAfterEdit.get(0));

        // Close WebDriver
        driver.close();
        System.out.println("TEST F"); //TODO: delete
    }

    @Test
    public void testGShouldCorrectlyMoveToTheCopiesScreen() throws InterruptedException {
        // call initWebDriverAndLogin() method
        WebDriver driver = initWebDriverAndLogin(login, password);

        // Obtain title id
        String titleId = getItemId(driver, "//li[@class='titles-list__item list__item']");

        // Select show copies button and click on it
        selectAndClickOnElement(driver, "//button[@class='show-copies-btn btn--small btn btn--primary']");

        // Wait for page to reload before checking
        sleepBeforeChecking();

        // select list of copies subtitle, check if site URL has changed to copies screen and if subtitle is
        // "LIST OF COPIES"
        WebElement listOfCopiesSubtitle = driver.findElement(By
                .xpath("//h2[@class='sub-title flex-grow--1 margin-right--1']"));
        assertEquals("LIST OF COPIES", listOfCopiesSubtitle.getText());
        assertEquals(baseUrl + "items/" + titleId, driver.getCurrentUrl());

        // Close WebDriver
        driver.close();
        System.out.println("TEST G"); //TODO: delete
    }

    @Test
    public void testHShouldCorrectlyAddCopyToList() throws InterruptedException {
        // Move to copies screen:
        WebDriver driver = moveToCopies();

        // check how many copies there were at the beginning:
        int howManyCopiesBefore = howManyElements(driver, "//ul[@class='items-list list']");

        // Select and click Add New button:
        selectAndClickOnElement(driver, "//button[@name='add-item-button']");

        // Information about purchase date is completed automatically - let's only confirm it by selecting and clicking
        // "add copy" button:
        selectAndClickOnElement(driver, "//button[@name='submit-button']");

        // check how many copies there are after adding one:
        sleepBeforeChecking();
        int howManyCopiesAfter = howManyElements(driver, "//ul[@class='items-list list']");

        // Assertion:
        assertNotEquals(howManyCopiesBefore, howManyCopiesAfter);
        assertEquals(howManyCopiesBefore + 1, howManyCopiesAfter);

        // Close WebDriver
        driver.close();
        System.out.println("TEST H"); //TODO: delete
    }










    @Test
    public void testIShouldCorrectlyEditCopyFromTheList() throws InterruptedException {
        // Move to copies screen:
        WebDriver driver = moveToCopies();

        // Select and click "Edit" button:
        selectAndClickOnElement(driver, "//button[@class='edit-btn btn--small btn btn--warning']");

        // Select "Purchase date" input field and change the date
        selectAndClickOnElement(driver, "//input[@name='purchase-date']");
//        List<WebElement> daysBtns = driver.findElements(By.xpath(""));

        // Close WebDriver
//        driver.close();
//        System.out.println("TEST I"); //TODO: delete
    }













    @Test
    public void testJShouldCorrectlyMoveToRentsScreen() throws InterruptedException {
        // Move to copies screen:
        WebDriver driver = moveToCopies();

        // Obtain copy ID:
        String copyId = getItemId(driver, "//li[@class='items-list__item list__item']");

        // Select and click "Show history" button:
        selectAndClickOnElement(driver, "//button[@class='show-rents-btn btn--small btn btn--primary']");

        // Wait for page to reload:
        sleepBeforeChecking();

        // Select rents history subtitle, check if site URL has changed to rents screen and if subtitle is:
        // "RENTS HISTORY"
        WebElement rentsSubtitle = driver.findElement(By.xpath("//h2[@class='sub-title flex-grow--1 margin-right--1']"));
        assertEquals("RENTS HISTORY", rentsSubtitle.getText().toUpperCase(Locale.ROOT));
        assertEquals(baseUrl + "rents/" + copyId, driver.getCurrentUrl());

        // Close WebDriver
        driver.close();
        System.out.println("TEST J"); //TODO: delete
    }

    @Test
    public void testYShouldCorrectlyRemoveCopyFromTheList() throws InterruptedException {
        // Move to copies screen:
        WebDriver driver = moveToCopies();

        // Check how many copies there were on the list at the beginning
        int howManyCopiesBefore = howManyElements(driver, "//li[@class='items-list__item list__item']");

        // Select and click "Remove" button
        selectAndClickOnElement(driver, "//button[@class='remove-btn btn--small btn btn--error']");

        // Wait for page to reload after removing element:
        sleepBeforeChecking();

        // Check how many copies there are after deleting one
        int howManyCopiesAfter = howManyElements(driver, "//li[@class='items-list__item list__item']");

        // Assertions:
        assertTrue(howManyCopiesBefore > howManyCopiesAfter);
        assertEquals(howManyCopiesBefore - 1, howManyCopiesAfter);

        // Close WebDriver
        driver.close();
        System.out.println("TEST Y"); //TODO: delete
    }

    @Test
    public void testZShouldCorrectlyRemoveTitleFromTheList() throws InterruptedException {
        // call initWebDriverAndLogin() method
        WebDriver driver = initWebDriverAndLogin(login, password);

        // Check how many titles there were on the list at the beginning
        int howManyTitlesBefore = driver.findElements(By.xpath("//li[@class='titles-list__item list__item']"))
                .size();

        // Select remove button and click on it:
        selectAndClickOnElement(driver, "//button[@class='remove-btn btn--small btn btn--error']");

        // Wait for page to reload after removing element:
        sleepBeforeChecking();

        // Check how many elements there are on the list after clicking "Remove" button
        int howManyTitlesAfter = driver.findElements(By.xpath("//li[@class='titles-list__item list__item']"))
                .size();

        // Check if the size of the list has decreased
        assertTrue(howManyTitlesBefore > howManyTitlesAfter);
        assertEquals(howManyTitlesBefore - 1, howManyTitlesAfter);

        // Close WebDriver
        driver.close();
        System.out.println("TEST Z"); //TODO: delete
    }

    // initialization method to be used in every test:
    public WebDriver init(String restOfTheUrl){
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium-drivers\\Chrome\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(baseUrl + restOfTheUrl);
        return driver;
    }

    public WebElement register(WebDriver driver) throws InterruptedException {
        // select input fields: by Xpath:
        List<WebElement> inputFields = driver.findElements(By.xpath("//input"));
        // fill input fields with login and password
        List<String> registerData = createAndFillStringList(login, password, password);
        fillListOfWebElements(inputFields, registerData);
        // select register button and click on it:
        selectAndClickOnElement(driver, "//button[@id='register-btn']");
        // wait until alert message:
        sleepBeforeChecking();
        // check if there is an alert message
        WebElement alertMessage = driver.findElement(By.xpath("//p[@class='alert__content']"));
        // return alert message to be checked in assertion:
        return alertMessage;
    }

    public void registrationAllMethods(String expectedMessage) throws InterruptedException {
        // WebDriver initialization
        WebDriver driver = init("register");
        // call register() method:
        WebElement alertMessage = register(driver);
        // assertion:
        assertEquals(expectedMessage, alertMessage.getText());
        // close WebDriver:
        driver.close();
    }

    public boolean login(WebDriver driver, String userLogin, String userPassword) throws InterruptedException {
        // Select input fields:
        List<WebElement> inputFields = driver.findElements(By.xpath("//input"));
        // fill input fields with login and password + click loginBtn
        List<String> loginData = createAndFillStringList(userLogin, userPassword);
        fillListOfWebElements(inputFields, loginData);
        // select login button and click on it:
        selectAndClickOnElement(driver, "//button[@id='login-btn']");
        // wait until login is complete and page is reloaded:
        sleepBeforeChecking();
        // check if there is an error alert after trying to log in
        boolean alerts = driver.findElements(By.xpath("//div[@class='alert alert--error']")).size() == 0;
        // return information about any alerts
        return alerts;
    }

    public WebDriver initWebDriverAndLogin(String userLogin, String userPassword) throws InterruptedException {
        // WebDriver initialization
        WebDriver driver = init("login");
        // Call login() function
        login(driver, userLogin, userPassword);
        return driver;
    }

    public boolean loginAllMethods(String userLogin, String userPassword) throws InterruptedException {
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
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
    }

    public void sleepBeforeChecking() throws InterruptedException {
        try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (InterruptedException e){
            System.out.println("Interrupted while sleeping");
        }
    }

    public void selectAndClickOnElement(WebDriver driver, String elementXpath){
        WebElement element = driver.findElement(By.xpath(elementXpath));
        element.click();
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

    public WebDriver moveToCopies() throws InterruptedException {
        // call initWebDriverAndLogin() method
        WebDriver driver = initWebDriverAndLogin(login, password);

        // Select show copies button and click on it
        selectAndClickOnElement(driver, "//button[@class='show-copies-btn btn--small btn btn--primary']");

        // wait for page to reload:
        sleepBeforeChecking();

        // return WebDriver
        return driver;
    }

    public int howManyElements(WebDriver driver, String xPath){
        int countOfElements = driver.findElements(By.xpath(xPath)).size();
        return countOfElements;
    }

    public String getItemId(WebDriver driver, String xPath){
        String id = driver.findElement(By.xpath(xPath)).getAttribute("id").replace("item-", "");
        return id;
    }

    //TODO: do I need this?
    public List<String> convertCurrentDate(LocalDate date){
        String year = Year.now().toString();
        String month = now.getMonth().toString().substring(0,3);
        String day = String.valueOf(date.getDayOfMonth());
        List<String> currentDateList = createAndFillStringList(year, month, day);
        System.out.println(currentDateList);
        return currentDateList;
    }


}

//TODO:
// get rid of messy code