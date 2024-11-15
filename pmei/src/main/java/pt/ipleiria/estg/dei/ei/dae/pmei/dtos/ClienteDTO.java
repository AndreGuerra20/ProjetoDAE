package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDTO implements Serializable {
    private long id;
    private String name;
    private long NIF;

    public ClienteDTO() {
    }

    public ClienteDTO(long id, String name, long NIF) {
        this.id = id;
        this.name = name;
        this.NIF = NIF;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public static ClienteDTO from(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getName(),
                cliente.getNIF()
        );
    }

    public static List<ClienteDTO> from(List<Cliente> clientes) {
        return clientes.stream().map(ClienteDTO::from).collect(Collectors.toList());
    }
}
