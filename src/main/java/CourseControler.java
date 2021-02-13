import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * @
 */
public class CourseControler {
    protected Vector<CourseModel> models;
    protected courseView view;
    protected Vector<Vector<String>> viewData;

    public CourseControler() {
        models = new Vector<>(20);
        viewData = new Vector<>(20);
        view = new courseView(viewData,new Vector<>(List.of("Numer kursu","Nazwa kursu","Link do kursu")));
        view.addMouseListener(new MouseEventCourseControler());
    }

    public void add(CourseModel model){
        models.add(model);
        int courseID = model.getCourseID();
        String name = model.getName();
        String link = model.getLink();
        Vector<String> stringVector = new Vector<>(List.of(String.valueOf(courseID), name, link));
        viewData.add(stringVector);
        SwingUtilities.updateComponentTreeUI(view);
    }

    public Vector<CourseModel> getModels() {
        return models;
    }

    public courseView getView() {
        return view;
    }

    public Vector<Vector<String>> getViewData() {
        return viewData;
    }



    class MouseEventCourseControler extends MouseAdapter {

        JTextField jTextFieldName;
        JTextField jTextFieldLink;
        JTextField jTextFieldidID;
        JTextField jTextFieldInstructor1;
        JTextField jTextFieldInstructor2;
        public MouseEventCourseControler() {
            jTextFieldName = new JTextField();
            jTextFieldLink = new JTextField();
            jTextFieldidID = new JTextField();
            jTextFieldInstructor1 = new JTextField();
            jTextFieldInstructor2 = new JTextField();


        }

        @Override
        public void mousePressed(MouseEvent e) {
            JTable source = (JTable) e.getSource();
            int row = source.rowAtPoint(e.getPoint());
            if(e.getClickCount()==2 && row!=-1){
                Integer courseID = Integer.parseInt(viewData.get(row).get(0));
                CourseModel courseModel = models.stream().filter(model -> model.getCourseID() == courseID).findFirst().get();
                System.out.println(courseModel);
                jTextFieldName.setText(courseModel.getName());
                jTextFieldLink.setText(courseModel.getLink());
                jTextFieldidID.setText(String.valueOf(courseModel.getCourseID()));
                jTextFieldInstructor1.setText(courseModel.getCourseInstructorsIDs().get(0).toString());
                jTextFieldInstructor2.setText(courseModel.getCourseInstructorsIDs().get(1).toString());
                Object[] fields = {
                        "Nazwa kursu",jTextFieldName,
                        "Link do kursu",jTextFieldLink,
                        "ID kursu(z html)",jTextFieldidID,
                        "prowadzacy 1",jTextFieldInstructor1,
                        "prowadzacy 2",jTextFieldInstructor2
                };
                int confirmDialog = JOptionPane.showConfirmDialog(view, fields, "Edytowanie danych kursu", JOptionPane.OK_CANCEL_OPTION);
                out:
                if(confirmDialog == JOptionPane.OK_OPTION){
                    String newNameText = jTextFieldName.getText();
                    String newLinkText = jTextFieldLink.getText();
                    Integer newCourseID = null;
                    Integer newInstructor1ID = null;
                    Integer newInstructor2ID = null;

                    try {
                        newCourseID = Integer.parseInt(jTextFieldidID.getText());
                        newInstructor1ID = Integer.parseInt(jTextFieldInstructor1.getText());
                        newInstructor2ID = Integer.parseInt(jTextFieldInstructor2.getText());
                    } catch (NumberFormatException numberFormatException) {
                        JOptionPane.showMessageDialog(view,"Nieprawidlowe dane","Blad",JOptionPane.ERROR_MESSAGE);
                        break out;
                    }
                    //model update
                    courseModel.setCourseID(newCourseID);
                    courseModel.setLink(newLinkText);
                    courseModel.setName(newNameText);
                    courseModel.getCourseInstructorsIDs().set(0,newInstructor1ID);
                    courseModel.getCourseInstructorsIDs().set(1,newInstructor2ID);
                    //view update
                    //table looks like
                    //numer kursu|nazwa kursu|link do kursu
                    viewData.get(row).set(0,newCourseID.toString()); // ID
                    viewData.get(row).set(1,newNameText); // Name
                    viewData.get(row).set(2,newLinkText); // Link
                    SwingUtilities.updateComponentTreeUI(source);
                }

            }
        }
    }
}




//Only for testing GUI
class courseControlerTESTER{
    public static void main(String[] args) {

        CourseControler courseControler = new CourseControler();
        courseControler.add(new CourseModel("Matematyka","Http//matematyka",254,new Vector<Integer>(List.of(5, 4))));
        courseControler.add(new CourseModel("Fizyka","Http//fizyka",234,new Vector<Integer>(List.of(5, 4))));
        courseControler.add(new CourseModel("Algorytmy","Http//algorytmy",123,new Vector<Integer>(List.of(5, 4))));

        JTable view = courseControler.getView();

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.getContentPane().add(new JScrollPane(view));

        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
