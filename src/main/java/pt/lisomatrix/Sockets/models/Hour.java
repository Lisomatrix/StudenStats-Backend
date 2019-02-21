package pt.lisomatrix.Sockets.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table(name = "hour")
public class Hour implements Serializable {

    @Id
    @Column(name = "hour_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long hourId;

    //@JsonFormat(pattern = "hh:mm", timezone = "GMT")
    //@Column(columnDefinition="TIME")
    //@Temporal(value = TemporalType.TIME)
    //@DateTimeFormat(pattern = "HH:mm:ss")
    //@JsonSerialize(using = LocalTimeSerializer.class)
    //@JsonDeserialize(using = LocalTimeDeserializer.class)
    private String startTime;

    //@JsonFormat(pattern = "hh:mm", timezone = "GMT")
    //@Column(columnDefinition="TIME")
    //@Temporal(value = TemporalType.TIME)
    //@DateTimeFormat(pattern = "HH:mm")
    //@JsonSerialize(using = LocalTimeSerializer.class)
    //@JsonDeserialize(using = LocalTimeDeserializer.class)
    private String endTime;

    public long getHourId() {
        return hourId;
    }

    public void setHourId(long hourId) {
        this.hourId = hourId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
