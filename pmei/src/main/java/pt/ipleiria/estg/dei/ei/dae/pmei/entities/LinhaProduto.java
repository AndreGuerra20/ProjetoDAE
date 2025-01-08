package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

@Entity
@NamedQueries({@NamedQuery(name = "getAllLinhaProdutos", query = "SELECT lp FROM LinhaProduto lp")})
public class LinhaProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Produto produto;
    private int quantidade;

    @ManyToOne
    private Volume volume;

    public LinhaProduto() {
    }

    public LinhaProduto(Produto produto, int quantidade, Volume volume) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.volume = volume;
    }

    public long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "LinhaProduto{" +
                "id=" + id +
                ", produto=" + produto +
                ", quantidade=" + quantidade +
                ", volume=" + volume +
                '}';
    }
}
