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

    public void create(Cliente cliente, String estado) {
        Encomenda encomenda = new Encomenda(cliente, estado);
        em.persist(encomenda);
    }

    public Encomenda find(long id) {
        var encomenda = em.find(Encomenda.class, id);
        Hibernate.initialize(encomenda.getVolumes());
        return encomenda;
    }

    public List<Encomenda> findAll() {
        return em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();
    }
}
