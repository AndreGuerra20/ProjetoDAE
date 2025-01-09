package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import java.io.Serializable;

public class EventoDTOValor implements Serializable {
    private String valor;

    public EventoDTOValor() {
    }

    public EventoDTOValor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
