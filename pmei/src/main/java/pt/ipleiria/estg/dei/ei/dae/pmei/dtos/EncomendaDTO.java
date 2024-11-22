package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import jakarta.persistence.Id;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EncomendaDTO implements Serializable {
    @Id
    private long id;
    private String clienteUsername;
    private String estado;
    private List<Long> lista_volumes = new ArrayList<>();

    public EncomendaDTO() {
    }

    public EncomendaDTO(long id, String estado, String clienteUsername, List<Long> lista_volumes) {
        this.id = id;
        this.estado = estado;
        this.clienteUsername = clienteUsername;
        this.lista_volumes = lista_volumes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClienteUsername() {
        return clienteUsername;
    }

    public void setClienteUsername(String clienteUsername) {
        this.clienteUsername = clienteUsername;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Long> getLista_volumes() {
        return lista_volumes;
    }

    public void setLista_volumes(List<Long> lista_volumes) {
        this.lista_volumes = lista_volumes;
    }

    public static EncomendaDTO from(Encomenda encomenda) {
        return new EncomendaDTO(
                encomenda.getId(),
                encomenda.getEstado(),
                encomenda.getCliente().getUsername(),
                encomenda.getVolumes().stream().map(Volume::getId).collect(Collectors.toList())
        );
    }

    public static List<EncomendaDTO> from(List<Encomenda> encomendas) {
        return encomendas.stream().map(EncomendaDTO::from).collect(Collectors.toList());
    }

}
