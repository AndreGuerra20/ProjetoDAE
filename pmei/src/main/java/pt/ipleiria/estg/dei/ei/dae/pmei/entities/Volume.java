package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Volume {
    @Id
    private long id;

    private String tipoEmbalagem;

    private long encomenda;

    @OneToMany(mappedBy = "volume")
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "volume")
    private List<Sensor> sensores = new ArrayList<>();

    public Volume() {
    }

    public Volume(long id, String tipoEmbalagem) {
        this.id = id;
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(long encomenda) {
        this.encomenda = encomenda;
    }

    public void addProduto(Produto produto) {
        produtos.add(produto);
    }

    public void addSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    @Override
    public String toString() {
        return "Volume{" +
                "id=" + id +
                ", tipoEmbalagem='" + tipoEmbalagem + '\'' +
                ", encomenda=" + encomenda +
                ", produtos=" + produtos +
                ", sensores=" + sensores +
                '}';
    }
}
