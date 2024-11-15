package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

@Stateless
public class VolumeBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String tipoEmbalagem, Encomenda encomenda) {
        Volume volume = new Volume(tipoEmbalagem);
        volume.setEncomenda(encomenda);
        em.persist(volume);
    }

    public Volume find(long id) {
        return em.find(Volume.class, id);
    }
}
