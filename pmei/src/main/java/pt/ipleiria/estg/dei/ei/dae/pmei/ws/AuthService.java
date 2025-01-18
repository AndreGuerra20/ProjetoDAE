package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.GestorBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.LogisticaBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Gestor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Logistica;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.TokenIssuer;

@Path("auth")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AuthService {
    @Context
    private SecurityContext securityContext;

    @Inject
    private TokenIssuer tokenIssuer;

    @EJB
    private UserBean userBean;

    @EJB
    private GestorBean gestorBean;

    @EJB
    private LogisticaBean logisticaBean;

    /**
     * EP 07 - Um utilizador faz login
     *
     * @param auth Informação para fazer login
     * @return Token para efeitos de autenticação e autorização
     */
    @POST
    @Path("/login")
    public Response authenticate(@Valid AuthDTO auth) {
        if (userBean.canLogin(auth.getUsername(), auth.getPassword())) {
            String token = tokenIssuer.issue(auth.getUsername());
            return Response.ok(token).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * EP 10 - Um gestor autenticado pretende criar uma nova conta com a função de gestor
     *
     * @param gestorDTO Dados para a criação como nome, codFuncionario, etc
     * @return Gestor criado
     */
    @POST
    @Path("/gestor")
    @Authenticated
    @RolesAllowed({"Gestor"})
    public Response registerGestor(@Valid GestorAuthDTO gestorDTO) throws MyConstraintViolationException, MyEntityExistsException {
        Gestor gestor = gestorBean.create(gestorDTO.getNome(),gestorDTO.getCodFuncionario(), gestorDTO.getUsername(), gestorDTO.getPassword(), gestorDTO.getEmail());
        if(gestor == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(GestorDTO.from(gestor)).build();
    }

    /**
     * EP 11 - Um gestor autenticado pretende criar uma nova conta com a função de logistica
     *
     * @param logisticaDTO Dados para a criação como nome, codFuncionario, etc
     * @return User Logistica criado
     */
    @POST
    @Path("/logistica")
    @Authenticated
    @RolesAllowed({"Gestor"})
    public Response registerLogistica(@Valid LogisticaAuthDTO logisticaDTO) throws MyConstraintViolationException, MyEntityExistsException {
        Logistica logistica = logisticaBean.create(logisticaDTO.getNome(), logisticaDTO.getCodFuncionario(), logisticaDTO.getUsername(), logisticaDTO.getPassword(), logisticaDTO.getEmail());
        if(logistica == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(UserDTO.from(logistica)).build();
    }

    /**
     * EP 22 - Um utilizador autenticado quer visualizar os seus dados
     *
     * @return UserDTO com os dados do utilizador autenticado
     */
    @GET
    @Authenticated
    @Path("/user")
    public Response getAuthenticatedUser() {
        var username = securityContext.getUserPrincipal().getName();
        var user = userBean.findOrFail(username);
        return Response.ok(UserDTO.from(user)).build();
    }

    /**
     * EP 23 - Um utilizador autenticado quer mudar a sua password
     * EP 24 - Um gestor autenticado quer mudar a password de um utilizador
     *
     * @param passwordChangeDTO Dados para a mudança de password
     * @return Response HTTP
     */
    @PUT
    @Path("/setPassword")
    @Authenticated
    public Response setPassword(@Valid PasswordChangeDTO passwordChangeDTO) {
        var principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Gestor") &&
                passwordChangeDTO.getUsername() != null && !passwordChangeDTO.getUsername().isEmpty()) {
            //O gestor quer mudar a password de um utilizador
            userBean.setPasswordGestor(
                    passwordChangeDTO.getUsername(),
                    passwordChangeDTO.getPassword(),
                    passwordChangeDTO.getConfirmPassword()
            );
        }
        else {
            //Um utilizador de Logistica ou Cliente quer mudar a sua password
            userBean.setPassword(
                    principal.getName(),
                    passwordChangeDTO.getPassword(),
                    passwordChangeDTO.getOldPassword(),
                    passwordChangeDTO.getConfirmPassword()
            );
        }
        return Response.ok().build();
    }
}
