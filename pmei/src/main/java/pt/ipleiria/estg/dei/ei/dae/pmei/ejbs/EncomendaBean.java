package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.*;

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

    public Encomenda create(long clienteId, String estado, List<Volume> volumes) {
        var cliente = em.find(Cliente.class, clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente {" + clienteId + "} not found");
        }
        Encomenda encomenda = new Encomenda(cliente, estado);
        encomenda.setVolumes(volumes);
        em.persist(encomenda);
        return encomenda;
    }

    public Encomenda createWeb(long clienteId, String estado, List<VolumeDTO> volumesRequest) {
        if (volumesRequest == null) {
            volumesRequest = new ArrayList<>();
        }
        var cliente = em.find(Cliente.class, clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente {" + clienteId + "} not found");
        }
        Encomenda encomenda = create(clienteId,estado,volumesRequest.stream().map(volDto -> {
            Volume vol = new Volume();
            vol.setTipoEmbalagem(volDto.getTipoEmbalagem());
            vol.setProdutos(volDto.getProdutos().stream().map(lpDto -> {
                LinhaProduto produto = new LinhaProduto();
                produto.setProduto(produtoBean.find(lpDto.getId()));
                produto.setQuantidade(lpDto.getQuantidade());
                produto.setVolume(vol);
                return produto;
            }).collect(Collectors.toList()));
            vol.setSensores(volDto.getSensores().stream().map(sensorDTO -> {
                Sensor sensor = new Sensor();
                sensor.setTipo(sensorDTO.getTipo());
                sensor.setStatus(sensorDTO.getStatus());
                sensor.setVolume(vol);
                return sensor;
            }).collect(Collectors.toList()));
            return vol;
        }).collect(Collectors.toList()));
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

    public Encomenda areAllVolumesDelivered(long id) {
        Encomenda encomenda = findWithVolumes(id);
        if(encomenda == null) {
            throw new IllegalArgumentException("Encomenda {" + id + "} not found");
        }
        for (Volume volume : encomenda.getVolumes()) {
            if (!volume.isEntregue()) {
                throw new IllegalArgumentException("Encomenda {" + id + "} does not have all volumes delivered - {" + volume.getIdVolume() + "} not delivered");
            }
        }
        encomenda.setEstado("Entregue");
        em.merge(encomenda);
        return encomenda;
    }
}
