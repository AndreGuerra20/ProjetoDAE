package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;


import java.io.Serializable;

public class SensorEventoDTO implements Serializable {
    private long id;
    private double valor;

    public SensorEventoDTO() {
    }

    public SensorEventoDTO(long id, double valor) {
        this.id = id;
        this.valor = valor;
    }

    // Getters e setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
