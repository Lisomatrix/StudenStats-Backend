package pt.lisomatrix.Sockets.websocket.models;

import pt.lisomatrix.Sockets.models.Class;

import java.util.ArrayList;
import java.util.List;

public class ClassDAO {

    private long classId;

    private String name;

    private Long classDirectorId;

    private String ClassDirectorName;

    private String course;

    private List<StudentDAO> students;

    private List<ModuleDAO> modules;

    //private List<TeacherD>

    public void populate(Class aClass) {

        setClassDirectorId(aClass.getClassDirector().getTeacherId());
        setClassDirectorName(aClass.getClassDirector().getName());
        setClassId(aClass.getClassId());
        setCourse(aClass.getCourse().getName());
        setName(aClass.getName());

        List<StudentDAO> studentDAOS = new ArrayList<>();

        for(int i = 0; i < aClass.getStudents().size(); i++) {

            StudentDAO studentDAO = new StudentDAO();

            studentDAO.populate(aClass.getStudents().get(i), aClass.getClassId());

            studentDAOS.add(studentDAO);
        }

        setStudents(studentDAOS);

        List<ModuleDAO> moduleDAOS = new ArrayList<>();

        for(int i = 0; i < aClass.getModules().size(); i++) {

            ModuleDAO moduleDAO = new ModuleDAO();

            moduleDAO.populate(aClass.getModules().get(i));

            moduleDAOS.add(moduleDAO);
        }

        setModules(moduleDAOS);
    }

    public List<ModuleDAO> getModules() {
        return modules;
    }

    public void setModules(List<ModuleDAO> modules) {
        this.modules = modules;
    }

    public List<StudentDAO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDAO> students) {
        this.students = students;
    }

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

    public Long getClassDirectorId() {
        return classDirectorId;
    }

    public void setClassDirectorId(Long classDirectorId) {
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
