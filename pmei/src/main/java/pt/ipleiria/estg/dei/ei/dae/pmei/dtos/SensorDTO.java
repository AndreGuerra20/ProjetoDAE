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
    private VolumeDTO volume;
    private List<EventoDTO> eventos;

    public SensorDTO() {
    }

    public SensorDTO(long id, String tipo, Boolean status, VolumeDTO volume, List<EventoDTO> eventos) {
        this.tipo = tipo;
        this.status = status;
        this.volume = volume;
        this.eventos = eventos;
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

    public VolumeDTO getVolume() {
        return volume;
    }

    public void setVolume(VolumeDTO volume) {
        this.volume = volume;
    }

    public List<EventoDTO> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoDTO> eventos) {
        this.eventos = eventos;
    }

    public static SensorDTO from(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                sensor.getTipo(),
                sensor.getStatus(),
                VolumeDTO.from(sensor.getVolume()),
                EventoDTO.from(sensor.getEventos())
        );
    }

    public static List<SensorDTO> from(List<Sensor> sensores) {
        return sensores.stream().map(SensorDTO::from).collect(Collectors.toList());
    }
}
