package pt.lisomatrix.Sockets.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "storage")
public class Storage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long storageId;

    private Long capacity;

    private Long used;

    private String ip;

    private boolean isMaster;

    private int storageNumber;

    @OneToMany(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn
    private List<File> files;

    public int getStorageNumber() {
        return storageNumber;
    }

    public void setStorageNumber(int storageNumber) {
        this.storageNumber = storageNumber;
    }

    public List<File> getFiles() {
        return files;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
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
}
