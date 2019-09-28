package pt.lisomatrix.Sockets.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_settings")
public class UserSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userSettingsId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 600)
    private String theme;

    @Column(length = 600)
    private String settings;

    @Column(name = "allow_emails")
    private boolean allowEmails;

    public boolean isAllowEmails() {
        return allowEmails;
    }

    public void setAllowEmails(boolean allowEmails) {
        this.allowEmails = allowEmails;
    }

    public long getUserSettingsId() {
        return userSettingsId;
    }

    public void setUserSettingsId(long userSettingsId) {
        this.userSettingsId = userSettingsId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }
}
