package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "test")
public class Test {

    @Id
    @Column(name = "test_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long testId;

    private Date date;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = false)
    private Class testClass;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private Module module;

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
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

    public Class getTestClass() {
        return testClass;
    }

    public void setTestClass(Class testClass) {
        this.testClass = testClass;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
