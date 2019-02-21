package pt.lisomatrix.Sockets.redis.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("storage_server")
public class RedisStorageServer {

    @Id
    private Long storageNumber;

    private Long capacity;

    private Long used;

    private String ip;

    public Long getStorageNumber() {
        return storageNumber;
    }

    public void setStorageNumber(Long storageNumber) {
        this.storageNumber = storageNumber;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
