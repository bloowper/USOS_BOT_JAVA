import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Vector;



public class GUI extends JFrame implements Runnable {
    GridBagConstraints constraints;
    JMenuBar jMenuBar;
    CourseControler courseControler;
    Bot bot;

    public GUI() throws HeadlessException {
        super();
        courseControler = new CourseControler();
        jMenuBar = new jMenuBarGUI();
        bot = new Bot("https://login.uj.edu.pl/login?service=https%3A%2F%2Fwww.usosweb.uj.edu.pl%2Fkontroler.php%3F_action%3Dlogowaniecas%2Findex&locale=pl");

        //TESTING
        // wrzucam sobie kursy do testowania
            var prop = new Properties();
            try {
                prop.load(new FileInputStream("src/main/java/courseInvoDevelopment.properties"));
                courseControler.add(new CourseModel(prop.getProperty("c1.name"),
                        prop.getProperty("c1.link"),
                        Integer.parseInt((String) prop.get("c1.id")),
                        new Vector<>(List.of(Integer.parseInt(prop.getProperty("c1.i1id")),Integer.parseInt(prop.getProperty("c1.i2id"))))
                        ));

                courseControler.add(new CourseModel(prop.getProperty("c2.name"),
                        prop.getProperty("c2.link"),
                        Integer.parseInt((String) prop.get("c2.id")),
                        new Vector<>(List.of(Integer.parseInt(prop.getProperty("c2.i1id")),Integer.parseInt(prop.getProperty("c2.i2id"))))
                ));

                courseControler.add(new CourseModel(prop.getProperty("c3.name"),
                        prop.getProperty("c3.link"),
                        Integer.parseInt((String) prop.get("c3.id")),
                        new Vector<>(List.of(Integer.parseInt(prop.getProperty("c3.i1id")),Integer.parseInt(prop.getProperty("c3.i2id"))))
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        //TESTING END
    }

    @Override
    public void run() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,900);
        setLayout(new GridBagLayout());
        var c = new GridBagConstraints();

        c.gridx=0;
        c.gridy=1;
        c.gridwidth=3;
        getContentPane().add(new JScrollPane(courseControler.getView()),c);

        Vector<CourseModel> courseModels = new Vector<>();
        bot.setModels(courseModels);

        setJMenuBar(jMenuBar);
        setLocationRelativeTo(null);
        pack();
        setResizable(false);
        setVisible(true);
    }

    class jMenuBarGUI extends JMenuBar{
        JMenu bot;
            JMenuItem bot_start;
        JMenu kursy;
            JMenuItem kursy_dodajNowy;
            JMenuItem kursy_usun_zaznaczone;

        public jMenuBarGUI() {
            super();
            //bar structure inicjalization
            bot = new JMenu("BOT");
                bot_start = new JMenuItem("Wlacz bota");
                add(bot);
                bot.add(bot_start);
            kursy = new JMenu("KURSY");
                kursy_dodajNowy = new JMenuItem("dodaj nowy");
                kursy_usun_zaznaczone = new JMenuItem("usun zaznaczone");
                add(kursy);
                kursy.add(kursy_dodajNowy);
                kursy.add(kursy_usun_zaznaczone);
            //end bar structure inicjalization
            //BOT
            //BOT START
            bot_start.addActionListener(new ActionListener_bot_start());
            //KURSY
            //KURSY DODAJ NOWY
            kursy_dodajNowy.addActionListener(e -> {
                JTextField jTextFieldName = new JTextField();
                JTextField jTextFieldLink = new JTextField();
                JTextField jTextFieldidID = new JTextField();
                JTextField jTextFieldInstructor1 = new JTextField();
                JTextField jTextFieldInstructor2 = new JTextField();
                Object[] fields = {
                        "Nazwa kursu",jTextFieldName,
                        "Link do kursu",jTextFieldLink,
                        "ID kursu(z html)",jTextFieldidID,
                        "prowadzacy 1",jTextFieldInstructor1,
                        "prowadzacy 2",jTextFieldInstructor2
                };
                int dialog = JOptionPane.showConfirmDialog(kursy, fields, "Nowy kurs", JOptionPane.OK_CANCEL_OPTION);
                if(dialog == JOptionPane.OK_OPTION){
                    String name = jTextFieldName.getText();
                    String link = jTextFieldLink.getText();
                    int ID = Integer.parseInt(jTextFieldidID.getText());
                    Vector<Integer> courseInstructors = new Vector<>(List.of(
                            Integer.parseInt(jTextFieldInstructor1.getText()),
                            Integer.parseInt(jTextFieldInstructor2.getText()))
                    );
                    courseControler.add(new CourseModel(name,link,ID,courseInstructors));
                }

            });
            //KURSY USUN ZAZNACZONE
        }

    //END JMENUBAR
    }

    class ActionListener_bot_start implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            bot.setModels(courseControler.getModels());


            String login = null;
            String password = null;
            try(var br = new BufferedReader(new FileReader("src/main/java/password.txt"))){
                login = br.readLine();
                password = br.readLine();
            }catch (FileNotFoundException exception) {
                File file = new File("src/main/java/password.txt");
                try {
                    if(file.createNewFile()){
                        System.out.println("src/main/java/password.txt\n1st line login 2nd line password\nfile is added to git ignore");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            bot.setLogin(login);
            bot.setPassword(password);

            new Thread(bot).start();
        }
    }

}



class GUIrunner{
    public static void main(String[] args) {
        GUI gui = new GUI();
        SwingUtilities.invokeLater(gui);

    }
}