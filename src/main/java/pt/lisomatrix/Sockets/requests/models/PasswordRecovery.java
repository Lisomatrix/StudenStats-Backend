package pt.lisomatrix.Sockets.requests.models;

public class PasswordRecovery {

    private int resetCode;

    private long resetId;

    private String password;

    public int getResetCode() {
        return resetCode;
    }

    public void setResetCode(int resetCode) {
        this.resetCode = resetCode;
    }

    public long getResetId() {
        return resetId;
    }

    public void setResetId(long resetId) {
        this.resetId = resetId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
