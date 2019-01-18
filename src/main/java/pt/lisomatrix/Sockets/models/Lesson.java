package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @Column(name = "lesson_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long lessonId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = false)
    private Class classe;

    @OneToOne(fetch = FetchType.EAGER)
    private ScheduleHour scheduleHour;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    private Date date;

    private int lessonNumber;

    private String summary;

    public Lesson(long lessonId) {
        this.lessonId = lessonId;
    }

    public Lesson() {

    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public ScheduleHour getScheduleHour() {
        return scheduleHour;
    }

    public void setScheduleHour(ScheduleHour scheduleHour) {
        this.scheduleHour = scheduleHour;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Class getClasse() {
        return classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
