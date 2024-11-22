package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.User;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Hasher;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;

import java.util.List;

@Stateless
public class ClienteBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public void create(String name, long NIF, String username, String password) {
        var cliente = find(username);
        if (cliente != null) {
            throw new IllegalArgumentException("Cliente already exists: " + username);
        }
        cliente = new Cliente(name, NIF, username, hasher.hash(password));
        em.persist(cliente);
    }

    public Cliente find(String username) {
        Cliente cliente = em.find(Cliente.class, username);
        if(cliente == null) {
            return null;
        }
        Hibernate.initialize(cliente.getEncomendas());
        return cliente;
    }

    public List<Cliente> findAll() {
        return em.createNamedQuery("getAllClientes", Cliente.class).getResultList();
    }
}
