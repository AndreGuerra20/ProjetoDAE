package pt.ipleiria.estg.dei.ei.dae.pmei;

public class ValoresLimite {
    public static float MINIMO_TEMPERATURA = 0;
    public static float MAXIMO_TEMPERATURA = 100;
    public static float MINIMO_PRESSAO = 0;
    public static float MAXIMO_PRESSAO = 1000;
    public static float MINIMO_ACELERACAO = 0;
    public static float MAXIMO_ACELERACAO = 100;

    public ValoresLimite() {
    }

    public ValoresLimite(float MINIMO_TEMPERATURA, float MAXIMO_TEMPERATURA, float MINIMO_PRESSAO, float MAXIMO_PRESSAO, float MINIMO_ACELERACAO, float MAXIMO_ACELERACAO) {
        ValoresLimite.MINIMO_TEMPERATURA = MINIMO_TEMPERATURA;
        ValoresLimite.MAXIMO_TEMPERATURA = MAXIMO_TEMPERATURA;
        ValoresLimite.MINIMO_PRESSAO = MINIMO_PRESSAO;
        ValoresLimite.MAXIMO_PRESSAO = MAXIMO_PRESSAO;
        ValoresLimite.MINIMO_ACELERACAO = MINIMO_ACELERACAO;
        ValoresLimite.MAXIMO_ACELERACAO = MAXIMO_ACELERACAO;
    }

    public float getMINIMO_TEMPERATURA() {
        return MINIMO_TEMPERATURA;
    }

    public void setMINIMO_TEMPERATURA(float MINIMO_TEMPERATURA) {
        ValoresLimite.MINIMO_TEMPERATURA = MINIMO_TEMPERATURA;
    }

    public float getMAXIMO_TEMPERATURA() {
        return MAXIMO_TEMPERATURA;
    }

    public void setMAXIMO_TEMPERATURA(float MAXIMO_TEMPERATURA) {
        ValoresLimite.MAXIMO_TEMPERATURA = MAXIMO_TEMPERATURA;
    }

    public float getMINIMO_PRESSAO() {
        return MINIMO_PRESSAO;
    }

    public void setMINIMO_PRESSAO(float MINIMO_PRESSAO) {
        ValoresLimite.MINIMO_PRESSAO = MINIMO_PRESSAO;
    }

    public float getMAXIMO_PRESSAO() {
        return MAXIMO_PRESSAO;
    }

    public void setMAXIMO_PRESSAO(float MAXIMO_PRESSAO) {
        ValoresLimite.MAXIMO_PRESSAO = MAXIMO_PRESSAO;
    }

    public float getMINIMO_ACELERACAO() {
        return MINIMO_ACELERACAO;
    }

    public void setMINIMO_ACELERACAO(float MINIMO_ACELERACAO) {
        ValoresLimite.MINIMO_ACELERACAO = MINIMO_ACELERACAO;
    }

    public float getMAXIMO_ACELERACAO() {
        return MAXIMO_ACELERACAO;
    }

    public void setMAXIMO_ACELERACAO(float MAXIMO_ACELERACAO) {
        ValoresLimite.MAXIMO_ACELERACAO = MAXIMO_ACELERACAO;
    }
}
