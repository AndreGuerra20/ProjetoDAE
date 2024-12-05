package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.EventoDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.SensorEventoDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.util.EventoComparator;

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
        List<SensorDTO> listaSensores = SensorDTO.from(sensorBean.findAll());
        if(listaSensores.isEmpty()){
            return null;
        }
        return listaSensores;
    }

    @GET
    @Path("{id}")
    public Response getSensor(@PathParam("id") long id) {
        Sensor withEventos = sensorBean.findWithEventos(id);
        if (withEventos == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(SensorDTO.from(withEventos)).build();
    }

    @GET
    @Path("{id}/min")
    public Response getSensorMin(@PathParam("id") long id) {
        double lowestValue = sensorBean.getLowestValue(id);
        if (lowestValue == Double.MAX_VALUE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(lowestValue).build();
    }

    @GET
    @Path("{id}/max")
    public Response getSensorMax(@PathParam("id") long id) {
        double highestValue = sensorBean.getHighestValue(id);
        if (highestValue == Double.MIN_VALUE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(highestValue).build();
    }

    @GET
    @Path("{id}/avg")
    public Response getSensorAvg(@PathParam("id") long id) {
        double averageValue = sensorBean.getAverageValue(id);
        if (averageValue == Double.MIN_VALUE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(averageValue).build();
    }

    @GET
    @Path("{id}/eventos")
    public List<EventoDTO> getSensorEventos(@PathParam("id") long id) {
        List<EventoDTO> listaEventos = EventoDTO.from(sensorBean.findWithEventos(id).getEventos());
        if(listaEventos.isEmpty()){
            return null;
        }
        return listaEventos;
    }

    @GET
    @Path("{id}/eventoRecente")
    public Response getSensorEventoRecente(@PathParam("id") long id) {
        List<Evento> eventos = sensorBean.findWithEventos(id).getEventos();
        if (eventos.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        eventos.sort(new EventoComparator());
        return Response.ok(EventoDTO.from(eventos.get(0))).build();
    }

    //EP 04
    //TODO: Falta devolver o sensor
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postSensorEvento(EventoDTO eventoDTO){
        System.out.println("id: " + eventoDTO.getSensorId() + " valor: " + eventoDTO.getValor());
        Sensor sensor = sensorBean.findWithEventos(eventoDTO.getSensorId());
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sensorBean.createEvento(sensor, Double.parseDouble(eventoDTO.getValor()));
        return Response.ok(SensorDTO.from(sensor)).build();
    }
}
