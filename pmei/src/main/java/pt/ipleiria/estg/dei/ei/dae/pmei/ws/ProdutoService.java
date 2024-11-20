package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.ProdutoBean;

import java.util.List;

@Path("produto")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProdutoService {
    @EJB
    private ProdutoBean produtoBean;

    @GET
    @Path("/")
    public List<ProdutoDTO> getProdutos() {
        return ProdutoDTO.from(produtoBean.findAll());
    }

    @GET
    @Path("{id}")
    public ProdutoDTO getProduto(@PathParam("id") long id) {
        return ProdutoDTO.from(produtoBean.find(id));
    }
}
