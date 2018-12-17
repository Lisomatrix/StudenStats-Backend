package pt.lisomatrix.Sockets.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "hour")
public class Hour {

    @Id
    @Column(name = "hour_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long hourId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm", timezone = "UTC")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm", timezone = "UTC")
    private Date endTime;

    public long getHourId() {
        return hourId;
    }

    public void setHourId(long hourId) {
        this.hourId = hourId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
