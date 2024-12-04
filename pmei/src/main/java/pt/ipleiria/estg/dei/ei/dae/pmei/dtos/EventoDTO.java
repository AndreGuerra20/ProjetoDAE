package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import jakarta.persistence.Id;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventoDTO implements Serializable {
    @Id
    private long id;
    private String valor;
    private String timestamp;

    public EventoDTO() {
    }

    public EventoDTO(String valor, String timestamp) {
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static EventoDTO from(Evento evento) {
        return new EventoDTO(
                evento.getValor(),
                evento.getTimestamp()
        );
    }

    public static List<EventoDTO> from(List<Evento> eventos) {
        return eventos.stream().map(EventoDTO::from).collect(Collectors.toList());
    }
}
