package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "homework")
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long homeworkId;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private Class aClass;

    private Date expireDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

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

    public long getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(long homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
