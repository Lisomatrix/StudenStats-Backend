package pt.lisomatrix.Sockets.requests.models;

public class NewModule {

    private String name;

    private long disciplineId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }
}
