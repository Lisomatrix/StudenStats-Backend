package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "schedule_day")
public class ScheduleDay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long scheduleDayId;

    private String day;

    public long getScheduleDayId() {
        return scheduleDayId;
    }

    public void setScheduleDayId(long scheduleDayId) {
        this.scheduleDayId = scheduleDayId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
