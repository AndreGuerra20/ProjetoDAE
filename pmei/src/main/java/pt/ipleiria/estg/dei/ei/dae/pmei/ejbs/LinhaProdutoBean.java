package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.LinhaProduto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class LinhaProdutoBean {
    @PersistenceContext
    private EntityManager em;

    public LinhaProduto create(long produto_id, int quantidade, long volume_id) throws MyEntityNotFoundException {
        var produto = em.find(Produto.class, produto_id);
        if(produto == null) {
            throw new MyEntityNotFoundException("Produto {" + produto_id + "} not found");
        }
        var volume = em.find(Volume.class, volume_id);
        if(volume == null) {
            throw new MyEntityNotFoundException("Volume {" + volume_id + "} not found");
        }
        LinhaProduto linhaProduto = new LinhaProduto(produto, quantidade, volume);
        em.persist(linhaProduto);
        return linhaProduto;
    }

    public LinhaProduto find(long id) {
        return em.find(LinhaProduto.class, id);
    }

    public LinhaProduto getByProdutoId(long produto_id, long volume_id) {
        LinhaProduto linhaProduto = null;
        List<LinhaProduto> linhaProdutos = em.createNamedQuery("getAllLinhaProdutos", LinhaProduto.class).getResultList();
        for (LinhaProduto lp : linhaProdutos) {
            if (lp.getProduto().getId() == produto_id && lp.getVolume().getIdVolume() == volume_id) {
                linhaProduto = lp;
            }
        }
        return linhaProduto;
    }

    public List<LinhaProduto> findAll() {
        return em.createNamedQuery("getAllLinhaProdutos", LinhaProduto.class).getResultList();
    }
}
