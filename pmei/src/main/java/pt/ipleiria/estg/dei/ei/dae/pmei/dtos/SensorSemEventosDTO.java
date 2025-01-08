package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SensorSemEventosDTO implements Serializable {
    private long id;
    private String tipo;
    private Boolean status;

    public SensorSemEventosDTO() {
    }

    public SensorSemEventosDTO(long id, String tipo, Boolean status) {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static SensorSemEventosDTO from(Sensor sensor) {
        return new SensorSemEventosDTO(sensor.getId(), sensor.getTipo(), sensor.getStatus());
    }

    public static List<SensorSemEventosDTO> from(List<Sensor> sensores) {
        return sensores.stream().map(SensorSemEventosDTO::from).collect(Collectors.toList());
    }
}