package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "absence")
public class Absence {

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

    @Column(nullable = false)
    private boolean isJustificable;

    @Column(nullable = false)
    private boolean isJustified;

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

    public boolean isJustificable() {
        return isJustificable;
    }

    public void setJustificable(boolean justificable) {
        isJustificable = justificable;
    }

    public boolean isJustified() {
        return isJustified;
    }

    public void setJustified(boolean justified) {
        isJustified = justified;
    }
}
