package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long id, String tipo, boolean estado) {
        Sensor sensor = new Sensor(id, tipo, estado);
        em.persist(sensor);
    }

    public Sensor find(long id) {
        return em.find(Sensor.class, id);
    }
}
