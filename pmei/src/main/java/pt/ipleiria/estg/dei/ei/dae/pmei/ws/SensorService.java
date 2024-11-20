package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.SensorDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.SensorBean;

import java.util.List;

@Path("sensor")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class SensorService {
    @EJB
    private SensorBean sensorBean;

    @GET
    @Path("/")
    public List<SensorDTO> getAllSensors() {
        return SensorDTO.from(sensorBean.findAll());
    }

    @GET
    @Path("{id}")
    public SensorDTO getSensor(@PathParam("id") long id) {
        return SensorDTO.from(sensorBean.findWithEventos(id));
    }
}
