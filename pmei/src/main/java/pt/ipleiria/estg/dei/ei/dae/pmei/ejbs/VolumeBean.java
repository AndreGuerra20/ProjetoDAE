package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.util.List;

@Stateless
public class VolumeBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String tipoEmbalagem, long encomenda_id) {
        var encomenda = em.find(Encomenda.class, encomenda_id);
        if(encomenda == null) {
            throw new IllegalArgumentException("Encomenda {" + encomenda_id + "} not found");
        }
        Volume volume = new Volume(tipoEmbalagem, encomenda);
        em.persist(volume);
    }

    public Volume find(long id) {
        return em.find(Volume.class, id);
    }

    public List<Volume> findAll() {
        return em.createNamedQuery("getAllVolumes", Volume.class).getResultList();
    }

    public Volume findWithProdutos(long id) {
        Volume volume = em.find(Volume.class, id);
        Hibernate.initialize(volume.getProdutos());
        return volume;
    }

    public Volume findWithSensores(long id) {
        Volume volume = em.find(Volume.class, id);
        Hibernate.initialize(volume.getSensores());
        return volume;
    }

    public Volume findWithBoth(long id) {
        Volume volume = em.find(Volume.class, id);
        Hibernate.initialize(volume.getProdutos());
        Hibernate.initialize(volume.getSensores());
        return volume;
    }
}
