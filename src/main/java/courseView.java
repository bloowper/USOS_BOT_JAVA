import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;


/**
 * View for courses models
 * @author tomek
 * @see javax.swing.JTable
 *
 */
public class courseView extends JTable {
    protected Vector<Vector<String>> viewsData;
    protected Vector<String> columnNames;

    public courseView(Vector<Vector<String>> viewsData, Vector<String> columnNames) {
        super(viewsData,columnNames);
        this.viewsData = viewsData;
        this.columnNames = columnNames;
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void add(Vector<String> courseRepresentation){
        viewsData.add(courseRepresentation);
        SwingUtilities.updateComponentTreeUI(this);
    }


    public void addAll(Vector<Vector<String>> multipleCourseRepresentation){
        viewsData.addAll(multipleCourseRepresentation);
        SwingUtilities.updateComponentTreeUI(this);
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
    }
}


//
////Only for testing GUI
//class testerView{
//
//    public static void main(String[] args) {
//        System.out.println("GUI test of courseView");
//        JFrame jFrame = new JFrame();
//        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        Vector<String> columnNames = new Vector<>();
//        columnNames.add("Nazwa kursu");
//        columnNames.add("Strona kursu");
//        Vector<Vector<String>> dataToDisplay = new Vector<>();
//        courseView courseView = new courseView(dataToDisplay, columnNames);
//        JScrollPane scrollPaneCourseView = new JScrollPane(courseView);
//        jFrame.getContentPane().add(scrollPaneCourseView);
//
//        Vector<String> daneKursu = new Vector<>();
//        daneKursu.add("Kurs numer 1");
//        daneKursu.add("Kurs numer 1");
//        courseView.add(daneKursu);
//
//        Vector<String> daneKursu2 = new Vector<>();
//        daneKursu2.add(0,"kurs2");
//        daneKursu2.add(1,"kurs2");
//        courseView.add(daneKursu2);
//
//        Vector<String> daneKursu3 = new Vector<>();
//        daneKursu3.add(0,"kurs3");
//        daneKursu3.add(1,"kurs3");
//        courseView.add(daneKursu3);
//
//        courseView.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                courseView source = (courseView) e.getSource();
//                int i = source.rowAtPoint(e.getPoint());
//                JOptionPane.showMessageDialog(source,"Kliknales "+i,"title",JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//
//        jFrame.setLocationRelativeTo(null);
//        jFrame.pack();
//        jFrame.setVisible(true);
//    }
//}