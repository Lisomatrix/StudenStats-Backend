package pt.lisomatrix.Sockets.models;

import javax.persistence.*;

@Entity
@Table(name = "permanent_token")
public class PermanentToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long permanentTokenId;

    public String permanentToken;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    public long getPermanentTokenId() {
        return permanentTokenId;
    }

    public void setPermanentTokenId(long permanentTokenId) {
        this.permanentTokenId = permanentTokenId;
    }

    public String getPermanentToken() {
        return permanentToken;
    }

    public void setPermanentToken(String permanentToken) {
        this.permanentToken = permanentToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
