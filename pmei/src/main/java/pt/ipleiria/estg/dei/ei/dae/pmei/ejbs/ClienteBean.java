package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.client.Client;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.User;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Hasher;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class ClienteBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public void create(String name, long NIF, String username, String password) throws MyEntityExistsException, MyConstraintViolationException {
        var cliente = find(username);
        if (cliente != null) {
            throw new MyEntityExistsException("Cliente already exists: " + username);
        }
        try {
            cliente = new Cliente(name, NIF, username, hasher.hash(password));
            em.persist(cliente);
            em.flush();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Cliente find(String username) {
        Cliente cliente = null;
        List<User> clientes = em.createNamedQuery("getAllUsers", User.class).getResultList();
        for (User c : clientes) {
            if (c.getUsername().equals(username)) {
                cliente = (Cliente)c;
            }
        }
        if (cliente == null) {
            return null;
        }
        Hibernate.initialize(cliente.getEncomendas());
        return cliente;
    }

    public Cliente find(long id) {
        List<Cliente> clientes = findAll();
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public List<Cliente> findAll() {
        List<User> clientes = em.createNamedQuery("getAllUsers", User.class).getResultList();
        List<Cliente> clientesList = new ArrayList<>();
        for (User cliente : clientes) {
            if(cliente instanceof Cliente){
                Hibernate.initialize(((Cliente) cliente).getEncomendas());
                clientesList.add((Cliente) cliente);
            }
        }
        return clientesList;
    }
}
