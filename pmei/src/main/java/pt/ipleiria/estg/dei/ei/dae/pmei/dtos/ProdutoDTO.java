package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import jakarta.persistence.Id;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDTO implements Serializable {
    @Id
    private long id;
    private int quantidade;
    private long volumeId;

    public ProdutoDTO() {
    }

    public ProdutoDTO(long id, int quantidade, long volumeId) {
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

    public static ProdutoDTO from(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getQuantidade(),
                produto.getVolume().getId()
        );
    }

    public static List<ProdutoDTO> from(List<Produto> produtos) {
        return produtos.stream().map(ProdutoDTO::from).collect(Collectors.toList());
    }
}
