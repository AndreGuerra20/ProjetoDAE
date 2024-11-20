package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

@Singleton
@Startup
public class ConfigBean {

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private ProdutoBean produtoBean;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private EventoBean eventoBean;

    private static final Logger logger = Logger.getLogger("ConfigBean.logger");

    @PostConstruct
    public void populateDB() throws InterruptedException {
        clienteBean.create("João", 123456789);

        encomendaBean.create(clienteBean.find(1).getId(), "Pendente");

        volumeBean.create("Caixa Isotérmica", encomendaBean.find(1).getId());

        produtoBean.create(2, volumeBean.find(1).getId());
        produtoBean.create(3, volumeBean.find(1).getId());

        sensorBean.create("Temperatura", true, volumeBean.find(1).getId());

        eventoBean.create("25", sensorBean.find(1).getId());
        sleep(1000);
        eventoBean.create("24.3", sensorBean.find(1).getId());
        sleep(1000);
        eventoBean.create("26", sensorBean.find(1).getId());
        sleep(1000);
        eventoBean.create("25.3", sensorBean.find(1).getId());

    }
}
