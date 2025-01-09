package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.util.EventoComparator;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SensorUltimoEventoDTO implements Serializable {
    private long id;
    private String tipo;
    private String ultimaleitura;
    private String timestamp;

    public SensorUltimoEventoDTO() {
    }

    public SensorUltimoEventoDTO(long id, String tipo, String ultimaleitura, String timestamp) {
        this.id = id;
        this.tipo = tipo;
        this.ultimaleitura = ultimaleitura;
        this.timestamp = timestamp;
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

    public String getLeitura() {
        return ultimaleitura;
    }

    public void setLeitura(String ultimaleitura) {
        this.ultimaleitura = ultimaleitura;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static SensorUltimoEventoDTO from(Sensor sensor) {
        Hibernate.initialize(sensor);
        List<Evento> eventos = sensor.getEventos();
        if (eventos.isEmpty()) {
            return null;
        }
        eventos.sort(new EventoComparator());
        Evento ultimoEvento = eventos.get(0);
        String ultimaleitura = ultimoEvento.getValor();
        String timestamp = ultimoEvento.getTimestamp();
        return new SensorUltimoEventoDTO(sensor.getId(), sensor.getTipo(), ultimaleitura, timestamp);
    }

    public static List<SensorUltimoEventoDTO> from(List<Sensor> sensores) {
        return sensores.stream().map(SensorUltimoEventoDTO::from).collect(Collectors.toList());
    }
}