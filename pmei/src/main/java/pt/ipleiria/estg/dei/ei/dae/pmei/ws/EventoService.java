package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.EventoDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.EventoBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;

import java.util.List;

@Path("eventos")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
public class EventoService {
    @EJB
    private EventoBean eventoBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    @RolesAllowed({"Gestor"})
    public List<EventoDTO> getAllEventos() {
        List<EventoDTO> listaEventos = EventoDTO.from(eventoBean.findAll());
        if(listaEventos.isEmpty()){
            return null;
        }
        return listaEventos;
    }

    @GET
    @Path("{id}")
    public Response getEvento(@PathParam("id") long id) {
        Evento evento = eventoBean.find(id);
        if (evento == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(securityContext.isUserInRole("Cliente")){
            if (!evento.getSensor().getVolume().getEncomenda().getCliente().getUsername().equals(securityContext.getUserPrincipal().getName())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        return Response.ok(EventoDTO.from(evento)).build();
    }
}
