package pt.lisomatrix.Sockets.requests.models;

public class CreateStorage {

    private boolean isMaster;

    private boolean hasSlave;

    private String slaveIp;

    private int StorageNumber;

    private String storageIp;

    private long totalSpace;

    public long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public boolean isHasSlave() {
        return hasSlave;
    }

    public void setHasSlave(boolean hasSlave) {
        this.hasSlave = hasSlave;
    }

    public String getSlaveIp() {
        return slaveIp;
    }

    public void setSlaveIp(String slaveIp) {
        this.slaveIp = slaveIp;
    }

    public int getStorageNumber() {
        return StorageNumber;
    }

    public void setStorageNumber(int storageNumber) {
        StorageNumber = storageNumber;
    }

    public String getStorageIp() {
        return storageIp;
    }

    public void setStorageIp(String storageIp) {
        this.storageIp = storageIp;
    }
}
