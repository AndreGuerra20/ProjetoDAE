package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;

@Stateless
public class ClienteBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long id, String name, long NIF) {
        em.persist(new Cliente(id, name, NIF));
    }

    public Cliente find(long id) {
        return em.find(Cliente.class, id);
    }
}
