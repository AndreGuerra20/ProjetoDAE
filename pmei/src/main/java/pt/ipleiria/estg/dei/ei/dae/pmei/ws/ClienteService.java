package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.ClienteDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.ClienteDTOLogistica;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.ClienteBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;

import java.util.List;

@Path("clientes")
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
    @RolesAllowed({"Gestor","Logistica"})
    public Response getAllClientes() {
        if (securityContext.isUserInRole("Gestor")) {
            List<ClienteDTO> listaClientes = ClienteDTO.from(clienteBean.findAll());
            if(listaClientes.isEmpty()){
                throw new MyEntityNotFoundException("No clients found");
            }
            return Response.ok(listaClientes).build();
        } else if (securityContext.isUserInRole("Logistica")) {
            List<ClienteDTOLogistica> listaClientes = ClienteDTOLogistica.from(clienteBean.findAll());
            if(listaClientes.isEmpty()){
                throw new MyEntityNotFoundException("No clients found");
            }
            return Response.ok(listaClientes).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("{username}")
    @RolesAllowed({"Gestor","Cliente"})
    public Response getCliente(@PathParam("username") String username) {
        var principal = securityContext.getUserPrincipal();
        if(!principal.getName().equals(username) && !securityContext.isUserInRole("Gestor")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        var cliente = clienteBean.find(username);
        if (cliente == null) {
            throw new MyEntityNotFoundException("Client with username " + username + " not found.");
        }
        return Response.ok(ClienteDTO.from(cliente)).build();
    }
}
