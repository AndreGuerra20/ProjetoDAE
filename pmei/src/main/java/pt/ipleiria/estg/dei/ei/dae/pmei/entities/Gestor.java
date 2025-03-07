package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

@Entity
@NamedQueries(@NamedQuery(name = "getAllGestores", query = "SELECT g FROM Gestor g ORDER BY g.username"))
public class Gestor extends User {
    private String codFuncionario;

    public Gestor() {
    }

    public Gestor(String username, String password,String nome, String codFuncionario, String email) {
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
