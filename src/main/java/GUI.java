import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Vector;



public class GUI extends JFrame implements Runnable {
    GridBagConstraints constraints;
    JMenuBar jMenuBar;
    CourseControler courseControler;
    Bot bot;

    JPasswordField passord;
    JTextField login;

    public GUI() throws HeadlessException {
        super();
        courseControler = new CourseControler();
        jMenuBar = new jMenuBarGUI();
        login = new JTextField("login");
        passord = new JPasswordField("haslo");
        bot = new Bot("https://login.uj.edu.pl/login?service=https%3A%2F%2Fwww.usosweb.uj.edu.pl%2Fkontroler.php%3F_action%3Dlogowaniecas%2Findex&locale=pl");
    }

    @Override
    public void run() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,900);
        setLayout(new GridBagLayout());
        var c = new GridBagConstraints();


        createLogginBar();

        c.gridx=0;
        c.gridy=1;
        c.gridwidth=4;
        c.gridheight=4;
        getContentPane().add(new JScrollPane(courseControler.getView()),c);

        Vector<CourseModel> courseModels = new Vector<>();
        bot.setModels(courseModels);

        setJMenuBar(jMenuBar);
        setLocationRelativeTo(null);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void createLogginBar() {
        JPanel logginTable = new JPanel();
        logginTable.setLayout(new BoxLayout(logginTable,BoxLayout.X_AXIS));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=4;
        c.gridheight=1;

        JTextArea logintext = new JTextArea("login");
        logintext.setFocusable(false);
        logginTable.add(logintext);
        this.login.setPreferredSize(new Dimension(200,20));
        logginTable.add(this.login);

        JTextArea passwodtext = new JTextArea("haslo");
        passwodtext.setFocusable(false);
        logginTable.add(passwodtext);
        passord.setPreferredSize(new Dimension(100,20));
        logginTable.add(passord);
        getContentPane().add(logginTable, c);

    }

    class jMenuBarGUI extends JMenuBar{
        JMenu bot;
            JMenuItem bot_start;
        JMenu kursy;
            JMenuItem kursy_dodajNowy;
            JMenuItem kursy_usun_zaznaczone;
            JMenuItem kursy_zapisz_do_pliku;
            JMenuItem kursy_odczyt;

        public jMenuBarGUI() {
            super();
            //bar structure inicjalization
            bot = new JMenu("BOT");
                bot_start = new JMenuItem("Wlacz bota");
                add(bot);
                bot.add(bot_start);
            kursy = new JMenu("KURSY");
                kursy_dodajNowy = new JMenuItem("dodaj nowy kurs");
                kursy_usun_zaznaczone = new JMenuItem("usun zaznaczone kursy");
                kursy_zapisz_do_pliku = new JMenuItem("Zapisz kursy");
                kursy_odczyt = new JMenuItem("Wczytaj kursy");
                add(kursy);
                kursy.add(kursy_dodajNowy);
                kursy.add(kursy_usun_zaznaczone);
                kursy.add(kursy_zapisz_do_pliku);
                kursy.add(kursy_odczyt);
            //end bar structure inicjalization
            //BOT
            //BOT START
            bot_start.addActionListener(new ActionListener_bot_start());
            //KURSY
            //KURSY DODAJ NOWY
            kursy_dodajNowy.addActionListener(new ActtionListener_add_course());
            //KURSY USUN ZAZNACZONE
            kursy_usun_zaznaczone.addActionListener(new ActionListener_delete_cours());
            //KURSY kursy_zapisz_do_pliku
            kursy_zapisz_do_pliku.addActionListener(new ActionListener_save_to_file());
            //KURSY kursy_odczyt;
            kursy_odczyt.addActionListener(new ActionListener_read_from_file());
        }

    //END JMENUBAR
    }

    class ActionListener_bot_start implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            bot.setModels(courseControler.getModels());

        //ZMIENIC TO BY UZYTKOWNIK DOPIERO W PRZEGLADARCE WPROWADZAL DANE
        //bezpieczniej
            String log = login.getText();
            String pass = String.valueOf(passord.getPassword());

        //END TESTING
            bot.setLogin(log);
            bot.setPassword(pass);
            bot.setRegisterMode(true);
            new Thread(bot).start();
        }
    }

    class ActtionListener_add_course implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

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
                int dialog = JOptionPane.showConfirmDialog((JMenuItem)e.getSource(), fields, "Nowy kurs", JOptionPane.OK_CANCEL_OPTION);
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
        }
    }

    class ActionListener_delete_cours implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int[] selectedRows = courseControler.getView().getSelectedRows();
            Vector<Vector<String>> viewData = courseControler.getViewData();
            Vector<Integer> toDeleteID = new Vector<>();
            for (int selectedRow : selectedRows) {
                toDeleteID.add(Integer.parseInt(viewData.get(selectedRow).get(0)));
            }
            System.out.println("ID delete: "+toDeleteID);
            toDeleteID.forEach(courseControler::deleteByID);
        }
    }

    class ActionListener_save_to_file implements ActionListener{
        private JFileChooser fileChooser;

        public ActionListener_save_to_file() {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setDialogTitle("Zapisz kursy do pliku");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int userSelection = fileChooser.showSaveDialog(jMenuBar);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.printf("Get : [%s] as dir to encode views",selectedFile);
                try {
                    selectedFile.createNewFile();
                    courseControler.saveToXml(selectedFile);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(jMenuBar,"problem z zapisem","blad",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class ActionListener_read_from_file implements ActionListener{
        private JFileChooser fileChooser;

        public ActionListener_read_from_file() {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setDialogTitle("Wczytaj kursy");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int dialog = fileChooser.showOpenDialog(jMenuBar);
            if(dialog == JFileChooser.APPROVE_OPTION){

                try {
                    courseControler.readFromXml(fileChooser.getSelectedFile());
                } catch (FileNotFoundException exception) {
                    JOptionPane.showMessageDialog(jMenuBar,"Wystapil blad podczas odczytu z pliku","Blad",JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }

}



