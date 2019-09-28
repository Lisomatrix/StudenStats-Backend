package pt.lisomatrix.Sockets.response.models;

import pt.lisomatrix.Sockets.models.Class;

import java.util.ArrayList;
import java.util.List;

public class ClassResponse {

    private long classId;

    private String name;

    private Long classDirectorId;

    private String ClassDirectorName;

    private String course;

    private List<StudentResponse> students;

    private List<ModuleResponse> modules;

    private int year;

    public void populate(Class aClass) {

        setClassDirectorId(aClass.getClassDirector().getTeacherId());
        setClassDirectorName(aClass.getClassDirector().getName());
        setClassId(aClass.getClassId());
        setCourse(aClass.getCourse().getName());
        setName(aClass.getName());

        List<StudentResponse> studentResponses = new ArrayList<>();

        if(aClass.getStudents() != null) {
            for(int i = 0; i < aClass.getStudents().size(); i++) {

                StudentResponse studentResponse = new StudentResponse();

                studentResponse.populate(aClass.getStudents().get(i), aClass.getClassId());

                studentResponses.add(studentResponse);
            }
        }

        setStudents(studentResponses);

        List<ModuleResponse> moduleResponses = new ArrayList<>();

        if(aClass.getModules() != null) {
            for(int i = 0; i < aClass.getModules().size(); i++) {

                ModuleResponse moduleResponse = new ModuleResponse();

                moduleResponse.populate(aClass.getModules().get(i));

                moduleResponses.add(moduleResponse);
            }
        }

        setModules(moduleResponses);
        setYear(aClass.getYear());
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<ModuleResponse> getModules() {
        return modules;
    }

    public void setModules(List<ModuleResponse> modules) {
        this.modules = modules;
    }

    public List<StudentResponse> getStudents() {
        return students;
    }

    public void setStudents(List<StudentResponse> students) {
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
