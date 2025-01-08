package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Gestor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Logistica;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.User;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Hasher;

import java.util.List;

@Stateless
public class LogisticaBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public void create(String name, String codFuncionario, String username, String password) {
        var logistica = find(username);
        if (logistica != null) {
            throw new IllegalArgumentException("Logistica already exists: " + username);
        }
        logistica = new Logistica(username, hasher.hash(password),name,codFuncionario);
        em.persist(logistica);
    }

    public Logistica find(String username) {
        Logistica logistica = null;
        List<User> allLogisticas = em.createNamedQuery("getAllUsers", User.class).getResultList();
        for (User lg : allLogisticas) {
            if (lg.getUsername().equals(username)) {
                logistica = (Logistica) lg;
            }
        }
        return logistica;
    }

    public List<Logistica> findAll() {
        return em.createNamedQuery("getAllLogisticas", Logistica.class).getResultList();
    }
}
