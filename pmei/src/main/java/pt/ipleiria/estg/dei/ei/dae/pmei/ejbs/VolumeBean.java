package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class VolumeBean {
    @PersistenceContext
    private EntityManager em;

    public void update(Volume volume) {
        em.merge(volume);
    }

    public void create(String tipoEmbalagem, long encomenda_id) {
        var encomenda = em.find(Encomenda.class, encomenda_id);
        if (encomenda == null) {
            throw new IllegalArgumentException("Encomenda {" + encomenda_id + "} not found");
        }
        Volume volume = new Volume(tipoEmbalagem, encomenda);
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
            throw new IllegalArgumentException("Volume {" + id + "} not found");
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
}
