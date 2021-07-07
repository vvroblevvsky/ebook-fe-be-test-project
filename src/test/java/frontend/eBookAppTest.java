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
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class eBookAppTest {

    // base URL of tested website (DO NOT CHANGE):
    private String baseUrl = "https://ta-ebookrental-fe.herokuapp.com/";

    // login and password to register new user and login to an account (CHANGE BEFORE TESTS):
    private String login = "log8";
    private String password = "log4";

    // Title data: title, author and year (CAN BE CHANGED BEFORE TESTS):
    String title = "test title";
    String author = "test author";
    String year = "2000";

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
        loginAllMethods("");
        System.out.println("Test C"); //TODO: delete
    }

    @Test // 2.2.
    public void testDShouldNotLoginIfLoginOrPasswordIsIncorrectOrAccountDoesNotExist(){
        loginAllMethods("m1st4k3");
        System.out.println("Test D"); //TODO: delete
    }

    @Test // 4.1.
    public void testEShouldCorrectlyAddTitleToList(){
        // WebDriver initialization
        WebDriver driver = init("login");

        // Call login() function with no mistake in login data to correctly log user in
        login(driver, "");

        // Check how many titles there were on the list at the beginning
        int howManyBooksBefore = driver.findElements(By.xpath("//li[@class='titles-list__item list__item']"))
                .size();

        // Select addNew button
        WebElement addNewBtn = driver.findElement(By.xpath("//button[@name='add-title-button']"));
        addNewBtn.click();

        // wait for page to reload
        waitUntil(driver);

        // select all WebElements
        List<WebElement> inputFields = driver.findElements(By.xpath("//input"));
        WebElement submitBtn = driver.findElement(By.xpath("//button[@name='submit-button']"));

        // add title to list
        inputFields.get(0).sendKeys(title);
        inputFields.get(1).sendKeys(author);
        inputFields.get(2).sendKeys(year);
        submitBtn.click();

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

    @Test // 5.1.
    public void testFShouldCorrectlyEditTitleFromTheList(){
        // WebDriver initialization
        WebDriver driver = init("login");

        // Call login() function with no mistake in login data to correctly log user in
        login(driver, "");

        // Check title data before editing:
        String titleBefore = driver.findElement(By
                .xpath("//div[@class='titles-list__item__title list__item__col list__item__col--primary']")).getText();
        String authorBefore = driver.findElement(By
                .xpath("//div[@class='titles-list__item__author list__item__col']")).getText();
        String yearBefore = driver.findElement(By
                .xpath("//div[@class='titles-list__item__year list__item__col']")).getText();
        System.out.println(titleBefore);
        System.out.println(authorBefore);
        System.out.println(yearBefore);

        // Select edit button
        WebElement editBtn = driver.findElement(By.xpath("//button[@class='edit-btn btn--small btn btn--warning']"));
        editBtn.click();

        // Select all input fields and edit title button
        List<WebElement> inputFields = driver.findElements(By.xpath("//input[@class='input-field__input']"));
        WebElement editTitleBtn = driver.findElement(By.xpath("//button[@name='submit-button']"));

        // put new data into title, author, year input fields, and click edit title button
        List<String> editedTitleData = new ArrayList<>();
        editedTitleData.add(title + " edited");
        editedTitleData.add(author + " edited");
        editedTitleData.add(Integer.toString(Integer.parseInt(year) - 5));
        for(int i = 0; i < inputFields.size(); i++){
            inputFields.get(i).clear();
            inputFields.get(i).sendKeys(editedTitleData.get(i));
        }
        editTitleBtn.click();

        //TODO: check if data about book has changed


    }

    @Test // 6.1.
    public void testGShouldCorrectlyRemoveTitleFromTheList(){
        // WebDriver initialization
        WebDriver driver = init("login");

        // Call login() function with no mistake in login data to correctly log user in
        login(driver, "");

        // Check how many titles there were on the list at the beginning
        int howManyBooksBefore = driver.findElements(By.xpath("//li[@class='titles-list__item list__item']"))
                .size();

        // Select remove button:
        WebElement removeBtn = driver.findElement(By.xpath("//button[@class='remove-btn btn--small btn btn--error']"));
        removeBtn.click();

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
        // select WebElements by Xpath:
        List<WebElement> inputFields = driver.findElements(By.xpath("//input"));
        WebElement registerBtn = driver.findElement(By.xpath("//button[@id='register-btn']"));
        // fill input fields with login and password + click registerBtn
        List<String> registerData = new ArrayList<>();
        registerData.add(login);
        registerData.add(password);
        registerData.add(password);
        for(int i = 0; i < inputFields.size(); i++){
            inputFields.get(i).sendKeys(registerData.get(i));
        }
        registerBtn.click();
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

    public boolean login(WebDriver driver, String intentionalMistake){
        // Select WebElements by Xpath:
        List<WebElement> inputFields = driver.findElements(By.xpath("//input"));
        WebElement loginBtn = driver.findElement(By.xpath("//button[@id='login-btn']"));
        // fill input fields with login and password + click loginBtn
        List<String> loginData = new ArrayList<>();
        loginData.add(login + intentionalMistake);
        loginData.add(password + intentionalMistake);
        for(int i = 0; i < inputFields.size(); i++){
            inputFields.get(i).sendKeys(loginData.get(i));
        }
        loginBtn.click();
        // wait until login is complete and page is reloaded:
        waitUntil(driver);
        // check if there is an error alert after trying to log in
        boolean alerts = driver.findElements(By.xpath("//div[@class='alert alert--error']")).size() == 0;
        // return information about any alerts
        return alerts;
    }

    public void loginAllMethods(String intentionalMistake){
        // WebDriver initialization
        WebDriver driver = init("login");
        // Call login() function with no mistake in login data
        boolean alerts = login(driver, intentionalMistake);
        // Assertion - if there were no alerts - the login process was successful
        if(intentionalMistake == ""){
            assertTrue(alerts);
        } else {
            assertFalse(alerts);
        }
        // Close WebDriver:
        driver.close();
    }

    public void waitUntil(WebDriver driver){
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

}

//TODO:
// get rid of shitty code