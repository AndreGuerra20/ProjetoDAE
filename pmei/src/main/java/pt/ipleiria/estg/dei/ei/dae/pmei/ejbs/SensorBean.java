package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
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

    @Transactional
    public List<Sensor> findAll() {
        List<Sensor> sensores = em.createNamedQuery("getAllSensores", Sensor.class).getResultList();
        for (Sensor sensor : sensores) {
            Hibernate.initialize(sensor.getEventos());
        }
        return sensores;
    }

    @Transactional
    public Sensor findWithEventos(long id) {
        Sensor sensor = em.find(Sensor.class, id);
        Hibernate.initialize(sensor.getEventos());
        return sensor;
    }

    public double getAverageValue(long id) {
        Sensor sensor = em.find(Sensor.class, id);
        return sensor.getEventos().stream().mapToDouble(e -> Double.parseDouble(e.getValor())).average().orElse(0);
    }

    public double getLowestValue(long id) {
        Sensor sensor = em.find(Sensor.class, id);
        return sensor.getEventos().stream().mapToDouble(e -> Double.parseDouble(e.getValor())).min().orElse(0);
    }

    public double getHighestValue(long id) {
        Sensor sensor = em.find(Sensor.class, id);
        return sensor.getEventos().stream().mapToDouble(e -> Double.parseDouble(e.getValor())).max().orElse(0);
    }

    public void createEvento(Sensor sensor, double valor) {
        Evento evento = new Evento(Double.toString(valor), sensor);
        sensor.addEvento(evento);
        em.merge(sensor);
        em.merge(evento);
    }
}
