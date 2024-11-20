package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.EventoDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.EventoBean;

import java.util.List;

@Path("evento")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class EventoService {
    @EJB
    private EventoBean eventoBean;

    @GET
    @Path("/")
    public List<EventoDTO> getAllEventos() {
        return EventoDTO.from(eventoBean.findAll());
    }

    @GET
    @Path("{id}")
    public EventoDTO getEvento(@PathParam("id") long id) {
        return EventoDTO.from(eventoBean.find(id));
    }
}
