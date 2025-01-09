package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

@Singleton
@Startup
public class ConfigBean {

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private LogisticaBean logisticaBean;

    @EJB
    private GestorBean gestorBean;

    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private ProdutoBean produtoBean;

    @EJB
    private LinhaProdutoBean linhaProdutoBean;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private EventoBean eventoBean;

    private static final Logger logger = Logger.getLogger("ConfigBean.logger");

    @PostConstruct
    public void populateDB() {
        try {
            clienteBean.create("João", 123456789, "joao", "123");
            logisticaBean.create("Henrique", "FUNC1", "henri", "123");
            gestorBean.create("Andre", "FUNC2", "andre", "123");

            encomendaBean.create(clienteBean.find("joao").getId(), "Pendente", new ArrayList<>());

            volumeBean.create("Caixa Isotérmica", 1);

            produtoBean.create("Peixe", true);
            produtoBean.create("Comando Remoto", false);

            linhaProdutoBean.create(1, 2, 1);
            linhaProdutoBean.create(2, 1, 1);

            sensorBean.create("Temperatura", true, 1);
            sensorBean.create("Pressao", true, 1);
            sensorBean.create("Posicionamento Global", true, 1);
            sensorBean.create("Aceleracao", true, 1);

            eventoBean.create("25", 1);
            sleep(1000);
            eventoBean.create("24.3", 1);
            sleep(1000);
            eventoBean.create("26", 1);
            sleep(1000);
            eventoBean.create("25.3", 1);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }
}
