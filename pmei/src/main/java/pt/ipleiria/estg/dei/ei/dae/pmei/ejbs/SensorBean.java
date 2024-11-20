package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.util.List;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String tipo, boolean estado, long volume_id) {
        var volume = em.find(Volume.class, volume_id);
        if(volume == null) {
            throw new IllegalArgumentException("Volume {" + volume_id + "} not found");
        }
        Sensor sensor = new Sensor(tipo, estado, volume);
        em.persist(sensor);
    }

    public Sensor find(long id) {
        return em.find(Sensor.class, id);
    }

    public List<Sensor> findAll() {
        return em.createNamedQuery("getAllSensores", Sensor.class).getResultList();
    }

    public Sensor findWithEventos(long id) {
        Sensor sensor = em.find(Sensor.class, id);
        Hibernate.initialize(sensor.getEventos());
        return sensor;
    }
}
