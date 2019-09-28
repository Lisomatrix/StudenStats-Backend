package pt.lisomatrix.Sockets.requests.models;

import pt.lisomatrix.Sockets.models.Hour;
import pt.lisomatrix.Sockets.models.ScheduleDay;

import java.util.List;

public class ScheduleData {

    private List<Hour> hours;

    private List<ScheduleDay> scheduleDays;

    public ScheduleData(List<Hour> hours, List<ScheduleDay> scheduleDays) {
        this.hours = hours;
        this.scheduleDays = scheduleDays;
    }

    public ScheduleData() {
        
    }
}
