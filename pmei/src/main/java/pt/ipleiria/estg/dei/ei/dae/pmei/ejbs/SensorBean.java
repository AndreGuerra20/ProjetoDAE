package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Volume;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.*;

@Stateless
public class SensorBean {
    @PersistenceContext
    private EntityManager em;

    public static final double MAX_TEMPERATURE = 60.00;
    public static final double MIN_TEMPERATURE = -30.00;
    public static final double MAX_ACCELERATION = 100;
    public static final double MIN_ACCELERATION = 0.1;

    private static final DecimalFormat dfTemperatura = new DecimalFormat("0.00");
    private static final DecimalFormat dfPressao = new DecimalFormat("0.000");
    private static final String regex = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?((1[0-7]\\d|[1-9]?\\d)(\\.\\d+)?|180(\\.0+)?)$";
    private static final Pattern pattern = Pattern.compile(regex);
    private static final DecimalFormat dfAceleracao = new DecimalFormat("0.00");

    public void create(String tipo, boolean estado, long volume_id) {
        var volume = em.find(Volume.class, volume_id);
        if (volume == null) {
            throw new IllegalArgumentException("Volume {" + volume_id + "} not found");
        }
        Sensor sensor = new Sensor(tipo, estado, volume);
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
        Hibernate.initialize(sensor.getEventos());
        return sensor;
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

    public Evento createEvento(Sensor sensor, String valor) {
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor cannot be null");
        }
        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException("calor cannot be null or empty");
        }
        switch (sensor.getTipo()){
            case "Temperatura":
                try {
                    valor = dfTemperatura.format(Double.parseDouble(valor));
                    double f = Double.parseDouble(valor);
                    if (f < MIN_TEMPERATURE || f > MAX_TEMPERATURE) {
                        throw new IllegalArgumentException("Invalid temperature, value must be between " + MIN_TEMPERATURE + " and " + MAX_TEMPERATURE);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid temperature, value must be a number");
                }
                break;
            case "Pressao":
                try {
                    valor = dfPressao.format(Double.parseDouble(valor));
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
                    if (f < MIN_ACCELERATION || f > MAX_ACCELERATION) {
                        throw new IllegalArgumentException("Invalid acceleration, value must be between " + MIN_ACCELERATION + " and " + MAX_ACCELERATION);
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
        em.merge(sensor);
        em.merge(evento);
        return evento;
    }
}
