package pt.lisomatrix.Sockets.requests.models;

public class Response {

    private Object message;

    private Boolean success;

    public Response() {

    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
