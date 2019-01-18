package pt.lisomatrix.Sockets.websocket.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import pt.lisomatrix.Sockets.models.Absence;

import java.util.Date;

public class AbsenceDAO {

    private long absenceId;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private Date date;

    private long lessonId;

    private Long studentId;

    private String AbsenceType;

    private String discipline;

    private boolean isJustifiable;

    private boolean isJustified;

    private boolean isRecuperated;

    private long moduleId;

    public void populate(Absence absence) {

        setStudentId(absence.getStudent().getStudentId());
        setAbsenceId(absence.getAbsenceId());
        setLessonId(absence.getLesson().getLessonId());
        setAbsenceType(absence.getAbsenceType().getName());
        setDate(absence.getDate());
        setDiscipline(absence.getDiscipline().getName());
        setJustifiable(absence.isJustifiable());
        setJustified(absence.isJustified());
        setRecuperated(absence.isRecuperated());
        setModuleId(absence.getModule().getModuleId());
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public boolean isRecuperated() {
        return isRecuperated;
    }

    public void setRecuperated(boolean recuperated) {
        isRecuperated = recuperated;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
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

    public boolean isJustifiable() {
        return isJustifiable;
    }

    public void setJustifiable(boolean justifiable) {
        isJustifiable = justifiable;
    }

    public boolean isJustified() {
        return isJustified;
    }

    public void setJustified(boolean justified) {
        isJustified = justified;
    }
}
