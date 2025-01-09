package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.EventoBean;
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

    @EJB
    private EventoBean eventoBean;

    /**
     * EP 02 - Um gestor visualiza todos os sensores
     *
     * @return Lista dos sensores na base de dados
     */
    @GET
    @Path("/")
    @RolesAllowed({"Gestor"})
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

    /**
     * EP 17 - Um gestor pretende visualizar a leitura mais baixa de um sensor
     *
     * @param id ID do sensor
     * @return Valor da leitura
     */
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

    /**
     * EP 16 - Um gestor pretende visualizar a leitura mais elevada de um sensor
     *
     * @param id ID do sensor
     * @return Valor da leitura
     */
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

    /**
     * EP 18 - Um gestor pretende visualizar a média das leituras de um sensor
     *
     * @param id ID do sensor
     * @return Valor da média
     */
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

    /**
     * EP 20 - Um gestor pretende visualizar todas as leituras de um sensor
     *
     * @param id ID do sensor
     * @return Lista de leituras do sensor
     */
    @GET
    @Path("{id}/eventos")
    public List<SensorEventoDTO> getSensorEventos(@PathParam("id") long id) {
        List<SensorEventoDTO> listaEventos = SensorEventoDTO.from(sensorBean.findWithEventos(id).getEventos());
        if(listaEventos.isEmpty()){
            return null;
        }
        return listaEventos;
    }

    /**
     * EP 03 - Um gestor vizualiza a última leitura de um sensor
     *
     * @param id ID do sensor
     * @return Dados da última leitura
     */
    @RolesAllowed({"Gestor"})
    @GET
    @Path("{id}/eventoRecente")
    public Response getSensorEventoRecente(@PathParam("id") long id) {
        Sensor sensor = sensorBean.findWithEventos(id);
        if(sensor == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Evento> eventos = sensor.getEventos();
        if (eventos.isEmpty()) {
            // return Response containing the message "No events registered"
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        eventos.sort(new EventoComparator());
        return Response.ok(EventoDTO.from(eventos.get(0))).build();
    }

    //TODO: Falta devolver o sensor
    /**
     * EP 04 - Criar uma leitura nova de um sensor
     *
     * @return Dados do sensor, com a leitura criada
     */
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
