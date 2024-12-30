package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EncomendaDTO implements Serializable {
    private long encomendaId;
    private long customerId;
    private String estado;
    private List<VolumeDTO> volumes = new ArrayList<>();

    public EncomendaDTO() {
    }

    public EncomendaDTO(long id, String estado, long clienteId, List<VolumeDTO> volumes) {
        this.encomendaId = id;
        this.estado = estado;
        this.customerId = clienteId;
        this.volumes = volumes;
    }

    public long getEncomendaId() {
        return encomendaId;
    }

    public void setEncomendaId(long encomendaId) {
        this.encomendaId = encomendaId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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
        return new EncomendaDTO(
                encomenda.getId(),
                encomenda.getEstado(),
                encomenda.getCliente().getId(),
                encomenda.getVolumes().stream().map(VolumeDTO::from).collect(Collectors.toList())
        );
    }

    public static List<EncomendaDTO> from(List<Encomenda> encomendas) {
        return encomendas.stream().map(EncomendaDTO::from).collect(Collectors.toList());
    }

}
