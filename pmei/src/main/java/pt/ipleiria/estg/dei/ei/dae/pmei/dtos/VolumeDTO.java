package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeDTO implements Serializable {
    private long idVolume;
    private String tipoEmbalagem;
    private List<LinhaProdutoDTO> produtos = new ArrayList<>();
    private List<SensorDTO> sensores = new ArrayList<>();
    private boolean isEntregue;

    public VolumeDTO() {
    }

    public VolumeDTO(long id, String tipoEmbalagem, List<LinhaProdutoDTO> produtos, List<SensorDTO> sensores, boolean isEntregue) {
        this.idVolume = id;
        this.tipoEmbalagem = tipoEmbalagem;
        this.produtos = produtos;
        this.sensores = sensores;
        this.isEntregue = isEntregue;
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

    public List<LinhaProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<LinhaProdutoDTO> produtos) {
        this.produtos = produtos;
    }

    public List<SensorDTO> getSensores() {
        return sensores;
    }

    public void setSensores(List<SensorDTO> sensores) {
        this.sensores = sensores;
    }

    public boolean isEntregue() {
        return isEntregue;
    }

    public void setEntregue(boolean entregue) {
        isEntregue = entregue;
    }

    public static VolumeDTO from(Volume volume) {
        return new VolumeDTO(volume.getIdVolume(), volume.getTipoEmbalagem(), volume.getProdutos().stream().map(LinhaProdutoDTO::from).collect(Collectors.toList()), volume.getSensores().stream().map(SensorDTO::from).collect(Collectors.toList()), volume.isEntregue());
    }

    public static List<VolumeDTO> from(List<Volume> volumes) {
        return volumes.stream().map(VolumeDTO::from).collect(Collectors.toList());
    }
}
