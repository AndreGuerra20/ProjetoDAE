package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.VolumeDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EncomendaBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private LinhaProdutoBean linhaProdutoBean;

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
        Encomenda encomenda = new Encomenda(cliente, estado);
        encomenda.setVolumes(volumesRequest.stream().map(volDto -> new Volume(volDto.getTipoEmbalagem(), encomenda)).collect(Collectors.toList()));
        for (Volume volume : encomenda.getVolumes()) {
            for (VolumeDTO volumeDTO : volumesRequest) {
                volume.addProdutos(volumeDTO.getProdutos().stream().map(dto -> linhaProdutoBean.find(dto.getProdutoId())).collect(Collectors.toList()));
                volume.addSensores(volumeDTO.getSensores().stream().map(dto -> new Sensor(dto.getTipo(), dto.getStatus(), volume)).collect(Collectors.toList()));
            }
        }
        em.persist(encomenda);
        return encomenda;
    }

    public Encomenda find(long id) {
        return em.find(Encomenda.class, id);
    }

    @Transactional
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

    @Transactional
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
}
