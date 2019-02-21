package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "absence_type")
public class AbsenceType implements Serializable {

    @Id
    @Column(name = "absenceType_id")
    private Long absenceTypeId;

    @Column(nullable = false)
    private String name;

    public AbsenceType() {

    }

    public AbsenceType(Long id, String name) {
        this.absenceTypeId = id;
        this.name = name;
    }

    public Long getAbsenceTypeId() {
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
