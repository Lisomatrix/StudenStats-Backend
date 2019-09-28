package pt.lisomatrix.Sockets.requests.models;

public class SendMessage {

    private String message;

    private String subject;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
