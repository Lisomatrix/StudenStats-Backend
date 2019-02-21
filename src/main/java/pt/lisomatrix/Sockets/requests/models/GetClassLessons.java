package pt.lisomatrix.Sockets.requests.models;

public class GetClassLessons {

    private long classId;

    private long disciplineId;

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }
}
