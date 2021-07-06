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

    @Test // 1
    public void testAShouldCorrectlyRegisterNewUser(){
        registrationAllMethods("You have been successfully registered!");
        System.out.println("test A"); //TODO: delete
    }

    @Test // 2
    public void testBShouldNotRegisterSameUserTwice(){
        registrationAllMethods("This user already exist!");
        System.out.println("Test B"); //TODO: delete
    }

    @Test // 3
    public void testCShouldCorrectlyLoginIfAccountExists(){
        loginAllMethods("");
        System.out.println("Test C"); //TODO: delete
    }

    @Test // 4
    public void testDShouldNotLoginIfLoginOrPasswordIsIncorrectOrAccountDoesNotExist(){
        loginAllMethods("m1st4k3");
        System.out.println("Test D"); //TODO: delete
    }

    @Test // 5
    public void testEShouldCorrectlyAddTitleToList(){
        // WebDriver initialization
        WebDriver driver = init("login");

        // Call login() function with no mistake in login data to correctly log user in
        login(driver, "");

        // Check how many titles there were on the list before adding new title
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
        inputFields.get(0).sendKeys("test title");
        inputFields.get(1).sendKeys("test author");
        inputFields.get(2).sendKeys("2000");
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

    @Test // 6
    public void testFShouldCorrectlyEditTitleFromTheList(){

        System.out.println("TEST F"); //TODO: delete
    }

    @Test // 7
    public void testGShouldCorrectlyRemoveTitleFromTheList(){
        // WebDriver initialization
        WebDriver driver = init("login");

        // Call login() function with no mistake in login data to correctly log user in
        login(driver, "");

        // Check how many titles there were on the list before removing a title
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

    public void registerAssertion(String expectedMessage){
        //TODO: put here registration tests
    }

}

//TODO:
// 1. write functionality that'll perform tests in order
// 2. write login tests