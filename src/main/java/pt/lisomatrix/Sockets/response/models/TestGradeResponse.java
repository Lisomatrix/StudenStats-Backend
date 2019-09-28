package pt.lisomatrix.Sockets.response.models;

import pt.lisomatrix.Sockets.models.TestGrade;

public class TestGradeResponse {

    private long testGradeId;

    private long studentId;

    private long testId;

    private int grade;

    public void populate(TestGrade testGrade) {
        setGrade(testGrade.getGrade());
        setStudentId(testGrade.getStudent().getStudentId());
        setTestGradeId(testGrade.getTestGradeId());
        setTestId(testGrade.getTest().getTestId());
    }

    public long getTestGradeId() {
        return testGradeId;
    }

    public void setTestGradeId(long testGradeId) {
        this.testGradeId = testGradeId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
