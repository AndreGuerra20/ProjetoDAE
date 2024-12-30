package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import jakarta.persistence.Id;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDTO implements Serializable {
    @Id
    private String username;
    private String name;
    private long NIF;
    private List<EncomendaDTO> encomendas = new ArrayList<>();

    public ClienteDTO() {
    }

    public ClienteDTO(String username, String name, long NIF, List<EncomendaDTO> encomendas) {
        this.username = username;
        this.name = name;
        this.NIF = NIF;
        this.encomendas = encomendas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNIF() {
        return NIF;
    }

    public void setNIF(long NIF) {
        this.NIF = NIF;
    }

    public List<EncomendaDTO> getEncomendas() {
        return encomendas;
    }

    public void setEncomendas(List<EncomendaDTO> encomendas) {
        this.encomendas = encomendas;
    }

    public static ClienteDTO from(Cliente cliente) {
        return new ClienteDTO(cliente.getUsername(), cliente.getName(), cliente.getNIF(), cliente.getEncomendas().stream().map(EncomendaDTO::from).collect(Collectors.toList()));
    }

    public static List<ClienteDTO> from(List<Cliente> clientes) {
        return clientes.stream().map(ClienteDTO::from).collect(Collectors.toList());
    }
}
