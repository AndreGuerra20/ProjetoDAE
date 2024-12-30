package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;

import java.util.List;

@Stateless
public class EventoBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String valor, long sensorId) {
        var sensor = em.find(Sensor.class, sensorId);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor {" + sensorId + "} not found");
        }
        Evento evento = new Evento(valor, sensor);
        em.persist(evento);
    }

    public Evento find(long id) {
        return em.find(Evento.class, id);
    }

    public List<Evento> findAll() {
        return em.createNamedQuery("getAllEventos", Evento.class).getResultList();
    }
}
