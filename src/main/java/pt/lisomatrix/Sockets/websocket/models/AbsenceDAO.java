package pt.lisomatrix.Sockets.websocket.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AbsenceDAO {

    private long absenceId;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date date;

    private long lessonId;

    private String studentId;

    private String AbsenceType;

    private String discipline;

    private boolean isJustificable;

    private boolean isJustified;

    private boolean isRecuperated;

    public boolean isRecuperated() {
        return isRecuperated;
    }

    public void setRecuperated(boolean recuperated) {
        isRecuperated = recuperated;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public long getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(long absenceId) {
        this.absenceId = absenceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getAbsenceType() {
        return AbsenceType;
    }

    public void setAbsenceType(String absenceType) {
        AbsenceType = absenceType;
    }

    public boolean isJustificable() {
        return isJustificable;
    }

    public void setJustificable(boolean justificable) {
        isJustificable = justificable;
    }

    public boolean isJustified() {
        return isJustified;
    }

    public void setJustified(boolean justified) {
        isJustified = justified;
    }
}
