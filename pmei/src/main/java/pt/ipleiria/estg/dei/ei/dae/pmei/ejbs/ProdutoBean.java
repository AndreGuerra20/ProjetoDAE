package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

@Stateless
public class ProdutoBean {
    @PersistenceContext
    private EntityManager em;

    public void create(int stock, Volume volume) {
        Produto produto = new Produto(stock);
        produto.setVolume(volume);
        em.persist(produto);
    }

    public Produto find(long id) {
        return em.find(Produto.class, id);
    }
}
