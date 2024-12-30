package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;

import java.util.List;

@Stateless
public class ProdutoBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String desc, boolean precisaEmbalagem) {
        Produto produto = new Produto(desc, precisaEmbalagem);
        em.persist(produto);
    }

    public Produto find(long id) {
        return em.find(Produto.class, id);
    }

    public List<Produto> findAll() {
        return em.createNamedQuery("getAllProdutos", Produto.class).getResultList();
    }
}
