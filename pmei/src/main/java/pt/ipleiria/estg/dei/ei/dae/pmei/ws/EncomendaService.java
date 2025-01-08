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

    // EP 01 - Um utilizador, autenticado como logística, cria uma nova encomenda utilizando o protocolo HTTP, verbo POST, para o sítio: `/monitorizacao/api/encomenda`
    @POST
    @Path("/")
    @RolesAllowed({"Logistica"})
    public Response createEncomenda(EncomendaDTO encomendaRequest) {
        Encomenda encomenda = encomendaBean.createWeb(encomendaRequest.getCustomerId(), encomendaRequest.getEstado(), encomendaRequest.getVolumes());
        var xpto = encomendaBean.findWithVolumes(encomenda.getId());
        return Response.ok(EncomendaDTO.from(xpto)).build();
    }
}