package pt.lisomatrix.Sockets.response.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import pt.lisomatrix.Sockets.models.Homework;

import java.util.Date;

public class HomeworkResponse {

    private long homeworkId;

    private String title;

    private String description;

    private long classId;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expireDate;

    private long disciplineId;

    public void populate(Homework homework, long classId) {
        setHomeworkId(homework.getHomeworkId());
        setDescription(homework.getDescription());
        setExpireDate(homework.getExpireDate());
        setTitle(homework.getTitle());
        setClassId(classId);
        setDisciplineId(homework.getDiscipline().getDisciplineId());
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public long getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(long homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
