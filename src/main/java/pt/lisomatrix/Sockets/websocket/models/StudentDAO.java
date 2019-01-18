package pt.lisomatrix.Sockets.websocket.models;

import pt.lisomatrix.Sockets.models.Student;

public class StudentDAO {

    private Long studentId;

    private String name;

    private long classId;

    public void populate(Student student, long classId) {

        setName(student.getName());
        setStudentId(student.getStudentId());
        setClassId(classId);
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
