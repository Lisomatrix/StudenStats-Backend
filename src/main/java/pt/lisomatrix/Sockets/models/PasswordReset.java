package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "password_reset")
@Entity
public class PasswordReset implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String passwordResetId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int resetCode;

    private boolean isUsed;

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public int getResetCode() {
        return resetCode;
    }

    public void setResetCode(int resetCode) {
        this.resetCode = resetCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPasswordResetId() {
        return passwordResetId;
    }

    public void setPasswordResetId(String passwordResetId) {
        this.passwordResetId = passwordResetId;
    }
}
