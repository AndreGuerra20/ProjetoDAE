package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;

import java.util.ArrayList;
import java.util.List;

@Path("encomenda")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
public class EncomendaService {
    @EJB
    private EncomendaBean encomendaBean;

    @GET
    @Path("/")
    public List<EncomendaDTO> getAllEncomendas() {
        List<EncomendaDTO> listaEncomendas = EncomendaDTO.from(encomendaBean.findAll());
        if(listaEncomendas.isEmpty()){
            return null;
        }
        return listaEncomendas;
    }

    @GET
    @Path("{id}")
    public Response getEncomenda(@PathParam("id") long id) {
        var encomenda = encomendaBean.findWithVolumes(id);
        if (encomenda == null) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }
        return Response.ok(EncomendaDTO.from(encomenda)).build();
    }

    /**
     * EP 01 - Logistica cria encomenda nova
     *
     * @param encomendaRequest Encomenda proveniente do pedido
     * @return Encomenda criada
     */
    @POST
    @Path("/")
    @RolesAllowed({"Logistica"})
    public Response createEncomenda(EncomendaDTO encomendaRequest) {
        Encomenda encomenda = encomendaBean.createWeb(encomendaRequest.getCustomerId(), encomendaRequest.getEstado(), encomendaRequest.getVolumes());
        var encomendaCriada = encomendaBean.findWithVolumes(encomenda.getId());
        return Response.ok(EncomendaDTO.from(encomendaCriada)).build();
    }

    /**
     * EP 12 - O gestor acede aos sensores de uma encomenda
     *
     * @param id ID da encomenda
     * @return Lista dos sensores da encomenda
     */
    @GET
    @Path("{id}/sensores")
    @RolesAllowed({"Gestor"})
    public Response getSensores(@PathParam("id") long id) {
        List<SensorSemEventosDTO> sensores = new ArrayList<>();
        Encomenda encomenda = encomendaBean.findWithVolumes(id);
        if (encomenda == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        for (Volume volume : encomenda.getVolumes()) {
            for (Sensor sensor : volume.getSensores()) {
                SensorSemEventosDTO sensorSemEventosDTO = SensorSemEventosDTO.from(sensor);
                sensores.add(sensorSemEventosDTO);
            }
        }
        return Response.ok(sensores).build();
    }

    /**
     * EP 22 - A encomenda é finalizada
     *
     * @return Encomenda atualizada depois de finalizada
     */
    @PATCH
    @Path("/")
    @RolesAllowed({"Logistica"})
    public Response finalizeEncomenda(EncomendaDTO encomendaRequest) {
        Encomenda encomenda = encomendaBean.areAllVolumesDelivered(encomendaRequest.getCustomerId());
        if(encomenda == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(EncomendaDTO.from(encomenda)).build();
    }
}