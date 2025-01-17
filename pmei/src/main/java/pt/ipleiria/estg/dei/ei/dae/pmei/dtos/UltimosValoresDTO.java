package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class UltimosValoresDTO implements Serializable {
    private List<UltimaLeituraDTO> ultimosValores;

    public UltimosValoresDTO() {
    }

    public UltimosValoresDTO(List<UltimaLeituraDTO> ultimosValores) {
        this.ultimosValores = ultimosValores;
    }

    public List<UltimaLeituraDTO> getUltimosValores() {
        return ultimosValores;
    }

    public void setUltimosValores(List<UltimaLeituraDTO> ultimosValores) {
        this.ultimosValores = ultimosValores;
    }

    public static UltimosValoresDTO from(List<UltimaLeituraDTO> leituras) {
        return new UltimosValoresDTO(leituras);
    }
}
