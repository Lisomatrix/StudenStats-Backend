package pt.lisomatrix.Sockets.websocket.models;

import org.springframework.data.annotation.Id;

public class StorageAuthorization {

    @Id
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
