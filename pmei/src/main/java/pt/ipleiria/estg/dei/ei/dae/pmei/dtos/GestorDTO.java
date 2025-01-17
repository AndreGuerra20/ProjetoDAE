package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import jakarta.persistence.Id;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Gestor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Logistica;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class GestorDTO implements Serializable {
    private String username;
    private String codFuncionario;

    public GestorDTO() {

    }

    public GestorDTO(String username, String codFuncionario) {
        this.username = username;
        this.codFuncionario = codFuncionario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(String codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    public static GestorDTO from(Gestor gestor) {
        return new GestorDTO(gestor.getUsername(), gestor.getCodFuncionario());
    }

    public static List<GestorDTO> from(List<Gestor> gestors) {
        return gestors.stream().map(GestorDTO::from).collect(Collectors.toList());
    }
}
