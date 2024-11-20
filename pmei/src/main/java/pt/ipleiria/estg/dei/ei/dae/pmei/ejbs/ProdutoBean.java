package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.util.List;

@Stateless
public class ProdutoBean {
    @PersistenceContext
    private EntityManager em;

    public void create(int stock, long volume_id) {
        var volume = em.find(Volume.class, volume_id);
        if(volume == null) {
            throw new IllegalArgumentException("Volume {" + volume_id + "} not found");
        }
        Produto produto = new Produto(stock, volume);
        em.persist(produto);
    }

    public Produto find(long id) {
        return em.find(Produto.class, id);
    }

    public List<Produto> findAll() {
        return em.createNamedQuery("getAllProdutos", Produto.class).getResultList();
    }
}
