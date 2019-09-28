package pt.lisomatrix.Sockets.response.models;

import pt.lisomatrix.Sockets.models.Student;

public class StudentResponse {

    private Long studentId;

    private String name;

    private long classId;

    public void populate(Student student, long classId) {

        setName(student.getName());
        setStudentId(student.getStudentId());
        setClassId(classId);
    }

    public void populate(Student student) {

        setName(student.getName());
        setStudentId(student.getStudentId());
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}