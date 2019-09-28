package pt.lisomatrix.Sockets.response.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import pt.lisomatrix.Sockets.models.Schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleResponse {

    private long scheduleId;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date startDate;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date endDate;

    private List<ScheduleHourResponse> scheduleHourResponseList;

    public void populate(Schedule schedule) {

        setScheduleId(schedule.getSchedulesId());
        setStartDate(schedule.getStartDate());
        setEndDate(schedule.getEndDate());

        List<ScheduleHourResponse> scheduleHourResponses = new ArrayList<>();

        for(int i = 0; i < schedule.getScheduleHours().size(); i++) {

            ScheduleHourResponse scheduleHourResponse = new ScheduleHourResponse();
            scheduleHourResponse.populate(schedule.getScheduleHours().get(i));

            scheduleHourResponses.add(scheduleHourResponse);
        }

        setScheduleHourResponseList(scheduleHourResponses);
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

    public List<ScheduleHourResponse> getScheduleHourResponseList() {
        return scheduleHourResponseList;
    }

    public void setScheduleHourResponseList(List<ScheduleHourResponse> scheduleHourResponseList) {
        this.scheduleHourResponseList = scheduleHourResponseList;
    }
}
