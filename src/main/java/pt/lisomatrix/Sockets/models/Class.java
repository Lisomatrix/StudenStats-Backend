package pt.lisomatrix.Sockets.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "class")
public class Class {

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

    //@OneToMany(fetch = FetchType.EAGER)
    @OneToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Student> students;

    //@OneToMany(fetch = FetchType.EAGER)
    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Teacher> teachers;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Discipline> disciplines;

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
