package pt.ipleiria.estg.dei.ei.dae.pmei.ejbs;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private static final String CSV_FILE = "/products.csv";

    @PostConstruct
    public void populateDB() {
        try {
            clienteBean.create("João", 123456789, "joao", "123");
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
            sensorBean.create(5,"Aceleracao", true, 1);

            eventoBean.create("25", 1);
            sleep(1000);
            eventoBean.create("24.3", 1);
            sleep(1000);
            eventoBean.create("26", 1);
            sleep(1000);
            eventoBean.create("25.3", 1);

            eventoBean.create("39.73557517376048,-8.821133807434666", 4);
            sleep(1000);

            eventoBean.create("40", 5);
            sleep(1000);

            eventoBean.create("21", 2);
            sleep(1000);
            eventoBean.create("22", 2);
            sleep(1000);
            eventoBean.create("23", 2);
            sleep(1000);
            eventoBean.create("24", 2);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }
}
