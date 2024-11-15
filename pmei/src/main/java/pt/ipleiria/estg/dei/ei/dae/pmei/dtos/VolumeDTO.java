package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;


import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {
    private long id;
    private String tipoEmbalagem;
    private EncomendaDTO encomenda;
    private List<ProdutoDTO> produtos = new ArrayList<>();
    private List<SensorDTO> sensores = new ArrayList<>();

    public VolumeDTO() {
    }

    public VolumeDTO(long id, String tipoEmbalagem, EncomendaDTO encomenda) {
        this.id = id;
        this.tipoEmbalagem = tipoEmbalagem;
        this.encomenda = encomenda;
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

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }

    public EncomendaDTO getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(EncomendaDTO encomenda) {
        this.encomenda = encomenda;
    }

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDTO> sensores) {
        this.sensores = sensores;
    }

    public static VolumeDTO from(Volume volume) {
        return new VolumeDTO(
                volume.getId(),
                volume.getTipoEmbalagem(),
                EncomendaDTO.from(volume.getEncomenda())
        );
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }
}
