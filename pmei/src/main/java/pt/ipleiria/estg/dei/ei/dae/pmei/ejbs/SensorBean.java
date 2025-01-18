package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.ValoresLimite;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;
import pt.ipleiria.estg.dei.ei.dae.pmei.exceptions.MyEntityExistsException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager em;

    private static final DecimalFormat dfTemperatura = new DecimalFormat("0.00");
    private static final DecimalFormat dfPressao = new DecimalFormat("0.000");
    private static final String regexPosicao = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?((1[0-7]\\d|[1-9]?\\d)(\\.\\d+)?|180(\\.0+)?)$";
    private static final Pattern pattern = Pattern.compile(regexPosicao);
    private static final DecimalFormat dfAceleracao = new DecimalFormat("0.00");

    public void create(long id,String tipo, boolean estado, long volume_id) throws MyEntityExistsException {
        var volume = em.find(Volume.class, volume_id);
        if (volume == null) {
            throw new IllegalArgumentException("Volume {" + volume_id + "} not found");
        }
        Sensor sensor = em.find(Sensor.class, id);
        if (sensor != null) {
            throw new MyEntityExistsException("Sensor with Id{" + id + "} already exists");
        }
        sensor = new Sensor(id,tipo, estado, volume);
        em.persist(sensor);
    }

    public Sensor find(long id) {
        return em.find(Sensor.class, id);
    }

    public List<Sensor> findAll() {
        List<Sensor> sensores = em.createNamedQuery("getAllSensores", Sensor.class).getResultList();
        for (Sensor sensor : sensores) {
            Hibernate.initialize(sensor.getEventos());
        }
        return sensores;
    }

    public Sensor findWithEventos(long id) {
        Sensor sensor = em.find(Sensor.class, id);
        if(sensor == null) {
            return null;
        }
        Hibernate.initialize(sensor.getEventos());
        return sensor;
    }

    public List<Sensor> findWithTipo(String tipo) {
        List<Sensor> sensores = findAll();
        List<Sensor> sensoresByTipo = new ArrayList<>();
        for (Sensor sensor : sensores) {
            if (sensor.getTipo().equals(tipo) && sensor.getStatus()) {
                sensoresByTipo.add(sensor);
            }
        }
        return sensoresByTipo;
    }

    public double getAverageValue(long id) {
        Sensor sensor = em.find(Sensor.class, id);
        return sensor.getEventos().stream().mapToDouble(e -> Double.parseDouble(e.getValor())).average().orElse(0);
    }

    public double getLowestValue(long id) {
        Sensor sensor = em.find(Sensor.class, id);
        return sensor.getEventos().stream().mapToDouble(e -> Double.parseDouble(e.getValor())).min().orElse(0);
    }

    public double getHighestValue(long id) {
        Sensor sensor = em.find(Sensor.class, id);
        return sensor.getEventos().stream().mapToDouble(e -> Double.parseDouble(e.getValor())).max().orElse(0);
    }

    public void createEvento(Sensor sensor, String valor) {
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor cannot be null");
        }
        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException("calor cannot be null or empty");
        }
        if(!sensor.getTipo().equals("Posicionamento Global") && (Float.parseFloat(valor) < ValoresLimite.MINIMO_TEMPERATURA || Float.parseFloat(valor) > ValoresLimite.MAXIMO_TEMPERATURA)) {
            throw new IllegalArgumentException("Valor fora dos limites");
        }
        switch (sensor.getTipo()){
            case "Temperatura":
                try {
                    valor = dfTemperatura.format(Double.parseDouble(valor));
                    double f = Double.parseDouble(valor);
                    if (f < ValoresLimite.MINIMO_TEMPERATURA || f > ValoresLimite.MAXIMO_TEMPERATURA) {
                        throw new IllegalArgumentException("Invalid temperature, value must be between " + ValoresLimite.MINIMO_TEMPERATURA + " and " + ValoresLimite.MAXIMO_TEMPERATURA);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid temperature, value must be a number");
                }
                break;
            case "Pressao":
                try {
                    valor = dfPressao.format(Double.parseDouble(valor));
                    double f = Double.parseDouble(valor);
                    if (f < ValoresLimite.MINIMO_PRESSAO || f > ValoresLimite.MAXIMO_PRESSAO) {
                        throw new IllegalArgumentException("Invalid pressure, value must be between " + ValoresLimite.MINIMO_PRESSAO + " and " + ValoresLimite.MAXIMO_PRESSAO);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid pressure,value must be a number");
                }
                break;
            case "Posicionamento Global":
                Matcher matcher = pattern.matcher(valor);
                if (!matcher.matches()) {
                    throw new IllegalArgumentException("Invalid location, value must be in the format 'latitude, longitude'");
                }
                break;
            case "Aceleracao":
                try {
                    valor = dfAceleracao.format(Double.parseDouble(valor));
                    double f = Double.parseDouble(valor);
                    if (f < ValoresLimite.MINIMO_ACELERACAO || f > ValoresLimite.MAXIMO_ACELERACAO) {
                        throw new IllegalArgumentException("Invalid acceleration, value must be between " + ValoresLimite.MINIMO_ACELERACAO + " and " + ValoresLimite.MAXIMO_ACELERACAO);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid acceleration, value must be a number");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid sensor type");
        }
        Evento evento = new Evento(valor, sensor);
        sensor.addEvento(evento);
        em.merge(evento);
    }
}
