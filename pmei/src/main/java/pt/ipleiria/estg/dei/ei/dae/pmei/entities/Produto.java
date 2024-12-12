package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
@NamedQueries({@NamedQuery(name = "getAllProdutos", query = "SELECT p FROM Produto p ORDER BY p.id")})
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String descricao;

    private boolean precisaEmbalagemAdicional;

//    private List<Evento> eventosNecessarios = new ArrayList<>();

    public Produto() {
    }

    public Produto(String descricao, boolean precisaEmbalagemAdicional) {
        this.descricao = descricao;
        this.precisaEmbalagemAdicional = precisaEmbalagemAdicional;
    }

    public long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isPrecisaEmbalagemAdicional() {
        return precisaEmbalagemAdicional;
    }

//    public List<Evento> getEventosNecessarios() {
//        return eventosNecessarios;
//    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPrecisaEmbalagemAdicional(boolean precisaEmbalagemAdicional) {
        this.precisaEmbalagemAdicional = precisaEmbalagemAdicional;
    }

//    public void setEventosNecessarios(List<Evento> eventosNecessarios) {
//        this.eventosNecessarios = eventosNecessarios;
//    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", precisaEmbalagemAdicional=" + precisaEmbalagemAdicional +
                '}';
    }
}
