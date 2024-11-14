package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;

@Stateless
public class ProdutoBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long id, int stock) {
        Produto produto = new Produto(id, stock);
        em.persist(produto);
    }

    public Produto find(long id) {
        return em.find(Produto.class, id);
    }
}
