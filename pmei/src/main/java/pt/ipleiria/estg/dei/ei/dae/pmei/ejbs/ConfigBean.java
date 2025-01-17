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

    public static boolean development = false;

    private static final Logger logger = Logger.getLogger("ConfigBean.logger");
    private static final String CSV_FILE = "/products.csv";
    private static final String CSV_FILE_DIRECOES = "/direcoes.csv";
    private static final String CSV_FILE_DIRECOES2 = "/direcoes2.csv";

    @PostConstruct
    public void populateDB() {
        try {

            //Criar utilizadores
            clienteBean.create("João", 123456789, "joao", "123", "joao@mail.pt");
            clienteBean.create("Manel", 123456787, "manel", "123", "manel@mail.pt");
            clienteBean.create("Joana Vasconcelos", 250624499, "joana", "123", "joana@mail.pt");
            clienteBean.create("Maria", 123456788, "maria", "123", "maria@mail.pt");
            clienteBean.create("Carlos Ferreira",8764321,"carlos","123","carlos.j.ferreira@ipleiria.pt");

            logisticaBean.create("Henrique", "FUNC1", "henri", "123","henrique@mail.pt");

            gestorBean.create("Andre", "FUNC2", "andre", "123","andre@mail.pt");
            gestorBean.create("Daniela", "FUNC3", "daniela", "123","daniela@mail.pt");

            //Criar encomendas
            encomendaBean.create(1,clienteBean.find("joao").getId(), "Pendente", new ArrayList<>());
            encomendaBean.create(2,clienteBean.find("joao").getId(), "Pendente", new ArrayList<>());
            encomendaBean.create(3,clienteBean.find("carlos").getId(), "Pendente", new ArrayList<>());
            encomendaBean.create(4,clienteBean.find("carlos").getId(), "Pendente", new ArrayList<>());
            encomendaBean.create(5,clienteBean.find("carlos").getId(), "Pendente", new ArrayList<>());
            encomendaBean.create(6,clienteBean.find("carlos").getId(), "Pendente", new ArrayList<>());

            //Criar volumes
            volumeBean.create(1,"Caixa Isotérmica", 1);
            volumeBean.create(2,"Caixa", 1);
            volumeBean.create(3,"Caixa Isotérmica", 1);
            volumeBean.create(4,"Caixa", 2);
            volumeBean.create(5,"Caixa Isotérmica", 3);
            volumeBean.create(6,"Caixa Cartão", 3);
            volumeBean.create(7,"Contentor", 3);
            volumeBean.create(8,"", 4);
            volumeBean.create(9,"Caixa Cartão", 5);
            volumeBean.create(10,"Caixa Cartão", 6);

            //Criar produtos
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

            //Criar linhas de produtos
            linhaProdutoBean.create(1, 2, 1);
            linhaProdutoBean.create(2, 1, 2);
            linhaProdutoBean.create(32, 2, 3);
            linhaProdutoBean.create(34, 3, 3);
            linhaProdutoBean.create(116, 3, 4);
            linhaProdutoBean.create(157, 12, 4);
            linhaProdutoBean.create(158, 2, 4);
            linhaProdutoBean.create(146, 1, 5);
            linhaProdutoBean.create(145, 2, 5);
            linhaProdutoBean.create(150, 4, 5);
            linhaProdutoBean.create(173, 3, 5);
            linhaProdutoBean.create(94, 2, 5);
            linhaProdutoBean.create(100, 2, 6);
            linhaProdutoBean.create(4, 1, 7);
            linhaProdutoBean.create(61, 2, 8);
            linhaProdutoBean.create(74, 12, 9);
            linhaProdutoBean.create(26, 3, 10);

            sensorBean.create(1,"Temperatura", true, 1);
            sensorBean.create(2,"Temperatura", true, 2);
            sensorBean.create(3,"Pressao", true, 2);
            sensorBean.create(4,"Posicionamento Global", true, 1);
            sensorBean.create(5,"Posicionamento Global", true, 1);
            sensorBean.create(6,"Aceleracao", true, 1);
            sensorBean.create(7,"Posicionamento Global", true, 2);
            sensorBean.create(222,"Posicionamento Global", true, 4);
            sensorBean.create(223,"Temperatura", true, 4);
            sensorBean.create(8,"Temperatura", true, 5);
            sensorBean.create(9,"Posicionamento Global", true, 5);
            sensorBean.create(10,"Posicionamento Global", true, 7);
            sensorBean.create(11,"Posicionamento Global", true, 7);

            //Eventos

            // Eliminar dps só pra testar frontend
            if (development) {
                sensorBean.create(100000,"Posicionamento Global", true, 1);
                sensorBean.create(100001,"Posicionamento Global", true, 1);
                sensorBean.create(100002,"Posicionamento Global", true, 1);
                sensorBean.create(100003,"Posicionamento Global", true, 1);
                sensorBean.create(100004,"Posicionamento Global", true, 1);
                sensorBean.create(100005,"Posicionamento Global", true, 1);
                sensorBean.create(100006,"Posicionamento Global", true, 1);
                sensorBean.create(100007,"Posicionamento Global", true, 1);
                sensorBean.create(100008,"Posicionamento Global", true, 1);

                eventoBean.create("39.74335365871343,-8.794312605631447", 100000);
                eventoBean.create("38.74335365871343,-8.794312605631447", 100000);
                eventoBean.create("37.74335365871343,-8.794312605631447", 100000);
                eventoBean.create("36.74335365871343,-8.794312605631447", 100000);

                eventoBean.create("39.74335365871343,-7.794312605631447", 100001);
                eventoBean.create("38.74335365871343,-7.794312605631447", 100001);
                eventoBean.create("37.74335365871343,-7.794312605631447", 100001);
                eventoBean.create("36.74335365871343,-7.794312605631447", 100001);

                eventoBean.create("39.74335365871343,-6.794312605631447", 100002);
                eventoBean.create("38.74335365871343,-6.794312605631447", 100002);
                eventoBean.create("37.74335365871343,-6.794312605631447", 100002);
                eventoBean.create("36.74335365871343,-6.794312605631447", 100002);

                eventoBean.create("39.74335365871343,-5.794312605631447", 100003);
                eventoBean.create("38.74335365871343,-5.794312605631447", 100003);
                eventoBean.create("37.74335365871343,-5.794312605631447", 100003);
                eventoBean.create("36.74335365871343,-5.794312605631447", 100003);

                eventoBean.create("39.74335365871343,-4.794312605631447", 100004);
                eventoBean.create("38.74335365871343,-4.794312605631447", 100004);
                eventoBean.create("37.74335365871343,-4.794312605631447", 100004);
                eventoBean.create("36.74335365871343,-4.794312605631447", 100004);

                eventoBean.create("39.74335365871343,-3.794312605631447", 100005);
                eventoBean.create("38.74335365871343,-3.794312605631447", 100005);
                eventoBean.create("37.74335365871343,-3.794312605631447", 100005);
                eventoBean.create("36.74335365871343,-3.794312605631447", 100005);

                eventoBean.create("39.74335365871343,-2.794312605631447", 100006);
                eventoBean.create("38.74335365871343,-2.794312605631447", 100006);
                eventoBean.create("37.74335365871343,-2.794312605631447", 100006);
                eventoBean.create("36.74335365871343,-2.794312605631447", 100006);

                eventoBean.create("39.74335365871343,-1.794312605631447", 100007);
                eventoBean.create("38.74335365871343,-1.794312605631447", 100007);
                eventoBean.create("37.74335365871343,-1.794312605631447", 100007);
                eventoBean.create("36.74335365871343,-1.794312605631447", 100007);

                eventoBean.create("39.74335365871343,-0.794312605631447", 100007);
                eventoBean.create("38.74335365871343,-0.794312605631447", 100007);
                eventoBean.create("37.74335365871343,-0.794312605631447", 100007);
                eventoBean.create("36.74335365871343,-0.794312605631447", 100007);
            }

            //Até aqui

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
            evento = eventoBean.create("39.77113452695224,-8.77589736436629", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.77119336589832,-8.77737738613472", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.77071284636706,-8.7796867304458", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76972237714301,-8.78003121826916", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76771197500298,-8.779278448576422", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76592708096547,-8.779240172154449", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76462270609049,-8.781626069315621", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76319080741687,-8.788222373150852", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.7629456163095,-8.790774134742515", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.762494462391516,-8.79513764716253", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76177849465753,-8.799207706954833", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.76085655266007,-8.803788119111815", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.75953246539975,-8.807705073194033", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.75719808662091,-8.812285485329665", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.75420643515213,-8.816164163036705", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.75151874232512,-8.817516596696722", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.749605904379436,-8.818218331144275", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.745939664640076,-8.819245937946379", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.742474709810196,-8.819443185190098", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.74010630711908,-8.819701123904203", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73758614390503,-8.822174300953293", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.735181615142885,-8.82349849975027", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73460819940431,-8.824013483888022", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.7339275198208,-8.823246372112129", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73344897744597,-8.822988880043251", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.7328837981031,-8.822661650547817", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73262389656611,-8.822345149879824", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.732640398280125,-8.821443927638756", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73272290679097,-8.820907485828597", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73343660130284,-8.82134200369877", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73409665900888,-8.821915996438653", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73461232468722,-8.821143520232026", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.73484867524408,-8.820904501348142", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            evento = eventoBean.create("39.735989222463374,-8.821158646850703", 7);
            evento.setTimestamp(DateTimeFormatter.ISO_INSTANT.format(new Date(timestamp).toInstant()));
            timestamp += 60000;
            eventoBean.create("39.74335365871343,-8.794312605631447", 5);
            eventoBean.create("38.74335365871343,-8.794312605631447", 5);
            eventoBean.create("37.74335365871343,-8.794312605631447", 5);
            eventoBean.create("36.74335365871343,-8.794312605631447", 5);

            eventoBean.create("40", 6);

            eventoBean.create("21", 2);
            eventoBean.create("22", 2);
            eventoBean.create("23", 2);
            eventoBean.create("24", 2);

            eventoBean.create("41.740096,-7.468176", 222);
            eventoBean.create("41.684223,-7.472112", 222);
            eventoBean.create("41.619839,-7.461317", 222);
            eventoBean.create("41.590704,-7.455980", 222);
            eventoBean.create("41.528462,-7.491110", 222);
            eventoBean.create("41.442432,-7.479131", 222);
            eventoBean.create("41.347555,-7.548980", 222);
            eventoBean.create("41.144992,-7.556486", 222);
            eventoBean.create("40.983462,-7.729851", 222);
            eventoBean.create("40.798841,-7.849879", 222);
            eventoBean.create("40.641568,-7.900192", 222);
            eventoBean.create("40.464148,-7.980530", 222);
            eventoBean.create("40.322860,-8.029498", 222);
            eventoBean.create("40.157325,-8.028678", 222);
            eventoBean.create("39.989452,-8.049782", 222);
            eventoBean.create("39.813646,-8.066631", 222);
            eventoBean.create("39.703694,-8.052763", 222);
            eventoBean.create("39.349566,-8.307152", 222);
            eventoBean.create("39.277974,-8.682947", 222);
            eventoBean.create("38.891373,-8.031097", 222);
            eventoBean.create("38.714938,-8.101782", 222);
            eventoBean.create("38.370459,-8.235728", 222);
            eventoBean.create("38.057738,-7.861600", 222);
            eventoBean.create("37.476860,-8.030482", 222);
            eventoBean.create("37.245460,-7.935921", 222);
            eventoBean.create("37.019356,-7.930440", 222);

            eventoBean.create("12.5", 223);
            eventoBean.create("13.0", 223);
            eventoBean.create("14.2", 223);
            eventoBean.create("15.0", 223);
            eventoBean.create("16.4", 223);
            eventoBean.create("17.1", 223);
            eventoBean.create("16.8", 223);
            eventoBean.create("18.3", 223);
            eventoBean.create("19.0", 223);
            eventoBean.create("18.5", 223);
            eventoBean.create("20.2", 223);
            eventoBean.create("21.3", 223);
            eventoBean.create("22.0", 223);
            eventoBean.create("21.5", 223);
            eventoBean.create("22.7", 223);
            eventoBean.create("23.1", 223);
            eventoBean.create("24.5", 223);
            eventoBean.create("25.3", 223);
            eventoBean.create("26.0", 223);
            eventoBean.create("27.1", 223);
            eventoBean.create("28.2", 223);
            eventoBean.create("29.0", 223);
            eventoBean.create("30.1", 223);
            eventoBean.create("31.0", 223);
            eventoBean.create("30.5", 223);
            eventoBean.create("29.8", 223);

            eventoBean.create("41.878113,-87.629799", 9);
            eventoBean.create("41.729652,-88.232964", 9);
            eventoBean.create("41.343054,-88.842175", 9);
            eventoBean.create("40.633124,-89.398528", 9);
            eventoBean.create("39.781721,-89.650148", 9);

            eventoBean.create("38.627003,-90.199404", 9);
            eventoBean.create("38.220360,-91.173703", 9);
            eventoBean.create("37.149649,-93.324610", 9);
            eventoBean.create("37.085792,-94.513281", 9);

            eventoBean.create("37.075902,-94.704167", 9);
            eventoBean.create("37.005070,-94.616534", 9);

            eventoBean.create("36.153982,-95.992775", 9);
            eventoBean.create("35.467560,-97.516428", 9);
            eventoBean.create("35.533241,-97.962981", 9);
            eventoBean.create("35.212243,-99.784013", 9);

            eventoBean.create("35.222001,-101.831297", 9);
            eventoBean.create("35.178137,-101.903661", 9);
            eventoBean.create("35.064239,-102.418197", 9);

            eventoBean.create("35.084386,-106.650422", 9);
            eventoBean.create("35.185852,-107.887808", 9);
            eventoBean.create("35.528078,-108.742587", 9);

            eventoBean.create("35.198284,-111.651302", 9);
            eventoBean.create("35.248478,-112.185988", 9);
            eventoBean.create("35.333485,-113.668238", 9);
            eventoBean.create("35.189443,-114.053006", 9);
            eventoBean.create("35.027381,-114.592986", 9);

            eventoBean.create("34.909263,-117.023448", 9);
            eventoBean.create("34.135560,-117.923462", 9);
            eventoBean.create("34.107855,-118.125468", 9);
            eventoBean.create("34.019470,-118.491227", 9);

            eventoBean.create("10.5", 8);
            eventoBean.create("12.0", 8);
            eventoBean.create("14.2", 8);
            eventoBean.create("16.0", 8);
            eventoBean.create("19.5", 8);

            eventoBean.create("16.7", 8);
            eventoBean.create("20.1", 8);
            eventoBean.create("18.4", 8);
            eventoBean.create("21.2", 8);

            eventoBean.create("18.0", 8);
            eventoBean.create("22.3", 8);

            eventoBean.create("22.7", 8);
            eventoBean.create("27.5", 8);
            eventoBean.create("29.1", 8);
            eventoBean.create("24.4", 8);

            eventoBean.create("28.9", 8);
            eventoBean.create("30.2", 8);
            eventoBean.create("32.4", 8);

            eventoBean.create("23.0", 8);
            eventoBean.create("24.5", 8);
            eventoBean.create("22.8", 8);

            eventoBean.create("30.0", 8);
            eventoBean.create("32.2", 8);
            eventoBean.create("33.8", 8);
            eventoBean.create("36.5", 8);
            eventoBean.create("38.3", 8);

            eventoBean.create("35.5", 8);
            eventoBean.create("37.2", 8);
            eventoBean.create("37.8", 8);
            eventoBean.create("39.0", 8);


            try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_DIRECOES))) {
                String[] linha;
                int i = 0;
                while ((linha = csvReader.readNext()) != null) {
                    i++;
                    String longitude = linha[0];
                    String latitude = linha[1];
                    if (eventoBean.findAll().size()%100 == 0){
                        System.out.println("Foram inseridos +100 eventos");
                    }
                    //Ler de 10 em 10 linhas porque o ficheiro é muito grande
                    if (i % 10 == 0){
                        eventoBean.create(longitude + "," + latitude, 10);
                    }
                }
            } catch (IOException | CsvValidationException e) {
                System.out.println("Ficheiro não encontrado");
            }

            try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_DIRECOES2))) {
                String[] linha;
                int i = 0;
                while ((linha = csvReader.readNext()) != null) {
                    i++;
                    String longitude = linha[0];
                    String latitude = linha[1];
                    if (eventoBean.findAll().size()%100 == 0){
                        System.out.println("Foram inseridos +100 eventos");
                    }
                    //Ler de 10 em 10 linhas porque o ficheiro é muito grande
                    if (i % 50 == 0){
                        eventoBean.create(longitude + "," + latitude, 11);
                    }
                }
            } catch (IOException | CsvValidationException e) {
                System.out.println("Ficheiro não encontrado");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }
}
