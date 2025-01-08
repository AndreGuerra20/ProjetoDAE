package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.ClienteDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.ClienteBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;

import java.util.List;

@Path("cliente")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
public class ClienteService {
    @EJB
    private ClienteBean clienteBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public List<ClienteDTO> getAllClientes() {
        List<ClienteDTO> listaClientes = ClienteDTO.from(clienteBean.findAll());
        if(listaClientes.isEmpty()){
            return null;
        }
        return listaClientes;
    }

    @GET
    @Path("{username}")
    public Response getCliente(@PathParam("username") String username) {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        var cliente = clienteBean.find(username);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ClienteDTO.from(cliente)).build();
    }
}
