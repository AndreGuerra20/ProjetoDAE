package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SensorEventoDTO implements Serializable {
    private String valor;
    private String timestamp;

    public SensorEventoDTO() {
    }

    public SensorEventoDTO(String valor, String timestamp) {
        this.valor = valor;
        this.timestamp = timestamp;
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

    public static SensorEventoDTO from(Evento evento) {
        return new SensorEventoDTO(evento.getValor(), evento.getTimestamp());
    }

    public static List<SensorEventoDTO> from(List<Evento> eventos) {
        return eventos.stream().map(SensorEventoDTO::from).collect(Collectors.toList());
    }
}
