package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensores")
@NamedQueries(@NamedQuery(name = "getAllSensores", query = "SELECT s FROM Sensor s ORDER BY s.id"))
public class Sensor {
    private static final List<String> sensorTypes = List.of("Temperatura", "Pressao", "Posicionamento Global", "Aceleracao");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tipo;

    private Boolean status = false;

    @ManyToOne
    private Volume volume;

    @OneToMany(mappedBy = "sensor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Evento> eventos = new ArrayList<>();

    public Sensor() {
    }

    public Sensor(String tipo, Boolean status, Volume volume) {
        if (!sensorTypes.contains(tipo)) {
            throw new IllegalArgumentException("Invalid sensor type");
        }
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
        if (!sensorTypes.contains(tipo)) {
            throw new IllegalArgumentException("Invalid sensor type");
        }
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

    public void addEventos(List<Evento> eventos) {
        this.eventos.addAll(eventos);
    }

    @Override
    public String toString() {
        return "Sensor{" + "id=" + id + ", tipo='" + tipo + '\'' + ", status=" + status + ", volume=" + volume.getIdVolume() + '}';
    }
}
