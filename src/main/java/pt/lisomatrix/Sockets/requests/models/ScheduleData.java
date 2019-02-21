package pt.lisomatrix.Sockets.requests.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.lisomatrix.Sockets.models.Hour;
import pt.lisomatrix.Sockets.models.ScheduleDay;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleData {

    private List<Hour> hours;

    private List<ScheduleDay> scheduleDays;
}
