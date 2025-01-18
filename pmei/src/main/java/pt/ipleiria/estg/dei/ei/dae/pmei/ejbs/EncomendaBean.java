package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.LinhaProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.*;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EncomendaBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private LinhaProdutoBean linhaProdutoBean;

    @EJB
    private ProdutoBean produtoBean;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private VolumeBean volumeBean;

    public Encomenda create(long encomendaId,long clienteId, String estado, List<Volume> volumes) throws MyEntityNotFoundException, MyConstraintViolationException {
        var cliente = em.find(Cliente.class, clienteId);
        if (cliente == null) {
            throw new MyEntityNotFoundException("Cliente {" + clienteId + "} not found");
        }
        try {
            Encomenda encomenda = new Encomenda(encomendaId,cliente, estado);
            encomenda.setVolumes(volumes);
            em.persist(encomenda);
            em.flush();
            return encomenda;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid estado {" + estado + "} should be one of " + Encomenda.LISTA_ESTADOS);
        }
    }

    public Encomenda createWeb(long encomendaId,long clienteId, String estado, List<VolumeDTO> volumesRequest) throws MyEntityNotFoundException, MyConstraintViolationException {
        if (volumesRequest == null) {
            volumesRequest = new ArrayList<>();
        }
        var cliente = em.find(Cliente.class, clienteId);
        if (cliente == null) {
            throw new MyEntityNotFoundException("Cliente {" + clienteId + "} not found");
        }
        Encomenda encomenda = em.find(Encomenda.class, encomendaId);
        if (encomenda != null) {
            throw new IllegalArgumentException("Encomenda with Id{" + encomendaId + "} already exists");
        }
        try {
            encomenda = create(encomendaId,clienteId,estado,volumesRequest.stream().map(volDto -> {
                Volume volume = em.find(Volume.class, volDto.getIdVolume());
                if (volume != null) {
                    throw new IllegalArgumentException("Volume with Id{" + volDto.getIdVolume() + "} already exists");
                }
                Volume vol = new Volume(volDto.getIdVolume(),volDto.getTipoEmbalagem(),null);
                //vol.setTipoEmbalagem(volDto.getTipoEmbalagem());
                vol.setProdutos(volDto.getProdutos().stream().map(lpDto -> {
                    LinhaProduto produto = new LinhaProduto();
                    Produto p = produtoBean.find(lpDto.getId());
                    if (p == null) {
                        throw new MyEntityNotFoundException("Produto with Id{" + lpDto.getId() + "} not found");
                    }
                    if (p.isPrecisaEmbalagemAdicional() && (vol.getTipoEmbalagem() == null || vol.getTipoEmbalagem().isEmpty())) {
                        throw new IllegalArgumentException("Produto with Id{" + lpDto.getId() + "} needs additional packaging");
                    }
                    produto.setProduto(p);
                    produto.setQuantidade(lpDto.getQuantidade());
                    produto.setVolume(vol);
                    return produto;
                }).collect(Collectors.toList()));
                vol.setSensores(volDto.getSensores().stream().map(sensorDTO -> {
                    Sensor s = em.find(Sensor.class, sensorDTO.getId());
                    if (s != null) {
                        throw new MyEntityExistsException("Sensor with Id{" + sensorDTO.getId() + "} already exists");
                    }
                    Sensor sensor = new Sensor(sensorDTO.getId(),sensorDTO.getTipo(),sensorDTO.getStatus(),vol);
                    //sensor.setTipo(sensorDTO.getTipo());
                    //sensor.setStatus(sensorDTO.getStatus());
                    //sensor.setVolume(vol);
                    return sensor;
                }).collect(Collectors.toList()));
                return vol;
            }).collect(Collectors.toList()));
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
        em.persist(encomenda);
        for (Volume volume : encomenda.getVolumes()) {
            volume.setEncomenda(encomenda);
        }
        em.persist(encomenda);
        return findWithVolumes(encomenda.getId());
    }

    public Encomenda find(long id) {
        return em.find(Encomenda.class, id);
    }

    public List<Encomenda> findAll() {
        List<Encomenda> encomendas = em.createNamedQuery("getAllEncomendas", Encomenda.class).getResultList();
        for (Encomenda encomenda : encomendas) {
            Hibernate.initialize(encomenda.getVolumes());
            for (Volume volume : encomenda.getVolumes()) {
                Hibernate.initialize(volume.getSensores());
                for (Sensor sensor : volume.getSensores()) {
                    Hibernate.initialize(sensor.getEventos());
                }
                Hibernate.initialize(volume.getProdutos());
            }
        }
        return encomendas;
    }

    public Encomenda findWithVolumes(long id) {
        Encomenda encomenda = em.find(Encomenda.class, id);
        if(encomenda == null) {
            return null;
        }
        Hibernate.initialize(encomenda.getVolumes());
        for (Volume volume : encomenda.getVolumes()) {
            Hibernate.initialize(volume.getProdutos());
            Hibernate.initialize(volume.getSensores());
            for (Sensor sensor : volume.getSensores()) {
                Hibernate.initialize(sensor.getEventos());
            }
        }
        return encomenda;
    }

    public void update(Encomenda encomenda) {
        em.merge(encomenda);
    }

    public void addVolumes(Encomenda encomenda, List<VolumeDTO> volumes) {
        for (VolumeDTO volumeDTO : volumes) {
            Volume volume = volumeBean.find(volumeDTO.getIdVolume());
            if (volume != null){
                throw new MyEntityExistsException("Volume with id {" + volumeDTO.getIdVolume() +"} already exists");
            }
            volume = new Volume();
            volume.setIdVolume(volumeDTO.getIdVolume());
            volume.setTipoEmbalagem(volumeDTO.getTipoEmbalagem());
            volume.setEncomenda(encomenda);

            Volume finalVolume = volume;
            List<LinhaProduto> produtos = volumeDTO.getProdutos().stream().map(produtoDTO -> {
                Produto produto = produtoBean.find(produtoDTO.getId());
                if (produto == null) {
                    throw new MyEntityNotFoundException("Produto with id {" + produtoDTO.getId() +"} not found");
                }
                if (produto.isPrecisaEmbalagemAdicional() && (finalVolume.getTipoEmbalagem() == null || finalVolume.getTipoEmbalagem().isEmpty())) {
                    throw new IllegalArgumentException("Produto with id {" + produtoDTO.getId() +"} needs additional packaging");
                }
                LinhaProduto linhaProduto = new LinhaProduto();
                linhaProduto.setProduto(produto);
                linhaProduto.setQuantidade(produtoDTO.getQuantidade());
                linhaProduto.setVolume(finalVolume);
                return linhaProduto;
            }).collect(Collectors.toList());

            Volume finalVolume1 = volume;
            List<Sensor> sensores = volumeDTO.getSensores().stream().map(sensorDTO -> {
                Sensor sensor = sensorBean.find(sensorDTO.getId());
                if (sensor != null) {
                    throw new MyEntityExistsException("Sensor with id {" + sensorDTO.getId() +"} already exists");
                }
                sensor = new Sensor();
                sensor.setId(sensorDTO.getId());
                sensor.setTipo(sensorDTO.getTipo());
                sensor.setStatus(true);
                sensor.setVolume(finalVolume1);
                return sensor;
            }).collect(Collectors.toList());

            volume.setProdutos(produtos);
            volume.setSensores(sensores);
            em.merge(volume);
            encomenda.addVolume(volume);
        }
        update(encomenda);
    }
}
