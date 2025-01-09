package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Gestor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class GestorAuthDTO implements Serializable {
    private String username;
    private String password;
    private String nome;
    private String codFuncionario;

    public GestorAuthDTO() {}

    public GestorAuthDTO(String username, String password, String nome, String codFuncionario) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.codFuncionario = codFuncionario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(String codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    public static GestorAuthDTO from(Gestor gestor) {
        return new GestorAuthDTO(gestor.getUsername(),gestor.getPassword(),gestor.getNome(),gestor.getCodFuncionario());
    }

    public static List<GestorAuthDTO> from(List<Gestor> gestors) {
        return gestors.stream().map(GestorAuthDTO::from).collect(Collectors.toList());
    }
}
