package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Evento {
    @Id
    private long id;

    private String valor;

    private Date timestamp;

    private long sensor;

    public Evento() {
    }

    public Evento(long id, String valor) {
        this.id = id;
        this.valor = valor;
        this.timestamp = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getSensor() {
        return sensor;
    }

    public void setSensor(long sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", valor='" + valor + '\'' +
                ", timestamp=" + timestamp +
                ", sensor=" + sensor +
                '}';
    }
}
