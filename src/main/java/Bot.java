import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Need google chrome drive to be installed
 * and be in standard location ( look at google chrome drive documentation)
 *
 */
public class Bot implements Runnable {
    static String registerButtonXpath = "//input[@type='submit' and @value='Rejestruj' and @class='submit ']";
    static String registerButtonNonActiveXpath = "//input[@type='submit' and @value='Rejestruj' and @class='submit semitransparent'] ";
    static Integer threadWaitTime = 250;

    protected Vector<CourseModel> models;
    protected String loginHtml;
    protected WebDriver driver;
    private String login;
    private String password;
    private boolean registerMode;
    public Bot(String loginHtml) {
        this.loginHtml = loginHtml;
        registerMode = false;

    }

    public void setModels(Vector<CourseModel> models){
        this.models = models;

    }


    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegisterMode(boolean registerMode) {
        this.registerMode = registerMode;
    }

    @Override
    public void run() {
        System.out.println("Bot started");
        try {
            driver = new ChromeDriver();
            driver.get(loginHtml);
            WebElement username = driver.findElement(By.name("username"));
            username.clear();
            username.sendKeys(this.login);

            WebElement password = driver.findElement(By.name("password"));
            password.clear();
            password.sendKeys(this.password);
            driver.findElement(By.className("btn-submit")).click();

            for (CourseModel model : models) {
                String link = model.getLink();
                driver.get(link);
                Vector<WebElement> webElementsInstructors = new Vector<>();
                model.getInstructorsXpaths().forEach(s -> {
                    try {
                        WebElement element = driver.findElement(By.xpath(s));
                        webElementsInstructors.add(element);
                    }catch (org.openqa.selenium.NoSuchElementException e){
                        System.out.println(model.getName()+" prowadzacy \n"+s+" \n nie dostepny\n" +
                                "---------------------------------------\n");
                    }
                });
                if(webElementsInstructors.isEmpty()){
                    System.out.printf("nie udalo zarejestrowac sie na \n" +
                            "%s\n" +
                            "brak wolnych prowadzacych\n" +
                            "---------------------------------------\n"
                            ,model.getName());
                }
                WebElement instructor = webElementsInstructors.get(0);
                instructor.click();
                if(registerMode){
                    boolean nonActiveInformationShowed = false;
                    WebElement registerButton = null;
                    while (registerButton==null){
                        try {
                            registerButton = driver.findElement(By.xpath(Bot.registerButtonXpath));
                        }catch (org.openqa.selenium.NoSuchElementException e){
                            if (nonActiveInformationShowed == false) System.out.println("Register Button jest dalej nie aktywny: oczekiwanie\n"+ "---------------------------------------\n");
                            nonActiveInformationShowed = true;

                            try {
                                Thread.sleep(Bot.threadWaitTime);
                            } catch (InterruptedException interruptedException) {interruptedException.printStackTrace(); }
                        }
                    }
                    if(nonActiveInformationShowed)
                    System.out.println("oczekiwanie zakonczone: rejestracja rozpoczeta");
                    if(registerMode)
                        registerButton.click();
                }
            }

        }
        finally {
            //driver.quit();
        }
    }
}

