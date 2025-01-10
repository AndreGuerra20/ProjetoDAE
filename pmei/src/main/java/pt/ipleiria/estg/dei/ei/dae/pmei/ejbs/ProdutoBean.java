package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyConstraintViolationException;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityExistsException;

import java.util.List;

@Stateless
public class ProdutoBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long id,String desc, boolean precisaEmbalagem) throws MyConstraintViolationException, MyEntityExistsException {
        Produto produto = em.find(Produto.class, id);
        if (produto != null) {
            throw new MyEntityExistsException("Produto with Id{" + id + "} already exists");
        }
        try {
            produto = new Produto(id,desc, precisaEmbalagem);
            em.persist(produto);
            em.flush();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }

    }

    public Produto find(long id) {
        return em.find(Produto.class, id);
    }

    public List<Produto> findAll() {
        return em.createNamedQuery("getAllProdutos", Produto.class).getResultList();
    }
}
