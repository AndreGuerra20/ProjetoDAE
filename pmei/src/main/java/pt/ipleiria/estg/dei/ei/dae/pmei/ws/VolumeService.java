package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.ProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.VolumeBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.LinhaProduto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;

import java.util.ArrayList;
import java.util.List;

@Path("volumes")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
public class VolumeService {
    @EJB
    private VolumeBean volumeBean;

    @EJB
    private ProdutoBean produtoBean;

    @Context
    private SecurityContext securityContext;

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
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        if(securityContext.isUserInRole("Cliente")){
            if (!withBoth.getEncomenda().getCliente().getUsername().equals(securityContext.getUserPrincipal().getName())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        return Response.ok(VolumeDTO.from(withBoth)).build();
    }

    /**
     * EP 19 - O gestor acede aos sensores de um volume
     *
     * @return Dados dos sensores do volume
     */
    @RolesAllowed({"Gestor"})
    @GET
    @Path("{id}/sensores")
    public List<SensorUltimoEventoDTO> getSensores(@PathParam("id") long id) {
        List<Sensor> sensores = volumeBean.findAllSensors(id);
        if (sensores.isEmpty()) {
            return null;
        }
        System.out.println(sensores);
        return SensorUltimoEventoDTO.from(sensores);
    }

    /**
     * EP 9 - Um gestor acede aos produtos de um volume
     *
     * @param id ID do volume
     * @return Lista de produtos do volume
     */
    @GET
    @Path("{id}/produtos")
    @RolesAllowed({"Gestor"})
    public Response getVolumeProdutos(@PathParam("id") long id) {
        List<ProdutoDTOComQuantidade> produtoDTOS = new ArrayList<>();
        Volume volume = volumeBean.findWithProdutos(id);
        if (volume == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        for (LinhaProduto lp : volume.getProdutos()) {
            Produto p = produtoBean.find(lp.getProduto().getId());
            if (p == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            ProdutoDTOComQuantidade produtoDTO = ProdutoDTOComQuantidade.from(p);
            produtoDTO.setQuantidade(lp.getQuantidade());
            produtoDTOS.add(produtoDTO);
        }
        return Response.ok(produtoDTOS).build();
    }

    /**
     * EP 21 - O volume é entregue ao cliente
     *
     * @param volumeId ID do volume
     * @return Volume atualizado
     */
    @PATCH
    @Path("{volumeId}")
    @RolesAllowed({"Logistica"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setVolumeEstado(@PathParam("volumeId") long volumeId, VolumeEstadoDTO volumeEstadoDTO) {
        volumeBean.update(volumeId,volumeEstadoDTO.getEstado());
        Volume updatedVolume = volumeBean.findWithBoth(volumeId);
        return Response.status(Response.Status.OK)
                .entity(VolumeDTO.from(updatedVolume))
                .build();
    }
}
