package pt.lisomatrix.Sockets.models;

import javax.persistence.*;

@Table(name = "disciplina")
@Entity
public class Disciplina {

    @Id
    @Column(name = "disciplina_id", nullable = false)
    private Long id;

    @Column(name = "nome_disciplina",nullable = false)
    private String nomeDisciplina;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }
}
