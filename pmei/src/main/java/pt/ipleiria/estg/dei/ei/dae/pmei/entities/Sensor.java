package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "sensores")
@NamedQueries(@NamedQuery(name = "getAllSensores", query = "SELECT s FROM Sensor s LEFT JOIN FETCH s.eventos ORDER BY s.id"))
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tipo;

    private Boolean status;

    @ManyToOne
    private Volume volume;

    @OneToMany(mappedBy = "sensor")
    private List<Evento> eventos = new ArrayList<>();

    public Sensor() {
    }

    public Sensor(String tipo, Boolean status, Volume volume) {
        this.tipo = tipo;
        this.status = status;
        this.volume = volume;
    }

    public long getId() {
        return id;
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

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    public void addEvento(Evento evento) {
        eventos.add(evento);
    }

    @Override
    public String toString() {
        return "Sensor{" + "id=" + id + ", tipo='" + tipo + '\'' + ", status=" + status + ", volume=" + volume.getId() + '}';
    }
}
