package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Logistica;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class LogisticaAuthDTO implements Serializable {
    private String username;
    private String password;
    private String nome;
    private String codFuncionario;
    private String email;

    public LogisticaAuthDTO() {

    }

    public LogisticaAuthDTO(String username, String password, String nome, String codFuncionario, String email) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.codFuncionario = codFuncionario;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNome() {
        return nome;
    }

    public String getCodFuncionario() {
        return codFuncionario;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodFuncionario(String codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static LogisticaAuthDTO from(Logistica logistica) {
        return new LogisticaAuthDTO(logistica.getUsername(), logistica.getPassword(), logistica.getNome(), logistica.getCodFuncionario(), logistica.getEmail());
    }

    public static List<LogisticaAuthDTO> from(List<Logistica> logisticas) {
        return logisticas.stream().map(LogisticaAuthDTO::from).collect(Collectors.toList());
    }
}
