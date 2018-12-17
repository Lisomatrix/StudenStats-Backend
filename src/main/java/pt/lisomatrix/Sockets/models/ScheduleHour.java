package pt.lisomatrix.Sockets.models;

import javax.persistence.*;

@Entity
@Table(name = "schedule_hour")
public class ScheduleHour {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long scheduleHourId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hour_id")
    private Hour hour;

    @OneToOne(fetch = FetchType.EAGER)
    private Discipline discipline;

    @OneToOne(fetch = FetchType.EAGER)
    private Class aClass;

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public long getScheduleHourId() {
        return scheduleHourId;
    }

    public void setScheduleHourId(long scheduleHourId) {
        this.scheduleHourId = scheduleHourId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Hour getHour() {
        return hour;
    }

    public void setHour(Hour hour) {
        this.hour = hour;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
