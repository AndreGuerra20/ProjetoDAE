package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

@Entity
@NamedQueries(@NamedQuery(name = "getAllLogisticas", query = "SELECT lg FROM Logistica lg ORDER BY lg.username"))
public class Logistica extends User {
    private String codFuncionario;

    public Logistica() {
    }

    public Logistica(String username, String password,String nome, String codFuncionario, String email) {
        super(username, password, nome, email);
        this.codFuncionario = codFuncionario;
    }

    public String getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(String codFuncionario) {
        this.codFuncionario = codFuncionario;
    }
}
