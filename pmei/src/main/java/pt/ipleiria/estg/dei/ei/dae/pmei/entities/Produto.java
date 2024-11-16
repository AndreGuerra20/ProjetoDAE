package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "volume_id")
    private Volume volume;

    public Produto() {
    }

    public Produto(int quantidade) {
        this.quantidade = quantidade;
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
                ", volume=" + volume.getId() +
                '}';
    }
}
