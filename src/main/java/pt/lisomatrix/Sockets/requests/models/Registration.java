package pt.lisomatrix.Sockets.requests.models;

import java.io.Serializable;

public class Registration implements Serializable {

    private String email;

    private String password;

    private String registrationCode;

    public Registration() {

    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
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
