package pt.lisomatrix.Sockets.models;

import javax.persistence.*;

@Entity
@Table(name = "test_grade")
public class TestGrade {

    @Id
    @Column(name = "test_grade_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long testGradeId;

    @JoinColumn(name = "student_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Student student;

    @JoinColumn(name = "test_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Test test;

    private int grade;

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public long getTestGradeId() {
        return testGradeId;
    }

    public void setTestGradeId(long testGradeId) {
        this.testGradeId = testGradeId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
