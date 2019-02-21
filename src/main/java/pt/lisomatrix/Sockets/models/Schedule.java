package pt.lisomatrix.Sockets.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "schedule")
public class Schedule implements Serializable {

    @Id
    @Column(name = "schedule_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long schedulesId;

    private Date startDate;

    private Date endDate;

    @OneToMany(mappedBy = "schedule")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ScheduleHour> scheduleHours = new ArrayList<>();

    public List<ScheduleHour> getScheduleHours() {
        return scheduleHours;
    }

    public void setScheduleHours(List<ScheduleHour> scheduleHours) {
        this.scheduleHours = scheduleHours;
    }

    public long getSchedulesId() {
        return schedulesId;
    }

    public void setSchedulesId(long schedulesId) {
        this.schedulesId = schedulesId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
