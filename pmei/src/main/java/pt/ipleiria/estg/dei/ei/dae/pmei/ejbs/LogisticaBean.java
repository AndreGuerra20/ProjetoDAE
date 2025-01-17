package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Gestor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Logistica;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.User;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Hasher;

import java.util.List;

@Stateless
public class LogisticaBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public Logistica create(String name, String codFuncionario, String username, String password, String email) throws MyEntityExistsException {
        var logistica = find(username);
        if (logistica != null) {
            throw new MyEntityExistsException("Logistica already exists: " + username);
        }
        List<User> logisticas = em.createNamedQuery("getAllUsers", User.class).getResultList();
        for (User lg : logisticas) {
            if(lg instanceof Logistica) {
                if(((Logistica) lg).getCodFuncionario().equals(codFuncionario)) {
                    throw new IllegalArgumentException("Logistica " + lg.getUsername() + " already has that codFuncionario: " + codFuncionario);
                }
            }
        }
        logistica = new Logistica(username, hasher.hash(password),name,codFuncionario,email);
        em.persist(logistica);
        return logistica;
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
