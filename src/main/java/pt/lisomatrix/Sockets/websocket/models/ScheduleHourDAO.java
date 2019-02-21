package pt.lisomatrix.Sockets.websocket.models;

import pt.lisomatrix.Sockets.models.Hour;
import pt.lisomatrix.Sockets.models.ScheduleDay;
import pt.lisomatrix.Sockets.models.ScheduleHour;

public class ScheduleHourDAO {

    private long scheduleHourId;

    private Hour hour;

    private long disciplineId;

    private ScheduleDay day;

    public void populate(ScheduleHour scheduleHour) {

        setDisciplineId(scheduleHour.getDiscipline().getDisciplineId());
        setHour(scheduleHour.getHour());
        setScheduleHourId(scheduleHour.getScheduleHourId());
        setDay(scheduleHour.getScheduleDay());
    }

    public ScheduleDay getDay() {
        return day;
    }

    public void setDay(ScheduleDay day) {
        this.day = day;
    }

    public long getScheduleHourId() {
        return scheduleHourId;
    }

    public void setScheduleHourId(long scheduleHourId) {
        this.scheduleHourId = scheduleHourId;
    }

    public Hour getHour() {
        return hour;
    }

    public void setHour(Hour hour) {
        this.hour = hour;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }

}
