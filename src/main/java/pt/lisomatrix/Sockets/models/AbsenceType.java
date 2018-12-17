package pt.lisomatrix.Sockets.models;

import javax.persistence.*;

@Entity
@Table(name = "absence_type")
public class AbsenceType {

    @Id
    @Column(name = "absenceType_id")
    private long absenceTypeId;

    @Column(nullable = false)
    private String name;

    public long getAbsenceTypeId() {
        return absenceTypeId;
    }

    public void setAbsenceTypeId(long absenceTypeId) {
        this.absenceTypeId = absenceTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
