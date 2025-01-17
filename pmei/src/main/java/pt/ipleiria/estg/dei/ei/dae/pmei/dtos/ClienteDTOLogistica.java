package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDTOLogistica implements Serializable {
    private String username;
    private String email;
    private long id;

    public ClienteDTOLogistica() {
    }

    public ClienteDTOLogistica(String username, String email, long id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static ClienteDTOLogistica from(Cliente cliente) {
        return new ClienteDTOLogistica(cliente.getUsername(), cliente.getEmail(), cliente.getId());
    }

    public static List<ClienteDTOLogistica> from(List<Cliente> clientes) {
        return clientes.stream().map(ClienteDTOLogistica::from).collect(Collectors.toList());
    }
}
