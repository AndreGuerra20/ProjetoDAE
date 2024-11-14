package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Encomenda {
    @Id
    private long id;

    @OneToMany(mappedBy = "encomenda")
    private List<Volume> volumes = new ArrayList<>();

    @ManyToOne
    private Cliente cliente;

    private String estado;

    public Encomenda() {
    }

    public Encomenda(long id, Cliente cliente, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.estado = estado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void addVolume(Volume volume) {
        this.volumes.add(volume);
    }

    @Override
    public String toString() {
        return "Encomenda{" +
                "id=" + id +
                ", volumes=" + volumes +
                ", cliente=" + cliente +
                ", estado='" + estado + '\'' +
                '}';
    }
}
