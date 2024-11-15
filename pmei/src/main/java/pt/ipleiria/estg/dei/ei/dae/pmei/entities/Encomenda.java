package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Encomenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "encomenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Volume> volumes = new ArrayList<>();

    @ManyToOne
    private Cliente cliente;

    private String estado;

    public Encomenda() {
    }

    public Encomenda(Cliente cliente, String estado) {
        this.cliente = cliente;
        this.estado = estado;
    }

    public long getId() {
        return id;
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
