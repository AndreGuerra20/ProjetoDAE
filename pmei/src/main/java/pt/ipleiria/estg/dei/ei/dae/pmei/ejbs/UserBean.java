package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.User;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Hasher;

import java.util.List;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private Hasher hasher;

    public User findOrFail(String username) {
        User user = null;
        List<User> users = em.createNamedQuery("getAllUsers", User.class).getResultList();
        for (User c : users) {
            if (c.getUsername().equals(username)) {
                user = c;
                break;
            }
        }
        return user;
    }

    public boolean canLogin(String username, String password) {
        var user = findOrFail(username);
        return user != null && user.getPassword().equals(hasher.hash(password));
    }

    public User find(long id) {
        return em.find(User.class, id);
    }

}
