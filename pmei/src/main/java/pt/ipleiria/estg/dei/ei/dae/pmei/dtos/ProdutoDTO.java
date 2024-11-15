package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDTO implements Serializable {
    private long id;
    private int quantidade;
    private VolumeDTO volume;

    public ProdutoDTO() {
    }

    public ProdutoDTO(long id, int quantidade, VolumeDTO volume) {
        this.id = id;
        this.quantidade = quantidade;
        this.volume = volume;
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

    public VolumeDTO getVolume() {
        return volume;
    }

    public void setVolume(VolumeDTO volume) {
        this.volume = volume;
    }

    public static ProdutoDTO from(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getQuantidade(),
                VolumeDTO.from(produto.getVolume())
        );
    }

    public static List<ProdutoDTO> from(List<Produto> produtos) {
        return produtos.stream().map(ProdutoDTO::from).collect(Collectors.toList());
    }
}
