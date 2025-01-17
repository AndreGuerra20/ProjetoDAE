package pt.ipleiria.estg.dei.ei.dae.pmei.ws;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.ipleiria.estg.dei.ei.dae.pmei.dtos.ProdutoDTO;
import pt.ipleiria.estg.dei.ei.dae.pmei.ejbs.ProdutoBean;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.security.Authenticated;

import java.util.List;

@Path("produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class ProdutoService {
    @EJB
    private ProdutoBean produtoBean;

    @GET
    @Path("/")
    public List<ProdutoDTO> getProdutos() {
        List<ProdutoDTO> listaProdutos = ProdutoDTO.from(produtoBean.findAll());
        if (listaProdutos.isEmpty()) {
            return null;
        }
        return listaProdutos;
    }

//    @GET
//    @Path("{id}")
//    public Response getProduto(@PathParam("id") long id) {
//        Produto produto = produtoBean.find(id);
//        if (produto == null) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        return Response.ok(ProdutoDTO.from(produto)).build();
//    }
}
