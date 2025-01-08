package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

    @EJB
    private UserBean userBean;

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
        var cliente = clienteBean.find(username);
        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ClienteDTO.from(cliente)).build();
    }

    @GET
    @Path("getName/{clienteId}")
    public Response getClienteName(@PathParam("clienteId") long clienteId) {
        var user = userBean.find(clienteId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var cliente = clienteBean.find(user.getUsername());
        return Response.ok(ClienteDTO.from(cliente)).build();
    }
}
