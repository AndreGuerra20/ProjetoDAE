package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.LinhaProduto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.util.List;

@Stateless
public class LinhaProdutoBean {
    @PersistenceContext
    private EntityManager em;

    public void create(long produto_id, int quantidade, long volume_id) {
        var produto = em.find(Produto.class, produto_id);
        if(produto == null) {
            throw new IllegalArgumentException("Produto {" + produto_id + "} not found");
        }
        var volume = em.find(Volume.class, volume_id);
        if(volume == null) {
            throw new IllegalArgumentException("Volume {" + volume_id + "} not found");
        }
        LinhaProduto linhaProduto = new LinhaProduto(produto, quantidade, volume);
        em.persist(linhaProduto);
    }

    public LinhaProduto find(long id) {
        return em.find(LinhaProduto.class, id);
    }

    public List<LinhaProduto> findAll() {
        return em.createNamedQuery("getAllLinhaProdutos", LinhaProduto.class).getResultList();
    }
}
