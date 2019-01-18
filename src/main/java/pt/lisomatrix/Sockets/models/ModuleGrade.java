package pt.lisomatrix.Sockets.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "module_grade")
public class ModuleGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long moduleGradeId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", nullable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Module module;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Student student;

    @OneToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Grade> grades;

    private int grade;

    public long getModuleGradeId() {
        return moduleGradeId;
    }

    public void setModuleGradeId(long moduleGradeId) {
        this.moduleGradeId = moduleGradeId;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
