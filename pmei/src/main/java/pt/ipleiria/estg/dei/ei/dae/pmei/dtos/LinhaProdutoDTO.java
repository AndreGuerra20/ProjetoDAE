package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.LinhaProduto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class LinhaProdutoDTO implements Serializable {
    private long id;
    private int quantidade;
    private long volumeId;
    private String descricao;

    public LinhaProdutoDTO() {
    }

    public LinhaProdutoDTO(long id, int quantidade, long volumeId, String descricao) {
        this.id = id;
        this.quantidade = quantidade;
        this.volumeId = volumeId;
        this.descricao = descricao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public long getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(long volumeId) {
        this.volumeId = volumeId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static LinhaProdutoDTO from(LinhaProduto linhaProduto) {
        return new LinhaProdutoDTO(linhaProduto.getProduto().getId(), linhaProduto.getQuantidade(), linhaProduto.getVolume().getIdVolume(), linhaProduto.getProduto().getDescricao());
    }

    public static List<LinhaProdutoDTO> from(List<LinhaProduto> linhaProdutos) {
        return linhaProdutos.stream().map(LinhaProdutoDTO::from).collect(Collectors.toList());
    }
}
