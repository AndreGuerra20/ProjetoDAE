package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;


import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {
    private long idVolume;
    private String tipoEmbalagem;
    private List<ProdutoDTO> produtos = new ArrayList<>();
    private List<SensorDTO> sensores = new ArrayList<>();

    public VolumeDTO() {
    }

    public VolumeDTO(long id, String tipoEmbalagem, List<ProdutoDTO> produtos, List<SensorDTO> sensores) {
        this.idVolume = id;
        this.tipoEmbalagem = tipoEmbalagem;
        this.produtos = produtos;
        this.sensores = sensores;
    }

    public long getIdVolume() {
        return idVolume;
    }

    public void setIdVolume(long idVolume) {
        this.idVolume = idVolume;
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

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDTO> sensores) {
        this.sensores = sensores;
    }

    public static VolumeDTO from(Volume volume) {
        return new VolumeDTO(
                volume.getIdVolume(),
                volume.getTipoEmbalagem(),
                volume.getProdutos().stream().map(ProdutoDTO::from).collect(Collectors.toList()),
                volume.getSensores().stream().map(SensorDTO::from).collect(Collectors.toList())
        );
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }
}
