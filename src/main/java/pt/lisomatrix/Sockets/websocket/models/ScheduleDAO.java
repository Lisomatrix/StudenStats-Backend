package pt.lisomatrix.Sockets.websocket.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import pt.lisomatrix.Sockets.models.Schedule;
import pt.lisomatrix.Sockets.models.ScheduleHour;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleDAO {

    private long scheduleId;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date startDate;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date endDate;

    private List<ScheduleHourDAO> scheduleHourDAOList;

    public void populate(Schedule schedule) {

        setScheduleId(schedule.getSchedulesId());
        setStartDate(schedule.getStartDate());
        setEndDate(schedule.getEndDate());

        List<ScheduleHourDAO> scheduleHourDAOS = new ArrayList<>();

        for(int i = 0; i < schedule.getScheduleHours().size(); i++) {

            ScheduleHourDAO scheduleHourDAO = new ScheduleHourDAO();
            scheduleHourDAO.populate(schedule.getScheduleHours().get(i));

            scheduleHourDAOS.add(scheduleHourDAO);
        }

        setScheduleHourDAOList(scheduleHourDAOS);
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
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

    public List<ScheduleHourDAO> getScheduleHourDAOList() {
        return scheduleHourDAOList;
    }

    public void setScheduleHourDAOList(List<ScheduleHourDAO> scheduleHourDAOList) {
        this.scheduleHourDAOList = scheduleHourDAOList;
    }
}
