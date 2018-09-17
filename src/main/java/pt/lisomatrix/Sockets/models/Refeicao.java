package pt.lisomatrix.Sockets.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "refeicoes")
public class Refeicao {

    @Id
    private Long refeicaoId;

    private Date data;

    private Long alunoId;

    public Refeicao() {

    }

    public Long getRefeicaoId() {
        return refeicaoId;
    }

    public void setRefeicaoId(Long refeicaoId) {
        this.refeicaoId = refeicaoId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }
}
