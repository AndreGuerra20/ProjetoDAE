package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@NamedQueries(@NamedQuery(name = "getAllClientes", query = "SELECT c FROM Cliente c LEFT JOIN FETCH c.encomendas ORDER BY c.id"))
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private long NIF;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Encomenda> encomendas = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(String name, long NIF) {
        this.name = name;
        this.NIF = NIF;
    }

    public long getId() {
        return id;
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
        return "Cliente{" + "id=" + id + ", name='" + name + '\'' + ", NIF=" + NIF + '}';
    }
}
