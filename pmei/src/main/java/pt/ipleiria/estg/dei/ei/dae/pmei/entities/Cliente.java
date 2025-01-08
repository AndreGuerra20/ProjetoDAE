package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries(@NamedQuery(name = "getAllClientes", query = "SELECT u FROM User u ORDER BY u.username"))
public class Cliente extends User {
    private long NIF;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Encomenda> encomendas = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(String name, long NIF, String username, String password) {
        super(username, password, name);
        this.NIF = NIF;
    }

    public long getNIF() {
        return NIF;
    }

    public void setNIF(long NIF) {
        this.NIF = NIF;
    }

    public List<Encomenda> getEncomendas() {
        return encomendas;
    }

    public void setEncomendas(List<Encomenda> encomendas) {
        this.encomendas = encomendas;
    }

    public void addEncomenda(Encomenda encomenda) {
        encomendas.add(encomenda);
    }

    public void removeEncomenda(Encomenda encomenda) {
        encomendas.remove(encomenda);
    }

    @Override
    public String toString() {
        return "Cliente{" + "username=" + getUsername() + ", name='" + getNome() + '\'' + ", NIF=" + NIF + '}';
    }
}
