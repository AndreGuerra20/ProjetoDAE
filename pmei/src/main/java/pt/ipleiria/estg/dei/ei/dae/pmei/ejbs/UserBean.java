package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.User;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;
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

    public void setPassword(String username, String password, String oldPassword, String confirmPassword) {
        User user = findOrFail(username);
        if(user == null) {
            throw new MyEntityNotFoundException("User with username " + username + " not found.");
        }
        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new IllegalArgumentException("Old password field is required.");
        }
        if (password == null || password.isEmpty() || confirmPassword == null
                || confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
            throw new IllegalArgumentException("Password and confirm password fields are required and must match.");
        }
        if (canLogin(username, oldPassword)) {
            user.setPassword(hasher.hash(password));
            em.merge(user);
        }
    }

    //método para alterar a password de qualquer utilizador como Gestor
    public void setPasswordGestor(String username, String password, String confirmPassword){
        User user = findOrFail(username);
        if(user == null) {
            throw new MyEntityNotFoundException("User with username " + username + " not found.");
        }
        if (password == null || password.isEmpty() || confirmPassword == null
                || confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
            throw new IllegalArgumentException("Password and confirm password fields are required and must match.");
        }
        user.setPassword(hasher.hash(password));
        em.merge(user);
    }

    //método para buscar todos os utilizadores
    public List<User> findAll() {
        return em.createNamedQuery("getAllUsers", User.class).getResultList();
    }
}
