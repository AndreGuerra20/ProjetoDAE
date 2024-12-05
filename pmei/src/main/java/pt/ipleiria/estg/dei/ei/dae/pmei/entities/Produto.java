package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
@NamedQueries({@NamedQuery(name = "getAllProdutos", query = "SELECT p FROM Produto p ORDER BY p.id")})
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int quantidade;

    @ManyToOne
    private Volume volume;

    public Produto() {
    }

    public Produto(int quantidade, Volume volume) {
        this.quantidade = quantidade;
        this.volume = volume;
    }

    public long getId() {
        return id;
    }

    public int getQuantidade() {
        return quantidade;
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
        return "Produto{" +
                "id=" + id +
                ", quantidade=" + quantidade +
                ", volume=" + volume.getIdVolume() +
                '}';
    }
}
