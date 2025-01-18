package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDTOComQuantidade implements Serializable {
    private long id;
    private String descricao;
    private boolean precisaEmbalagemAdicional;
    private int quantidade;

    public ProdutoDTOComQuantidade() {
    }

    public ProdutoDTOComQuantidade(long id, String descricao, boolean precisaEmbalagemAdicional, int quantidade) {
        this.id = id;
        this.descricao = descricao;
        this.precisaEmbalagemAdicional = precisaEmbalagemAdicional;
        this.quantidade = quantidade;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public static ProdutoDTOComQuantidade from(Produto produto) {
        return new ProdutoDTOComQuantidade(produto.getId(), produto.getDescricao(), produto.isPrecisaEmbalagemAdicional(),0);
    }

    public static List<ProdutoDTOComQuantidade> from(List<Produto> produtos) {
        return produtos.stream().map(ProdutoDTOComQuantidade::from).collect(Collectors.toList());
    }
}
