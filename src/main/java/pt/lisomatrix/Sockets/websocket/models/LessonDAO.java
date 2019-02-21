package pt.lisomatrix.Sockets.websocket.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import pt.lisomatrix.Sockets.models.Lesson;

import java.util.Date;

public class LessonDAO {

    private long lessonId;

    private long disciplineId;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date date;

    private long classId;

    private int lessonNumber;

    private String summary;

    private ModuleDAO module;

    public ModuleDAO getModule() {
        return module;
    }

    public void setModule(ModuleDAO module) {
        this.module = module;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }
}
