package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String tipo, boolean estado, Volume volume) {
        Sensor sensor = new Sensor(tipo, estado);
        sensor.setVolume(volume);
        em.persist(sensor);
    }

    public Sensor find(long id) {
        return em.find(Sensor.class, id);
    }
}
