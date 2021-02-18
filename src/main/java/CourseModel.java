import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import static java.util.stream.Collectors.*;


public class CourseModel implements Serializable {
    protected String name;
    protected String link;
    protected int ID;
    protected Vector<Integer> courseInstructorsIDs;

    public CourseModel(String name, String link, int courseID, Vector<Integer> courseInstructorsIDs) {
        this.name = name;
        this.link = link;
        this.ID = courseID;
        this.courseInstructorsIDs = courseInstructorsIDs;
    }

    public CourseModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Vector<Integer> getCourseInstructorsIDs() {
        return courseInstructorsIDs;
    }

    public void setCourseInstructorsIDs(Vector<Integer> courseInstructorsIDs) {
        this.courseInstructorsIDs = courseInstructorsIDs;
    }



    @Override
    public String toString() {
        return "CourseModel{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", ID=" + ID +
                ", courseInstructorsIDs=" + courseInstructorsIDs +
                '}';
    }

    /**
     * @return List of xpaths to buttons releted to course instructors
     */
    public ArrayList<String> getInstructorsXpaths(){
        return courseInstructorsIDs.stream()
                        .map(instructorID -> String.format("//input[@name='zajecia[%d][]' and @value='%d']", ID, instructorID))
                        .collect(toCollection(ArrayList::new));
    }

}
