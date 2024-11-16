package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EncomendaDTO implements Serializable {
    private long id;
    private ClienteDTO cliente;
    private String estado;
    private List<VolumeDTO> volumes = new ArrayList<>();

    public EncomendaDTO() {
    }

    public EncomendaDTO(long id, String estado, ClienteDTO cliente) {
        this.id = id;
        this.estado = estado;
        this.cliente = cliente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<VolumeDTO> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumeDTO> volumes) {
        this.volumes = volumes;
    }

    public static EncomendaDTO from(Encomenda encomenda) {
        EncomendaDTO encomendaDTO = new EncomendaDTO(encomenda.getId(), encomenda.getEstado(), ClienteDTO.from(encomenda.getCliente()));
        encomendaDTO.setVolumes(VolumeDTO.from(encomenda.getVolumes()));
        return encomendaDTO;
    }

    public static List<EncomendaDTO> from(List<Encomenda> encomendas) {
        return encomendas.stream().map(EncomendaDTO::from).collect(Collectors.toList());
    }

}
