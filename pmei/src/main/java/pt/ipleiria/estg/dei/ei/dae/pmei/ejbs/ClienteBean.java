package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;

import java.util.List;

@Stateless
public class ClienteBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String name, long NIF) {
        em.persist(new Cliente(name, NIF));
    }

    public Cliente find(long id) {
        Cliente cliente = em.find(Cliente.class, id);
        Hibernate.initialize(cliente.getEncomendas());
        return cliente;
    }

    public List<Cliente> findAll() {
        return em.createNamedQuery("getAllClientes", Cliente.class).getResultList();
    }
}
