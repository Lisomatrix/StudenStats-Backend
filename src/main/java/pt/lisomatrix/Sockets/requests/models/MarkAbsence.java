package pt.lisomatrix.Sockets.requests.models;


public class MarkAbsence {

    private Long studentId;

    private long disciplineId;

    private long lessonId;

    private boolean isJustificable;

    private long absenceType;

    private boolean create;

    private String description;

    private Long absenceId;

    public Long getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(Long absenceId) {
        this.absenceId = absenceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public boolean isJustificable() {
        return isJustificable;
    }

    public void setJustificable(boolean justificable) {
        isJustificable = justificable;
    }

    public long getAbsenceType() {
        return absenceType;
    }

    public void setAbsenceType(long absenceType) {
        this.absenceType = absenceType;
    }
}
