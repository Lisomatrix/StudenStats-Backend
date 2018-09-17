package pt.lisomatrix.Sockets.requests.models;

public class Authentication {

    private String email;

    private String password;

    public Authentication() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
