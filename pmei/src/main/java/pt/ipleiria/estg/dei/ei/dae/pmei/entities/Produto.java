package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
@NamedQueries({@NamedQuery(name = "getAllProdutos", query = "SELECT p FROM Produto p ORDER BY p.id")})
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String descricao;

    private boolean precisaEmbalagemAdicional;

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

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPrecisaEmbalagemAdicional(boolean precisaEmbalagemAdicional) {
        this.precisaEmbalagemAdicional = precisaEmbalagemAdicional;
    }

    @Override
    public String toString() {
        return "Produto{" + "id=" + id + ", descricao='" + descricao + '\'' + ", precisaEmbalagemAdicional=" + precisaEmbalagemAdicional + '}';
    }
}
