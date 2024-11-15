package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.EncomendaBean;

@Path("encomenda")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class EncomendaService {
    @EJB
    private EncomendaBean encomendaBean;

    @GET
    @Path("/")
    public Response getAllEncomendas() {
//        return Response.ok(encomendaBean.findAll()).build();
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("{id}")
    public Response getEncomenda(@PathParam("id") long id) {
        var encomenda = encomendaBean.find(id);
        if (encomenda == null) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }
        return Response.ok(encomenda).build();
    }
}