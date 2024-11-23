package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.EncomendaDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.EncomendaBean;

import java.util.List;

@Path("encomenda")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
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
}