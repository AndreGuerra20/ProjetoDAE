package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger("ConfigBean.logger");

    @PostConstruct
    public void populateDB() {
        clienteBean.create(1, "João", 123456789);

        encomendaBean.create(1, clienteBean.find(1), "Pendente");

        volumeBean.create(1, "Caixa Isotérmica");
        volumeBean.create(2, "Caixa Cartão");

        produtoBean.create(1, 10);
        produtoBean.create(2, 20);
        volumeBean.find(1).addProduto(produtoBean.find(1));
        volumeBean.find(2).addProduto(produtoBean.find(2));

        sensorBean.create(1, "Temperatura", true);
        volumeBean.find(1).addSensor(sensorBean.find(1));

        encomendaBean.find(1).addVolume(volumeBean.find(1));
        encomendaBean.find(1).addVolume(volumeBean.find(2));


//        System.out.println(">>> Encomenda 1 - " + encomendaBean.find(1));
        logger.log(Level.WARNING, ">>> Encomenda 1 - " + encomendaBean.find(1));
    }
}
