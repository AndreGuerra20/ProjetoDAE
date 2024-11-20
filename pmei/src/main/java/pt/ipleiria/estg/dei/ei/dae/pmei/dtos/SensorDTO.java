package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import jakarta.persistence.Id;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SensorDTO implements Serializable {
    @Id
    private long id;
    private String tipo;
    private Boolean status;
    private List<Long> lista_eventos = new ArrayList<>();

    public SensorDTO() {
    }

    public SensorDTO(String tipo, Boolean status, List<Long> lista_eventos) {
        this.tipo = tipo;
        this.status = status;
        this.lista_eventos = lista_eventos;
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

    public List<Long> getLista_eventos() {
        return lista_eventos;
    }

    public void setLista_eventos(List<Long> lista_eventos) {
        this.lista_eventos = lista_eventos;
    }

    public static SensorDTO from(Sensor sensor) {
        return new SensorDTO(
                sensor.getTipo(),
                sensor.getStatus(),
                sensor.getEventos().stream().map(Evento::getId).collect(Collectors.toList())
        );
    }

    public static List<SensorDTO> from(List<Sensor> sensores) {
        return sensores.stream().map(SensorDTO::from).collect(Collectors.toList());
    }
}
