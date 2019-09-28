package pt.lisomatrix.Sockets.requests.models;

import java.util.Date;

public class MarkHomework {

    private String title;

    private String description;

    private Date date;

    private long disciplineId;

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
