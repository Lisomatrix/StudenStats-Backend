package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "module")
public class Module implements Serializable {

    @Id
    @Column(name = "module_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long moduleId;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    @Column(nullable = false)
    private long hours;

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
