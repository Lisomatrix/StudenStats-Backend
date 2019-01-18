package pt.lisomatrix.Sockets.models;

import javax.persistence.*;

@Entity
@Table(name = "class_room")
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
