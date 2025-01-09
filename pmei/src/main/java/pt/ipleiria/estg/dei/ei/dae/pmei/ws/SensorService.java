package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.pmei.util.EventoComparator;

import java.util.List;

@Path("sensor")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
@RolesAllowed({"Logistica","Gestor"})
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    @GET
    @Path("/")
    public List<SensorSemEventosDTO> getAllSensors() {
        List<SensorSemEventosDTO> listaSensores = SensorSemEventosDTO.from(sensorBean.findAll());
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

    @RolesAllowed({"Gestor"})
    @GET
    @Path("{id}/min")
    public Response getSensorMin(@PathParam("id") long id) {
        double lowestValue = sensorBean.getLowestValue(id);
        if (lowestValue == Double.MAX_VALUE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(lowestValue).build();
    }

    @RolesAllowed({"Gestor"})
    @GET
    @Path("{id}/max")
    public Response getSensorMax(@PathParam("id") long id) {
        double highestValue = sensorBean.getHighestValue(id);
        if (highestValue == Double.MIN_VALUE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(highestValue).build();
    }

    @RolesAllowed({"Gestor"})
    @GET
    @Path("{id}/avg")
    public Response getSensorAvg(@PathParam("id") long id) {
        double averageValue = sensorBean.getAverageValue(id);
        if (averageValue == Double.MIN_VALUE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(averageValue).build();
    }

    //EP 20
    @GET
    @Path("{id}/eventos")
    public List<SensorEventoDTO> getSensorEventos(@PathParam("id") long id) {
        List<SensorEventoDTO> listaEventos = SensorEventoDTO.from(sensorBean.findWithEventos(id).getEventos());
        if(listaEventos.isEmpty()){
            return null;
        }
        return listaEventos;
    }

    @RolesAllowed({"Gestor"})
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
    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postSensorEvento(@PathParam("id") long SensorId, EventoDTOValor eventoDTO) {
        Sensor sensor = sensorBean.findWithEventos(SensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sensorBean.createEvento(sensor, eventoDTO.getValor());
        return Response.ok(SensorDTO.from(sensor)).build();
    }
}
