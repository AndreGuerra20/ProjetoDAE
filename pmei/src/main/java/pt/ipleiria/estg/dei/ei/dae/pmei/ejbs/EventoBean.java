package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.pmei.ValoresLimite;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class EventoBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private SensorBean sensorBean;

    public Evento create(String valor, long sensorId) throws MyEntityNotFoundException, MyConstraintViolationException, IllegalArgumentException {
        var sensor = em.find(Sensor.class, sensorId);
        if (sensor == null) {
            throw new MyEntityNotFoundException("Sensor {" + sensorId + "} not found");
        }
        switch (sensor.getTipo()) {
            case "Temperatura" -> {
                if (Float.parseFloat(valor) < ValoresLimite.MINIMO_TEMPERATURA || Float.parseFloat(valor) > ValoresLimite.MAXIMO_TEMPERATURA) {
                    throw new IllegalArgumentException("Valor fora dos limites");
                }
            }
            case "Pressao" -> {
                if (Float.parseFloat(valor) < ValoresLimite.MINIMO_PRESSAO || Float.parseFloat(valor) > ValoresLimite.MAXIMO_PRESSAO) {
                    throw new IllegalArgumentException("Valor fora dos limites");
                }
            }
            case "Aceleracao" -> {
                if (Float.parseFloat(valor) < ValoresLimite.MINIMO_ACELERACAO || Float.parseFloat(valor) > ValoresLimite.MAXIMO_ACELERACAO) {
                    throw new IllegalArgumentException("Valor fora dos limites");
                }
            }
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

    public void update(Evento evento) {
        if(Float.parseFloat(evento.getValor()) < ValoresLimite.MINIMO_TEMPERATURA || Float.parseFloat(evento.getValor()) > ValoresLimite.MAXIMO_TEMPERATURA) {
            throw new IllegalArgumentException("Valor fora dos limites");
        }
        em.merge(evento);
    }
}
