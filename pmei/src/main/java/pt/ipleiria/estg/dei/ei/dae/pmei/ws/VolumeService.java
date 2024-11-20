package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.VolumeBean;

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
        return VolumeDTO.from(volumeBean.findAll());
    }

    @GET
    @Path("{id}")
    public VolumeDTO getVolume(@PathParam("id") long id) {
        return VolumeDTO.from(volumeBean.findWithBoth(id));
    }
}
