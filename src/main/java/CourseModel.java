import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.*;
import static java.util.stream.Collectors.*;


public class CourseModel implements Serializable {
    protected String name;
    protected String link;
    protected Integer courseID;
    protected Vector<Integer> courseInstructorsIDs;

    public CourseModel(String name, String link, int courseID, Vector<Integer> courseInstructorsIDs) {
        this.name = name;
        this.link = link;
        this.courseID = courseID;
        this.courseInstructorsIDs = courseInstructorsIDs;
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

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
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
                ", courseID=" + courseID +
                ", courseInstructorsIDs=" + courseInstructorsIDs +
                '}';
    }

    /**
     * @return List of xpaths to buttons releted to course instructors
     */
    public ArrayList<String> getInstructorsXpaths(){
        return courseInstructorsIDs.stream()
                        .map(instructorID -> String.format("//input[@name='zajecia[%d][]' and @value='%d']", courseID, instructorID))
                        .collect(toCollection(ArrayList::new));
    }

}
