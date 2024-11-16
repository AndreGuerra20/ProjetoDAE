package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;


import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {
    private long id;
    private String tipoEmbalagem;
    private List<ProdutoDTO> produtos = new ArrayList<>();
    private List<SensorDTO> sensores = new ArrayList<>();

    public VolumeDTO() {
    }

    public VolumeDTO(long id, String tipoEmbalagem) {
        this.id = id;
        this.tipoEmbalagem = tipoEmbalagem;
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

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDTO> sensores) {
        this.sensores = sensores;
    }

    public static VolumeDTO from(Volume volume) {
        VolumeDTO volumeDTO = new VolumeDTO(
                volume.getId(),
                volume.getTipoEmbalagem()
        );
        volumeDTO.setProdutos(ProdutoDTO.from(volume.getProdutos()));
        volumeDTO.setSensores(SensorDTO.from(volume.getSensores()));
        return volumeDTO;
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }
}
