package pt.lisomatrix.Sockets.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Discipline")
public class Discipline implements Serializable {

    @Id
    @Column(name = "discipline_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long disciplineId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String abbreviation;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Teacher> teachers;

    public  Discipline() {

    }

    public Discipline(long disciplineId) {
        this.disciplineId = disciplineId;
    }

    @JsonIgnore
    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
