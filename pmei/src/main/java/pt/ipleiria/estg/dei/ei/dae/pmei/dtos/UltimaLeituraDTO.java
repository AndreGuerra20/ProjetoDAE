package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.pmei.util.EventoComparator;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class UltimaLeituraDTO implements Serializable {
    private long idSensor;
    private long idEncomenda;
    private long idVolume;
    private String ultimaLeitura;
    private String timestamp;

    public UltimaLeituraDTO() {
    }

    public UltimaLeituraDTO(long idSensor, long idEncomenda, long idVolume, String ultimaLeitura, String timestamp) {
        this.idSensor = idSensor;
        this.idEncomenda = idEncomenda;
        this.idVolume = idVolume;
        this.ultimaLeitura = ultimaLeitura;
        this.timestamp = timestamp;
    }

    public long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(long idSensor) {
        this.idSensor = idSensor;
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

    public String getUltimaLeitura() {
        return ultimaLeitura;
    }

    public void setUltimaLeitura(String ultimaLeitura) {
        this.ultimaLeitura = ultimaLeitura;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static UltimaLeituraDTO from(Sensor sensor) {
        return new UltimaLeituraDTO(sensor.getId(), sensor.getVolume().getEncomenda().getId(), sensor.getVolume().getIdVolume(), sensor.getEventos().stream().max(new EventoComparator()).map(Evento::getValor).orElse(null), sensor.getEventos().stream().max(new EventoComparator()).map(Evento::getTimestamp).orElse(null));
    }

    public static List<UltimaLeituraDTO> from(List<Sensor> sensores) {
        return sensores.stream().map(UltimaLeituraDTO::from).collect(Collectors.toList());
    }
}
