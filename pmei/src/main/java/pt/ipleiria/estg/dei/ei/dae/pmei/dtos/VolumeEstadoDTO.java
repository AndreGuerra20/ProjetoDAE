package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import java.io.Serializable;

public class VolumeEstadoDTO implements Serializable {

    private String estado;

    public VolumeEstadoDTO() {
    }

    public VolumeEstadoDTO(String estado) {

        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
