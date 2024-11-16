package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventoDTO implements Serializable {
    private long id;
    private String valor;
    private Date timestamp;

    public EventoDTO() {
    }

    public EventoDTO(long id, String valor, Date timestamp) {
        this.id = id;
        this.valor = valor;
        this.timestamp = timestamp;
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

    public static EventoDTO from(Evento evento) {
        return new EventoDTO(
                evento.getId(),
                evento.getValor(),
                evento.getTimestamp()
        );
    }

    public static List<EventoDTO> from(List<Evento> eventos) {
        return eventos.stream().map(EventoDTO::from).collect(Collectors.toList());
    }
}
