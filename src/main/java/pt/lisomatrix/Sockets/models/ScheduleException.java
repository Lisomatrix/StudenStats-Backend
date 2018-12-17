package pt.lisomatrix.Sockets.models;

import javax.persistence.*;

@Entity
@Table(name = "schedule_exception")
public class ScheduleException {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long scheduleExceptionId;

    @OneToOne(fetch = FetchType.EAGER)
    private Hour hour;

    @OneToOne(fetch = FetchType.EAGER)
    private Class aClass;

    @OneToOne(fetch = FetchType.EAGER)
    private Discipline discipline;

    @OneToOne(fetch = FetchType.EAGER)
    private Schedule schedule;

    public long getScheduleExceptionId() {
        return scheduleExceptionId;
    }

    public void setScheduleExceptionId(long scheduleExceptionId) {
        this.scheduleExceptionId = scheduleExceptionId;
    }

    public Hour getHour() {
        return hour;
    }

    public void setHour(Hour hour) {
        this.hour = hour;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
