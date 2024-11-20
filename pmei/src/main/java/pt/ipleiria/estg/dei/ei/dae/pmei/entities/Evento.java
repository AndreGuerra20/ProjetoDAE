package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table(name = "eventos")
@NamedQueries({@NamedQuery(name = "getAllEventos", query = "SELECT e FROM Evento e")})
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String valor;

    private String timestamp;

    @ManyToOne
    private Sensor sensor;

    public Evento() {
    }

    public Evento(String valor, Sensor sensor) {
        this.valor = valor;
        this.sensor = sensor;
        this.timestamp = DateTimeFormatter.ISO_INSTANT.format(new Date().toInstant());
    }

    public long getId() {
        return id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "Evento{" + "id=" + id + ", valor='" + valor + '\'' + ", timestamp=" + timestamp + ", sensor=" + sensor.getId() + '}';
    }
}
