package pt.lisomatrix.Sockets.websocket.models;

public class ClassDAO {

    private long classId;

    private String name;

    private String classDirectorId;

    private String ClassDirectorName;

    private String course;

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassDirectorId() {
        return classDirectorId;
    }

    public void setClassDirectorId(String classDirectorId) {
        this.classDirectorId = classDirectorId;
    }

    public String getClassDirectorName() {
        return ClassDirectorName;
    }

    public void setClassDirectorName(String classDirectorName) {
        this.ClassDirectorName = classDirectorName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
