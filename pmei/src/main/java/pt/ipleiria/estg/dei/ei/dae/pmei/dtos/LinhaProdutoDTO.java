package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.LinhaProduto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class LinhaProdutoDTO implements Serializable {
    private long id;
    private int quantidade;
    private long volumeId;

    public LinhaProdutoDTO() {
    }

    public LinhaProdutoDTO(long id, int quantidade, long volumeId) {
        this.id = id;
        this.quantidade = quantidade;
        this.volumeId = volumeId;
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

    public static LinhaProdutoDTO from(LinhaProduto linhaProduto) {
        return new LinhaProdutoDTO(linhaProduto.getProduto().getId(), linhaProduto.getQuantidade(), linhaProduto.getVolume().getIdVolume());
    }

    public static List<LinhaProdutoDTO> from(List<LinhaProduto> linhaProdutos) {
        return linhaProdutos.stream().map(LinhaProdutoDTO::from).collect(Collectors.toList());
    }
}
