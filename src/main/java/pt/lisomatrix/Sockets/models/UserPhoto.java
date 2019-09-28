package pt.lisomatrix.Sockets.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "user_profile_photos")
public class UserPhoto {

    @Id
    @Column(name = "user_photo_id")
    private String userPhotoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @Column(name = "photo_name")
    private String photoName;

    @Column(name = "photo_path")
    @JsonIgnore
    private String photoPath;

    @Column(name = "storage_number")
    @JsonIgnore
    private int storageNumber;

    public String getUserPhotoId() {
        return userPhotoId;
    }

    public void setUserPhotoId(String userPhotoId) {
        this.userPhotoId = userPhotoId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getStorageNumber() {
        return storageNumber;
    }

    public void setStorageNumber(int storageNumber) {
        this.storageNumber = storageNumber;
    }
}
