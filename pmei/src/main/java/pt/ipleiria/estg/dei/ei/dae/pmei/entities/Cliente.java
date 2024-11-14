package pt.ipleiria.estg.dei.ei.dae.pmei.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Cliente {
    @Id
    private long id;

    private String name;

    private long NIF;

    public Cliente() {
    }

    public Cliente(long id, String name, long NIF) {
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

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", NIF=" + NIF +
                '}';
    }
}
