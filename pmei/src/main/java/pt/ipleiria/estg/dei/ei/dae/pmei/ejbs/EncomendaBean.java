package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.util.List;

@Stateless
public class EncomendaBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long cliente_id, String estado) {
        var cliente = em.find(Cliente.class, cliente_id);
        if(cliente == null) {
            throw new IllegalArgumentException("Cliente {" + cliente_id + "} not found");
        }
        Encomenda encomenda = new Encomenda(cliente, estado);
        em.persist(encomenda);
    }

    public Encomenda find(long id) {
        return em.find(Encomenda.class, id);
    }

    public List<Encomenda> findAll() {
        return em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();
    }

    public Encomenda findWithVolumes(long id) {
        Encomenda encomenda = em.find(Encomenda.class, id);
        Hibernate.initialize(encomenda.getVolumes());
        return encomenda;
    }
}
