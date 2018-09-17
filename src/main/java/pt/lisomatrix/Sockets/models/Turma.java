package pt.lisomatrix.Sockets.models;

import javax.persistence.*;
import java.util.List;

@Table(name = "turma")
@Entity
public class Turma {

    @Id
    @Column(name = "turma_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long turmaId;

    @OneToMany(fetch = FetchType.EAGER)
   /* @JoinTable(name = "user_turma",
            joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "turma_id", referencedColumnName = "turma_id")
    )*/
    //@JoinColumn(name = "person_id")
    private List<User> alunos;

    @Column(nullable = false)
    private String anoLetivo;

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public List<User> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<User> alunos) {
        this.alunos = alunos;
    }

    public String getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(String anoLetivo) {
        this.anoLetivo = anoLetivo;
    }
}
