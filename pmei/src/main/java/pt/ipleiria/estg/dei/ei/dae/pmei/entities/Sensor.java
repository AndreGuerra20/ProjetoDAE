package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Sensor {
    @Id
    private long id;

    private String tipo;

    private Boolean status;

    private long volume;

    @OneToMany(mappedBy = "sensor")
    private List<Evento> eventos = new ArrayList<>();

    public Sensor() {
    }

    public Sensor(long id, String tipo, Boolean status) {
        this.id = id;
        this.tipo = tipo;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", status=" + status +
                ", volume=" + volume +
                '}';
    }
}
