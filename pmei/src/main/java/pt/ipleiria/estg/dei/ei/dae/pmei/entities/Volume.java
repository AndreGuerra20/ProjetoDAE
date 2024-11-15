package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "volumes")
public class Volume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tipoEmbalagem;

    @ManyToOne
    @JoinColumn(name = "encomenda_id")
    private Encomenda encomenda;

    @OneToMany(mappedBy = "volume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "volume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensores = new ArrayList<>();

    public Volume() {
    }

    public Volume(String tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public long getId() {
        return id;
    }

    public String getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(String tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }

    public void addProduto(Produto produto) {
        produtos.add(produto);
    }

    public void addSensor(Sensor sensor) {
        sensores.add(sensor);
    }
}
