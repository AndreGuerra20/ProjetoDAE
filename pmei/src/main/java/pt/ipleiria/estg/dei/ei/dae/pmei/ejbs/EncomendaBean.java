package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

@Stateless
public class EncomendaBean {
    @PersistenceContext
    private EntityManager em;

    public void create(Cliente cliente, String estado) {
        Encomenda encomenda = new Encomenda(cliente, estado);
        em.persist(encomenda);
    }

    public Encomenda find(long id) {
        return em.find(Encomenda.class, id);
    }
}
