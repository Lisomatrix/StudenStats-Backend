package pt.lisomatrix.Sockets.requests.models;

import pt.lisomatrix.Sockets.redis.models.RedisToken;

public class RememberToken {

    private RedisToken redisToken;

    private String permanentToken;

    public RedisToken getRedisToken() {
        return redisToken;
    }

    public void setRedisToken(RedisToken redisToken) {
        this.redisToken = redisToken;
    }

    public String getPermanentToken() {
        return permanentToken;
    }

    public void setPermanentToken(String permanentToken) {
        this.permanentToken = permanentToken;
    }
}
