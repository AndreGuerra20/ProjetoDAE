package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import jakarta.persistence.Id;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Logistica;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class LogisticaDTO implements Serializable {
    @Id
    private String username;
    private String codFuncionario;

    public LogisticaDTO() {

    }

    public LogisticaDTO(String username, String codFuncionario) {
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

    public static LogisticaDTO from(Logistica logistica) {
        return new LogisticaDTO(logistica.getUsername(), logistica.getCodFuncionario());
    }

    public static List<LogisticaDTO> from(List<Logistica> logisticas) {
        return logisticas.stream().map(LogisticaDTO::from).collect(Collectors.toList());
    }
}
