package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "volumes")
@NamedQueries(@NamedQuery(name = "getAllVolumes", query = "SELECT v FROM Volume v ORDER BY v.idVolume"))
public class Volume {
    @Id
    private long idVolume;

    private String tipoEmbalagem;

    @ManyToOne
    private Encomenda encomenda;

    @OneToMany(mappedBy = "volume", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LinhaProduto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "volume", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Sensor> sensores = new ArrayList<>();

    private boolean isEntregue;

    public Volume() {
    }

    public Volume(long id,String tipoEmbalagem, Encomenda encomenda) {
        this.idVolume = id;
        this.tipoEmbalagem = tipoEmbalagem;
        this.encomenda = encomenda;
        this.isEntregue = false;
    }

    public void setIdVolume(long idVolume) {
        this.idVolume = idVolume;
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

    public List<LinhaProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<LinhaProduto> produtos) {
        this.produtos = produtos;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public void addProduto(LinhaProduto produto) {
        produtos.add(produto);
    }

    public void addProdutos(List<LinhaProduto> produtos) {
        this.produtos.addAll(produtos);
    }

    public void removeProduto(LinhaProduto produto) {
        produtos.remove(produto);
    }

    public void addSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    public void addSensores(List<Sensor> sensores) {
        this.sensores.addAll(sensores);
    }

    public void removeSensor(Sensor sensor) {
        sensores.remove(sensor);
    }

    public boolean isEntregue() {
        return isEntregue;
    }

    public void setEntregue(boolean entregue) {
        isEntregue = entregue;
    }

    @Override
    public String toString() {
        return "Volume{" + "id=" + idVolume + ", tipoEmbalagem='" + tipoEmbalagem + ", isEntregue='" + isEntregue + '\'' + '}';
    }
}
