package pt.lisomatrix.Sockets.websocket.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import pt.lisomatrix.Sockets.models.Teacher;
import pt.lisomatrix.Sockets.models.Test;

import java.util.Date;

public class TestDAO {

    private long testId;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date date;

    private Long teacherId;

    private String teacherName;

    private long classId;

    private String className;

    private String discipline;

    public void populate(Test test) {

        setClassId(test.getTestClass().getClassId());
        setClassName(test.getTestClass().getName());
        setDate(test.getDate());
        setDiscipline(test.getDiscipline().getName());
        setTeacherId(test.getTeacher().getTeacherId());
        setTeacherName(test.getTeacher().getName());
        setTestId(test.getTestId());
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
