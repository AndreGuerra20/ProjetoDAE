package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityExistsException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class VolumeBean {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private EncomendaBean encomendaBean;

    public void update(Volume volume) {
        em.merge(volume);
    }

    public void create(long volumeId,String tipoEmbalagem, long encomenda_id) throws MyEntityExistsException {
        var encomenda = em.find(Encomenda.class, encomenda_id);
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda {" + encomenda_id + "} not found");
        }
        Volume volume = em.find(Volume.class, volumeId);
        if (volume != null) {
            throw new MyEntityExistsException("Volume wiht Id{" + volumeId + "} already exists");
        }
        volume = new Volume(volumeId,tipoEmbalagem, encomenda);
        em.persist(volume);
    }

    public Volume find(long id) {
        return em.find(Volume.class, id);
    }

    public List<Volume> findAll() {
        List<Volume> volumes = em.createNamedQuery("getAllVolumes", Volume.class).getResultList();
        for (Volume volume : volumes) {
            Hibernate.initialize(volume.getProdutos());
            for (Sensor sensor : volume.getSensores()) {
                Hibernate.initialize(sensor.getEventos());
            }
        }
        return volumes;
    }

    public Volume findWithProdutos(long id) {
        Volume volume = em.find(Volume.class, id);
        if(volume == null) {
            throw new IllegalArgumentException("Volume {" + id + "} not found");
        }
        Hibernate.initialize(volume.getProdutos());
        return volume;
    }

    public Volume findWithSensores(long id) {
        Volume volume = em.find(Volume.class, id);
        if(volume == null) {
            throw new IllegalArgumentException("Volume {" + id + "} not found");
        }
        Hibernate.initialize(volume.getSensores());
        for (Sensor sensor : volume.getSensores()) {
            Hibernate.initialize(sensor.getEventos());
        }
        return volume;
    }

    public Volume findWithBoth(long id) {
        Volume volume = em.find(Volume.class, id);
        if(volume == null) {
            return null;
        }
        Hibernate.initialize(volume.getProdutos());
        for (Sensor sensor : volume.getSensores()) {
            Hibernate.initialize(sensor.getEventos());
        }
        return volume;
    }

    public List<Sensor> findAllSensors(long id) {
        List<Sensor> sensores = em.createNamedQuery("getAllSensores", Sensor.class).getResultList();
        List<Sensor> sensoresByVolumeId = new ArrayList<>();
        for (Sensor sensor : sensores) {
            if (sensor.getVolume().getIdVolume() == id) {
                Hibernate.initialize(sensor.getEventos());
                sensoresByVolumeId.add(sensor);
            }
        }
        if (sensoresByVolumeId.isEmpty()) {
            return null;
        }
        return sensoresByVolumeId;
    }

    public void update(long volumeId, String estado){
        var volume = find(volumeId);
        if (volume == null) {
            throw new IllegalArgumentException("Volume {" + volumeId + "} not found");
        }
        if(volume.isEntregue()){
            throw new IllegalArgumentException("Volume {" + volumeId + "} already delivered");
        }
        volume.setEntregue(estado.equals("Entregue"));
        Hibernate.initialize(volume.getEncomenda());
        Encomenda encomenda = encomendaBean.findWithVolumes(volume.getEncomenda().getId());
        boolean allDelivered = true;
        for (Volume v : encomenda.getVolumes()) {
            if (!v.isEntregue()) {
                allDelivered = false;
                break;
            }
        }
        if (allDelivered) {
            encomenda.setEstado("Entregue");
        }
        em.merge(volume);
    }
}
