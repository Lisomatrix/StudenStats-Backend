package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "class_room")
public class ClassRoom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long classRoomId;

    private String name;

    public long getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(long classRoomId) {
        this.classRoomId = classRoomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
