package pt.lisomatrix.Sockets.requests.models;

public class UpdateLessonSummary {

    public long lessonId;

    public String summary;

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
