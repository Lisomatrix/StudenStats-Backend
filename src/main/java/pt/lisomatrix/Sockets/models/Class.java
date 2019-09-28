package pt.lisomatrix.Sockets.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "class")
public class Class implements Serializable {

    @Id
    @Column(name = "class_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long classId;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher classDirector;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Student> students;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Teacher> teachers;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Discipline> disciplines;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Module> modules;

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /*@JsonIgnore
    @OneToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    private Schedule schedule;*/

    private int year;

    public Class() {

    }

    public Class(long classId) {
        this.classId = classId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /*public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }*/

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getClassDirector() {
        return classDirector;
    }

    public void setClassDirector(Teacher classDirector) {
        this.classDirector = classDirector;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
