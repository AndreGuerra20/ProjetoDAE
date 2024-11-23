package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.util.List;

@Path("volume")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class VolumeService {
    @EJB
    private VolumeBean volumeBean;

    @GET
    @Path("/")
    public List<VolumeDTO> getAllVolumes() {
        List<VolumeDTO> listaVolumes = VolumeDTO.from(volumeBean.findAll());
        if (listaVolumes.isEmpty()) {
            return null;
        }
        return listaVolumes;
    }

    @GET
    @Path("{id}")
    public Response getVolume(@PathParam("id") long id) {
        Volume withBoth = volumeBean.findWithBoth(id);
        if (withBoth == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(VolumeDTO.from(withBoth)).build();
    }
}
