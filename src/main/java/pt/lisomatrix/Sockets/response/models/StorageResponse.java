package pt.lisomatrix.Sockets.response.models;

import pt.lisomatrix.Sockets.models.Storage;

public class StorageResponse {

    private long storageId;

    private int storageNumber;

    private String Ip;

    private long totalSpace;

    private long usedSpace;

    private boolean isMaster;

    private boolean isOnline;

    public StorageResponse() {

    }

    public StorageResponse(Storage storage) {
        populate(storage);
    }

    private void populate(Storage storage) {
        setIp(storage.getIp());
        setMaster(storage.isMaster());
        setTotalSpace(storage.getCapacity());
        setStorageId(storage.getStorageId());
        setStorageNumber(storage.getStorageNumber());
        setUsedSpace(storage.getUsed());
        setOnline(false);
    }

    public long getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(long usedSpace) {
        this.usedSpace = usedSpace;
    }

    public long getStorageId() {
        return storageId;
    }

    public void setStorageId(long storageId) {
        this.storageId = storageId;
    }

    public int getStorageNumber() {
        return storageNumber;
    }

    public void setStorageNumber(int storageNumber) {
        this.storageNumber = storageNumber;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
