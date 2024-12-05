package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class EventoDTO implements Serializable {
    private long sensorId;
    private String valor;
    private String timestamp;

    public EventoDTO() {
    }

    public EventoDTO(long id, String valor, String timestamp) {
        this.sensorId = id;
        this.valor = valor;
        this.timestamp = timestamp;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
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

    public static EventoDTO from(Evento evento) {
        return new EventoDTO(
                evento.getSensor().getId(),
                evento.getValor(),
                evento.getTimestamp()
        );
    }

    public static List<EventoDTO> from(List<Evento> eventos) {
        return eventos.stream().map(EventoDTO::from).collect(Collectors.toList());
    }
}
