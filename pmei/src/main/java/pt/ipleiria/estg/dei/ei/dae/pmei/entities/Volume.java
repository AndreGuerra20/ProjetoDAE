package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "volumes")
@NamedQueries(@NamedQuery(name = "getAllVolumes", query = "SELECT v FROM Volume v ORDER BY v.id"))
public class Volume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tipoEmbalagem;

    @ManyToOne
    private Encomenda encomenda;

    @OneToMany(mappedBy = "volume", fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "volume", fetch = FetchType.LAZY)
    private List<Sensor> sensores = new ArrayList<>();

    public Volume() {
    }

    public Volume(String tipoEmbalagem, Encomenda encomenda) {
        this.tipoEmbalagem = tipoEmbalagem;
        this.encomenda = encomenda;
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

    @Override
    public String toString() {
        return "Volume{" +
                "id=" + id +
                ", tipoEmbalagem='" + tipoEmbalagem + '\'' +
                '}';
    }
}
