package pt.lisomatrix.Sockets.redis.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import pt.lisomatrix.Sockets.models.Class;
import pt.lisomatrix.Sockets.models.Student;
import pt.lisomatrix.Sockets.models.Teacher;

import java.io.Serializable;


@RedisHash("authenticated")
public class AuthenticatedUser {

    private long userId;

    @Id
    private String token;

    private String role;

    public AuthenticatedUser() {

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
