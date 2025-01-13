package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.EventoBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.SensorBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.pmei.util.EventoComparator;

import java.util.ArrayList;
import java.util.List;

@Path("sensores")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    @EJB
    private EventoBean eventoBean;

    @Context
    private SecurityContext securityContext;

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
        if(securityContext.isUserInRole("Cliente")){
            if (!withEventos.getVolume().getEncomenda().getCliente().getUsername().equals(securityContext.getUserPrincipal().getName())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
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
    @RolesAllowed({"Gestor","Cliente"})
    @GET
    @Path("{id}/eventos")
    public List<SensorEventoDTO> getSensorEventos(@PathParam("id") long id) {
        Sensor sensor = sensorBean.findWithEventos(id);
        if (sensor == null) {
            return null;
        }
        if(securityContext.isUserInRole("Cliente")){
            if (!sensor.getVolume().getEncomenda().getCliente().getUsername().equals(securityContext.getUserPrincipal().getName())) {
                return null;
            }
        }
        List<SensorEventoDTO> listaEventos = SensorEventoDTO.from(sensor.getEventos());
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
    @RolesAllowed({"Gestor","Cliente"})
    @GET
    @Path("{id}/eventoRecente")
    public Response getSensorEventoRecente(@PathParam("id") long id) {
        Sensor sensor = sensorBean.findWithEventos(id);
        if(sensor == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(securityContext.isUserInRole("Cliente")){
            if (!sensor.getVolume().getEncomenda().getCliente().getUsername().equals(securityContext.getUserPrincipal().getName())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        List<Evento> eventos = sensor.getEventos();
        if (eventos.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        eventos.sort(new EventoComparator());
        return Response.ok(EventoDTO.from(eventos.get(0))).build();
    }

    /**
     * EP 05 - O gestor pede informação da última leitura registada por todos os sensores do tipo escolhido ativos
     *
     * @param tiposensor Tipo de sensor
     * @return Dados dos últimos eventos para os sensores do tipo escolhido
     */
    @RolesAllowed({"Gestor"})
    @GET
    @Path("tipo/{tiposensor}")
    public List<SensorEventoEncomendaDTO> getUltimosSensoresAtivos(@PathParam("tiposensor") String tiposensor) {
        List<Sensor> sensores = sensorBean.findWithTipo(tiposensor);
        List<Evento> eventos = new ArrayList<>();
        if (sensores.isEmpty()) {
            return null;
        }
        for (Sensor sensor : sensores) {
            List<Evento> eventosBySensor = sensor.getEventos();
            eventosBySensor.sort(new EventoComparator());
            Evento ultimoEvento = eventosBySensor.get(0);
            eventos.add(ultimoEvento);
        }
        return SensorEventoEncomendaDTO.from(eventos);
    }

    /**
     * EP 04 - Criar uma leitura nova de um sensor
     *
     * @param SensorId ID do sensor
     * @return Dados do sensor, com a leitura criada
     */
    @POST
    @Path("{id}")
    @RolesAllowed({"Gestor"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postSensorEvento(@PathParam("id") long SensorId, EventoDTOValor eventoDTO) {
        Sensor sensor = sensorBean.findWithEventos(SensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sensorBean.createEvento(sensor, eventoDTO.getValor());
        return Response.ok(SensorDTO.from(sensor)).build();
    }

    /**
     * EP 06,07,08,09 - O cliente pede a última temperatura registada pelos sensores de temperatura ativos nas suas encomendas
     *
     * @param username do cliente
     * @param tipoSensor Tipo de sensor
     * @return DTO que precisa de ser criado
     */
    @GET
    @Path("/cliente/{username}/sensores/{tipoSensor}")
    @RolesAllowed({"Cliente"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUltimosValoresSensor(@PathParam("username") String username, @PathParam("tipoSensor") String tipoSensor) {
        if(securityContext.isUserInRole("Cliente")){
            if (!username.equals(securityContext.getUserPrincipal().getName())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        //TODO Implementar
        //Vai ser preciso criar um novo DTO
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

}
