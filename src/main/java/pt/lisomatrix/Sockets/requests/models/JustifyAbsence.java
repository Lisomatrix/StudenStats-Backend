package pt.lisomatrix.Sockets.requests.models;

public class JustifyAbsence {

    private long absenceId;

    private boolean isJustified;

    public boolean isJustified() {
        return isJustified;
    }

    public void setJustified(boolean justified) {
        isJustified = justified;
    }

    public long getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(long absenceId) {
        this.absenceId = absenceId;
    }
}
