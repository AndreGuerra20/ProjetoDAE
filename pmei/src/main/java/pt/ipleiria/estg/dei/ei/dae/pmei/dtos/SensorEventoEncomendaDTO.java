package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SensorEventoEncomendaDTO implements Serializable {
    private long sensorId;
    private long idEncomenda;
    private long idVolume;
    private String valor;
    private String timestamp;

    public SensorEventoEncomendaDTO() {
    }

    public SensorEventoEncomendaDTO(long id, long idEncomenda, long idVolume, String valor, String timestamp) {
        this.sensorId = id;
        this.idEncomenda = id;
        this.idVolume = idVolume;
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

    public long getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(long idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    public long getIdVolume() {
        return idVolume;
    }

    public void setIdVolume(long idVolume) {
        this.idVolume = idVolume;
    }

    public static SensorEventoEncomendaDTO from(Evento evento) {
        Hibernate.initialize(evento);
        Sensor sensor = evento.getSensor();
        Volume volume = sensor.getVolume();
        Encomenda encomenda = volume.getEncomenda();

        long idVolume = volume.getIdVolume();
        long idEncomenda = encomenda.getId();

        return new SensorEventoEncomendaDTO(sensor.getId(), idEncomenda, idVolume, evento.getValor(), evento.getTimestamp());
    }

    public static List<SensorEventoEncomendaDTO> from(List<Evento> eventos) {
        return eventos.stream().map(SensorEventoEncomendaDTO::from).collect(Collectors.toList());
    }
}
