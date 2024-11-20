package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.EventoDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.util.EventoComparator;

import java.util.Collections;
import java.util.List;

@Path("sensor")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    @GET
    @Path("/")
    public List<SensorDTO> getAllSensors() {
        return SensorDTO.from(sensorBean.findAll());
    }

    @GET
    @Path("{id}")
    public SensorDTO getSensor(@PathParam("id") long id) {
        return SensorDTO.from(sensorBean.findWithEventos(id));
    }

    @GET
    @Path("{id}/min")
    public double getSensorMin(@PathParam("id") long id) {
        return sensorBean.getLowestValue(id);
    }

    @GET
    @Path("{id}/max")
    public double getSensorMax(@PathParam("id") long id) {
        return sensorBean.getHighestValue(id);
    }

    @GET
    @Path("{id}/avg")
    public double getSensorAvg(@PathParam("id") long id) {
        return sensorBean.getAverageValue(id);
    }

    @GET
    @Path("{id}/eventos")
    public List<EventoDTO> getSensorEventos(@PathParam("id") long id) {
        return EventoDTO.from(sensorBean.findWithEventos(id).getEventos());
    }

    @GET
    @Path("{id}/eventoRecente")
    public EventoDTO getSensorEventoRecente(@PathParam("id") long id) {
        List<Evento> eventos = sensorBean.findWithEventos(id).getEventos();
        eventos.sort(new EventoComparator());
        return EventoDTO.from(eventos.get(0));
    }
}
