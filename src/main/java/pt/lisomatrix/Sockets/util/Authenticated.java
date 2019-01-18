package pt.lisomatrix.Sockets.util;

import pt.lisomatrix.Sockets.redis.models.RedisUserStorage;

public class Authenticated {

    private Boolean authenticated;

    private String role;

    private Long userId;

    private String ip;

    public Authenticated(boolean authenticated, String role, Long userId, String ip) {
        this.authenticated = authenticated;
        this.role = role;
        this.userId = userId;
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }
}
