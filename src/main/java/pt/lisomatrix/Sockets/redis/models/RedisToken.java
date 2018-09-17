package pt.lisomatrix.Sockets.redis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import pt.lisomatrix.Sockets.constants.Roles;

@RedisHash("tokens")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedisToken {

    @Id
    private String token;

    private Boolean isUsed;

    private String Role;

    private String ipAddress;

    public Boolean getUsed() {
        return isUsed;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }
}
