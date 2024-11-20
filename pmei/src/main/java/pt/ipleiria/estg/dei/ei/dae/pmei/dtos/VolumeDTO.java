package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;


import jakarta.persistence.Id;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Produto;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {
    @Id
    private long id;
    private String tipoEmbalagem;
    private long encomendaId;
    private List<Long> lista_produtos = new ArrayList<>();
    private List<Long> lista_sensores = new ArrayList<>();

    public VolumeDTO() {
    }

    public VolumeDTO(long id, String tipoEmbalagem, long encomendaId, List<Long> lista_produtos, List<Long> lista_sensores) {
        this.id = id;
        this.tipoEmbalagem = tipoEmbalagem;
        this.encomendaId = encomendaId;
        this.lista_produtos = lista_produtos;
        this.lista_sensores = lista_sensores;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(String tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    public long getEncomendaId() {
        return encomendaId;
    }

    public void setEncomendaId(long encomendaId) {
        this.encomendaId = encomendaId;
    }

    public List<Long> getLista_produtos() {
        return lista_produtos;
    }

    public void setLista_produtos(List<Long> lista_produtos) {
        this.lista_produtos = lista_produtos;
    }

    public List<Long> getLista_sensores() {
        return lista_sensores;
    }

    public void setLista_sensores(List<Long> lista_sensores) {
        this.lista_sensores = lista_sensores;
    }

    public static VolumeDTO from(Volume volume) {
        return new VolumeDTO(
                volume.getId(),
                volume.getTipoEmbalagem(),
                volume.getEncomenda().getId(),
                volume.getProdutos().stream().map(Produto::getId).collect(Collectors.toList()),
                volume.getSensores().stream().map(Sensor::getId).collect(Collectors.toList())
        );
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }
}
