package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDTO implements Serializable {
    private long id;
    private String descricao;
    private boolean precisaEmbalagemAdicional;

    public ProdutoDTO() {
    }

    public ProdutoDTO(long id, String descricao, boolean precisaEmbalagemAdicional) {
        this.id = id;
        this.descricao = descricao;
        this.precisaEmbalagemAdicional = precisaEmbalagemAdicional;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isPrecisaEmbalagemAdicional() {
        return precisaEmbalagemAdicional;
    }

    public void setPrecisaEmbalagemAdicional(boolean precisaEmbalagemAdicional) {
        this.precisaEmbalagemAdicional = precisaEmbalagemAdicional;
    }

    public static ProdutoDTO from(Produto produto) {
        return new ProdutoDTO(produto.getId(), produto.getDescricao(), produto.isPrecisaEmbalagemAdicional());
    }

    public static List<ProdutoDTO> from(List<Produto> produtos) {
        return produtos.stream().map(ProdutoDTO::from).collect(Collectors.toList());
    }
}
