package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "encomendas")
@NamedQueries({@NamedQuery(name = "getAllEncomendas", query = "SELECT e FROM Encomenda e ORDER BY e.id")})

public class Encomenda {
    @Id
    private long id;

    public static final List<String> LISTA_ESTADOS = List.of("Pendente","Entregue","Despachada");

    @OneToMany(mappedBy = "encomenda", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Volume> volumes = new ArrayList<>();

    @ManyToOne
    @NotNull
    private Cliente cliente;

    private String estado;

    public Encomenda() {
    }

    public Encomenda(long id,Cliente cliente, String estado) {
        this.id = id;
        this.cliente = cliente;
        if (LISTA_ESTADOS.contains(estado)) {
            this.estado = estado;
        } else {
            throw new IllegalArgumentException("Invalid estado");
        }
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
        for (Volume volume : volumes) {
            volume.setEncomenda(this);
        }
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
        if (LISTA_ESTADOS.contains(estado)) {
            this.estado = estado;
        } else {
            throw new IllegalArgumentException("Invalid estado");
        }
    }

    public void addVolume(Volume volume) {
        this.volumes.add(volume);
    }

    public void removeVolume(Volume volume) {
        this.volumes.remove(volume);
    }

    @Override
    public String toString() {
        return "Encomenda{" + "id=" + id + ", volumes=" + volumes + ", cliente=" + cliente + ", estado='" + estado + '\'' + '}';
    }
}
