package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EventoBean {
    @PersistenceContext
    private EntityManager em;

    public Evento create(String valor, long sensorId) throws MyEntityNotFoundException, MyConstraintViolationException {
        var sensor = em.find(Sensor.class, sensorId);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor {" + sensorId + "} not found");
        }
        try {
            Evento evento = new Evento(valor, sensor);
            em.persist(evento);
            em.flush();
            return evento;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Evento find(long id) {
        return em.find(Evento.class, id);
    }

    public List<Evento> findAll() {
        return em.createNamedQuery("getAllEventos", Evento.class).getResultList();
    }
}
