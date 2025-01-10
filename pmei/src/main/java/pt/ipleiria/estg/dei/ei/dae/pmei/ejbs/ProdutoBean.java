package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyConstraintViolationException;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

@Stateless
public class ProdutoBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String desc, boolean precisaEmbalagem) throws MyConstraintViolationException {
        try {
            Produto produto = new Produto(desc, precisaEmbalagem);
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
