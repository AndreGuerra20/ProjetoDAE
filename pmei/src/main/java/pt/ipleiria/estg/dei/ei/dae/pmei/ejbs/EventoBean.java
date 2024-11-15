package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;

@Stateless
public class EventoBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String valor, Sensor sensor) {
        Evento evento = new Evento(valor);
        evento.setSensor(sensor);
        em.persist(evento);
    }

    public Evento find(long id) {
        return em.find(Evento.class, id);
    }
}
