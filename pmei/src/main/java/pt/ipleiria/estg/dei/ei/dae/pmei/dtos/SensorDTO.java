package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SensorDTO implements Serializable {
    private long id;
    private String tipo;
    private Boolean status;
    private List<EventoDTO> eventos = new ArrayList<>();

    public SensorDTO() {
    }

    public SensorDTO(long id, String tipo, Boolean status) {
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

    public List<EventoDTO> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoDTO> eventos) {
        this.eventos = eventos;
    }

    public static SensorDTO from(Sensor sensor) {
        SensorDTO sensorDTO = new SensorDTO(sensor.getId(), sensor.getTipo(), sensor.getStatus());
        sensorDTO.setEventos(EventoDTO.from(sensor.getEventos()));
        return sensorDTO;
    }

    public static List<SensorDTO> from(List<Sensor> sensores) {
        return sensores.stream().map(SensorDTO::from).collect(Collectors.toList());
    }
}
