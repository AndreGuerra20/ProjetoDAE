package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.TokenIssuer;

@Path("users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserService {

    @Context
    private SecurityContext securityContext;

    @Inject
    private TokenIssuer tokenIssuer;

    @EJB
    private UserBean userBean;

    /**
     * EP 30 - Um utilizador autenticado quer visualizar todos os utilizadores
     *
     * @return lista de utilizadores
     */
    @GET
    @Path("/")
    @Authenticated
    @RolesAllowed({"Gestor"})
    public Response getAllUsers() {
        return Response.ok(UserDTO.from(userBean.findAll())).build();
    }

}
