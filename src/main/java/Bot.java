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
                    //registerButton.click();
                }
            }

        }
        finally {
            //driver.quit();
        }
    }
}


/**
 * ONLY FOR TESTING PURPOSE
 */
class BotTestRunner{
    public static void main(String[] args) {
        String login = null;
        String password = null;
        try(var br = new BufferedReader(new FileReader("src/main/java/password.txt"))){
            login = br.readLine();
            password = br.readLine();
        }catch (FileNotFoundException e) {
            File file = new File("src/main/java/password.txt");
            try {
                if(file.createNewFile()){
                    System.out.println("src/main/java/password.txt\n1st line login 2nd line password\nfile is added to git ignore");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Vector<CourseModel> courseModels = new Vector<>();
        courseModels.add(new CourseModel(
                "uklady fpga",
                "https://www.usosweb.uj.edu.pl/kontroler.php?_action=dla_stud/rejestracja/brdg2/grupyPrzedmiotu&rej_kod=WFAIS_20%2F21L_INF&prz_kod=WFAIS.IF-X209.0&cdyd_kod=20%2F21L&odczyt=0&prgos_id=497467&callback=g_e808963c",
                471581,
                new Vector<>(List.of(2,1))));
        courseModels.add(new CourseModel(
                "Ebiznes",
                "https://www.usosweb.uj.edu.pl/kontroler.php?_action=dla_stud/rejestracja/brdg2/grupyPrzedmiotu&rej_kod=WFAIS_20%2F21L_INF&prz_kod=WFAIS.IF-D208.0&cdyd_kod=20%2F21L&odczyt=0&prgos_id=497467&callback=g_4182d433",
                472180,
                new Vector<>(List.of(1,2))));

        Bot bot = new Bot("https://login.uj.edu.pl/login?service=https%3A%2F%2Fwww.usosweb.uj.edu.pl%2Fkontroler.php%3F_action%3Dlogowaniecas%2Findex&locale=pl");
        bot.setLogin(login);
        bot.setPassword(password);
        bot.setModels(courseModels);
        bot.setRegisterMode(true);

        Thread thread = new Thread(bot);
        thread.start();
    }
}
