package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Gestor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.User;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Hasher;

import java.util.List;

@Stateless
public class GestorBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Hasher hasher;

    public void create(String name, String codFuncionario, String username, String password) {
        var gestor = find(username);
        if (gestor != null) {
            throw new IllegalArgumentException("Gestor already exists: " + username);
        }
        gestor = new Gestor(username, hasher.hash(password),name,codFuncionario);
        em.persist(gestor);
    }

    public Gestor find(String username) {
        Gestor gestor = null;
        List<User> gestores = em.createNamedQuery("getAllUsers", User.class).getResultList();
        for (User g : gestores) {
            if (g.getUsername().equals(username)) {
                gestor = (Gestor)g;
            }
        }
        return gestor;
    }

    public List<Gestor> findAll() {
        return em.createNamedQuery("getAllGestores", Gestor.class).getResultList();
    }
}
