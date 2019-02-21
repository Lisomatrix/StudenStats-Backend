package pt.lisomatrix.Sockets.models;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "absence")
public class Absence implements Serializable {

    @Id
    @Column(name = "absence_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long absenceId;

    private Date date;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "absenceType_id", nullable = false)
    private AbsenceType absenceType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Column(nullable = false)
    private boolean isJustifiable;

    @Column(nullable = false)
    private boolean isJustified;

    private boolean isRecuperated;

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public boolean isRecuperated() {
        return isRecuperated;
    }

    public void setRecuperated(boolean recuperated) {
        isRecuperated = recuperated;
    }

    public long getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(long absenceId) {
        this.absenceId = absenceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public AbsenceType getAbsenceType() {
        return absenceType;
    }

    public void setAbsenceType(AbsenceType absenceType) {
        this.absenceType = absenceType;
    }

    public boolean isJustifiable() {
        return isJustifiable;
    }

    public void setJustifiable(boolean justifiable) {
        isJustifiable = justifiable;
    }

    public boolean isJustified() {
        return isJustified;
    }

    public void setJustified(boolean justified) {
        isJustified = justified;
    }
}
