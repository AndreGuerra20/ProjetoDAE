package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.pmei.entities.Evento;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
    private static final String CSV_FILE = "/products.csv";

    @PostConstruct
    public void populateDB() {
        try {
            clienteBean.create("João", 123456789, "joao", "123");
            clienteBean.create("Manel", 123456787, "manel", "123");
            logisticaBean.create("Henrique", "FUNC1", "henri", "123");
            gestorBean.create("Andre", "FUNC2", "andre", "123");

            encomendaBean.create(1,clienteBean.find("joao").getId(), "Pendente", new ArrayList<>());

            volumeBean.create(1,"Caixa Isotérmica", 1);
            volumeBean.create(2,"Caixa", 1);

            produtoBean.create(1,"Peixe", true);
            produtoBean.create(2,"Comando Remoto", true);

            try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE))) {
                csvReader.readNext();

                String[] linha;
                int i = 2;
                while ((linha = csvReader.readNext()) != null) {
                    i++;
                    String descricao = linha[0];
                    int precisaEmbalagem = Integer.parseInt(linha[1]);
                    if (i == 500){
                        break;
                    }
                    if (produtoBean.findAll().size()%100 == 0){
                        System.out.println("Foram inseridos +100 produtos");
                    }
                    produtoBean.create(i, descricao, precisaEmbalagem == 1);
                }
            } catch (IOException | CsvValidationException e) {
                System.out.println("Ficheiro não encontrado");
            }


            linhaProdutoBean.create(1, 2, 1);
            linhaProdutoBean.create(2, 1, 2);

            sensorBean.create(1,"Temperatura", true, 1);
            sensorBean.create(2,"Temperatura", true, 2);
            sensorBean.create(3,"Pressao", true, 2);
            sensorBean.create(4,"Posicionamento Global", true, 1);
            sensorBean.create(8,"Posicionamento Global", true, 1);
            sensorBean.create(5,"Aceleracao", true, 1);
            sensorBean.create(6,"Posicionamento Global", true, 2);

            eventoBean.create("25", 1);
            eventoBean.create("24.3", 1);
            eventoBean.create("26", 1);
            eventoBean.create("25.3", 1);

            Evento evento = null;
            long timestamp = new Date().getTime();

            evento = eventoBean.create("39.74335365871343,-8.794312605631447", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.743001743412144,-8.79325771624433", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.74242426788271,-8.793574216908228", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.74153329614267,-8.793354275758379", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.740893934335354,-8.793198707633433", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73949969250403,-8.793402555517487", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73707600966279,-8.794055719074873", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.736167169036,-8.795517257114422", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73566764848646,-8.797222384856108", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73548726517331,-8.798918490731804", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73458534152421,-8.803014406017748", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.734127437316886,-8.804241376244569", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.733620963948425,-8.807047168411684", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73350431519367,-8.808491475626528", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73331669891445,-8.809334971717563", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73154221087,-8.80995761749507", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73006546901546,-8.810110723191576", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.729511069395805,-8.810710387183958", 4);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.77113452695224,-8.77589736436629", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.77119336589832,-8.77737738613472", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.77071284636706,-8.7796867304458", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76972237714301,-8.78003121826916", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76771197500298,-8.779278448576422", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76592708096547,-8.779240172154449", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76462270609049,-8.781626069315621", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76319080741687,-8.788222373150852", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.7629456163095,-8.790774134742515", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.762494462391516,-8.79513764716253", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76177849465753,-8.799207706954833", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76085655266007,-8.803788119111815", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.75953246539975,-8.807705073194033", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.75719808662091,-8.812285485329665", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.75420643515213,-8.816164163036705", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.75151874232512,-8.817516596696722", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.749605904379436,-8.818218331144275", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.745939664640076,-8.819245937946379", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.742474709810196,-8.819443185190098", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.74010630711908,-8.819701123904203", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73758614390503,-8.822174300953293", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.735181615142885,-8.82349849975027", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73460819940431,-8.824013483888022", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.7339275198208,-8.823246372112129", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73344897744597,-8.822988880043251", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.7328837981031,-8.822661650547817", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73262389656611,-8.822345149879824", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.732640398280125,-8.821443927638756", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73272290679097,-8.820907485828597", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73343660130284,-8.82134200369877", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73409665900888,-8.821915996438653", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73461232468722,-8.821143520232026", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73484867524408,-8.820904501348142", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.735989222463374,-8.821158646850703", 6);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            eventoBean.create("39.74335365871343,-8.794312605631447", 8);
            eventoBean.create("38.74335365871343,-8.794312605631447", 8);
            eventoBean.create("37.74335365871343,-8.794312605631447", 8);
            eventoBean.create("36.74335365871343,-8.794312605631447", 8);

            eventoBean.create("40", 5);

            eventoBean.create("21", 2);
            eventoBean.create("22", 2);
            eventoBean.create("23", 2);
            eventoBean.create("24", 2);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }
}
