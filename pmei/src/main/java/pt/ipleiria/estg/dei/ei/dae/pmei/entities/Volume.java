package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "volumes")
@NamedQueries(@NamedQuery(name = "getAllVolumes", query = "SELECT v FROM Volume v ORDER BY v.idVolume"))
public class Volume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idVolume;

    private String tipoEmbalagem;

    @ManyToOne
    private Encomenda encomenda;

    @OneToMany(mappedBy = "volume", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "volume", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Sensor> sensores = new ArrayList<>();

    public Volume() {
    }

    public Volume(String tipoEmbalagem, Encomenda encomenda) {
        this.tipoEmbalagem = tipoEmbalagem;
        this.encomenda = encomenda;
    }

    public long getIdVolume() {
        return idVolume;
    }

    public String getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(String tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
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

    public void addProduto(Produto produto) {
        produtos.add(produto);
    }

    public void removeProduto(Produto produto) {
        produtos.remove(produto);
    }

    public void addSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    public void removeSensor(Sensor sensor) {
        sensores.remove(sensor);
    }

    @Override
    public String toString() {
        return "Volume{" +
                "id=" + idVolume +
                ", tipoEmbalagem='" + tipoEmbalagem + '\'' +
                '}';
    }
}
