package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDTO implements Serializable {
    private long id;
    private String username;
    private String name;
    private long NIF;
    private String email;
    public ClienteDTO() {
    }

    public ClienteDTO(long id,String username, String name, long NIF, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.NIF = NIF;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static ClienteDTO from(Cliente cliente) {
        return new ClienteDTO(cliente.getId(), cliente.getUsername(), cliente.getNome(), cliente.getNIF(), cliente.getEmail());
    }

    public static List<ClienteDTO> from(List<Cliente> clientes) {
        return clientes.stream().map(ClienteDTO::from).collect(Collectors.toList());
    }
}
