package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.ClienteBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.EncomendaBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.ProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("encomendas")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Authenticated
public class EncomendaService {
    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private ProdutoBean produtoBean;

    @EJB
    private ClienteBean clienteBean;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    public List<EncomendaDTO> getAllEncomendas() {
        List<EncomendaDTO> listaEncomendas = EncomendaDTO.from(encomendaBean.findAll());
        if(listaEncomendas.isEmpty()){
            return null;
        }
        return listaEncomendas;
    }

    @GET
    @Path("/cliente")
    @RolesAllowed({"Cliente"})
    public List<EncomendaDTO> getEncomendasCliente() {
        List<EncomendaDTO> listaEncomendas = EncomendaDTO.from(encomendaBean.findAll());
        if(listaEncomendas.isEmpty()){
            return null;
        }
        return listaEncomendas.stream().filter(encomendaDTO -> {
            Encomenda encomenda = encomendaBean.find(encomendaDTO.getEncomendaId());
            return encomenda.getCliente().getUsername().equals(securityContext.getUserPrincipal().getName());
        }).collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response getEncomenda(@PathParam("id") long id) {
        var encomenda = encomendaBean.findWithVolumes(id);
        if (encomenda == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        if(securityContext.isUserInRole("Cliente")){
            var cliente = clienteBean.find(encomenda.getCliente().getId());
            if (cliente == null || !cliente.getUsername().equals(securityContext.getUserPrincipal().getName())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        return Response.ok(EncomendaDTO.from(encomenda)).build();
    }

    /**
     * EP 01 - Logistica cria encomenda nova
     *
     * @param encomendaRequest Encomenda proveniente do pedido
     * @return Encomenda criada
     */
    @POST
    @Path("/")
    @RolesAllowed({"Logistica"})
    public Response createEncomenda(EncomendaDTO encomendaRequest) throws MyConstraintViolationException, MyEntityNotFoundException {
        encomendaBean.createWeb(encomendaRequest.getEncomendaId(),encomendaRequest.getCustomerId(), encomendaRequest.getEstado(), encomendaRequest.getVolumes());
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * EP 12 - O gestor acede aos sensores de uma encomenda
     *
     * @param id ID da encomenda
     * @return Lista dos sensores da encomenda
     */
    @GET
    @Path("{id}/sensores")
    @RolesAllowed({"Gestor","Cliente"})
    public Response getSensores(@PathParam("id") long id) {
        List<SensorSemEventosDTO> sensores = new ArrayList<>();
        Encomenda encomenda = encomendaBean.findWithVolumes(id);
        if (encomenda == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(securityContext.isUserInRole("Cliente")){
            if (!encomenda.getCliente().getUsername().equals(securityContext.getUserPrincipal().getName())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        for (Volume volume : encomenda.getVolumes()) {
            for (Sensor sensor : volume.getSensores()) {
                SensorSemEventosDTO sensorSemEventosDTO = SensorSemEventosDTO.from(sensor);
                sensores.add(sensorSemEventosDTO);
            }
        }
        return Response.ok(sensores).build();
    }

    //TODO - Passsar maior parte do código para o EncomendaBean
    /**
     * EP 15 - Adicionar novos volumes a uma encomenda que já existe
     *
     * @param id ID da encomenda
     * @return Encomenda atualizada
     */
    @POST
    @Path("{id}")
    @RolesAllowed({"Logistica"})
    public Response addVolumesToEncomenda(@PathParam("id") long id, List<VolumeDTO> volumesRequest) throws MyEntityNotFoundException {
        System.out.println("Adding volumes to encomenda " + id);
        Encomenda encomenda = encomendaBean.findWithVolumes(id);
        if (encomenda == null) {
            System.out.println("Encomenda not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        for (VolumeDTO volumeDTO : volumesRequest) {
            Volume volume = new Volume();
            volume.setTipoEmbalagem(volumeDTO.getTipoEmbalagem());
            volume.setEncomenda(encomenda);

            List<LinhaProduto> produtos = volumeDTO.getProdutos().stream().map(produtoDTO -> {
                Produto produto = produtoBean.find(produtoDTO.getId());
                if (produto == null) {
                    throw new WebApplicationException("Produto not found", Response.Status.NOT_FOUND);
                }
                LinhaProduto linhaProduto = new LinhaProduto();
                linhaProduto.setProduto(produto);
                linhaProduto.setQuantidade(produtoDTO.getQuantidade());
                linhaProduto.setVolume(volume);
                return linhaProduto;
            }).collect(Collectors.toList());

            List<Sensor> sensores = volumeDTO.getSensores().stream().map(sensorDTO -> {
                Sensor sensor = new Sensor();
                sensor.setTipo(sensorDTO.getTipo());
                sensor.setStatus(true);
                sensor.setVolume(volume);
                return sensor;
            }).collect(Collectors.toList());

            volume.setProdutos(produtos);
            volume.setSensores(sensores);
            encomenda.addVolume(volume);
            encomendaBean.update(encomenda);
        }
        return Response.ok(EncomendaDTO.from(encomendaBean.findWithVolumes(id))).build();
    }
}