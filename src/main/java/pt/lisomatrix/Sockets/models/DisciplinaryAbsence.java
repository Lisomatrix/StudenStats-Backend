package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "disciplinary_absence")
public class DisciplinaryAbsence implements Serializable {

    @Id
    @Column(name = "disciplinary_absence_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long disciplinaryAbsenceId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "absence_id", nullable = false)
    private Absence absence;

    @Column(nullable =  false)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDisciplinaryAbsenceId() {
        return disciplinaryAbsenceId;
    }

    public void setDisciplinaryAbsenceId(long disciplinaryAbsenceId) {
        this.disciplinaryAbsenceId = disciplinaryAbsenceId;
    }

    public Absence getAbsence() {
        return absence;
    }

    public void setAbsence(Absence absence) {
        this.absence = absence;
    }
}
