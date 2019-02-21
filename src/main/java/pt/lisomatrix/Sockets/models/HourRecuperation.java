package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "hour_recuperation")
public class HourRecuperation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long hourRecuperationId;

    private Date date;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Absence> absences;

    public long getHourRecuperationId() {
        return hourRecuperationId;
    }

    public void setHourRecuperationId(long hourRecuperationId) {
        this.hourRecuperationId = hourRecuperationId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }
}
