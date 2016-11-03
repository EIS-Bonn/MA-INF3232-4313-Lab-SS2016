package de.uni.bonn.iai.eis;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesETL;
import de.uni.bonn.iai.eis.rdf.obeu.ComponentProperty;
import de.uni.bonn.iai.eis.rdf.obeu.classification.*;
import de.uni.bonn.iai.eis.rdf.obeu.measure.Amount;
import de.uni.bonn.iai.eis.web.EtlService;
import de.uni.bonn.iai.eis.web.InputAnalyzerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
public class ABuDaTApplication extends SpringBootServletInitializer {

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        MultipartResolver multipartResolver = new StandardServletMultipartResolver();
//        MultipartResolver multipartResolver = new CommonsMultipartResolver();

        return multipartResolver;
    }

    @Bean
    public ABuDaTConfiguration aBuDaTConfiguration() {
        return new ABuDaTConfiguration();
    }

    @Bean
    public LinkedPipesETL linkedPipesETL() {
        return new LinkedPipesETL(aBuDaTConfiguration());
    }

    @Bean
    public EtlService etlService() {
        return new EtlService(aBuDaTConfiguration(), linkedPipesETL());
    }

    @Bean
    public DatabaseService templateService() {
        return new DatabaseService();
    }

    @Bean
    public InputAnalyzerService inputAnalyzerService() {
        return new InputAnalyzerService();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ABuDaTApplication.class);
    }

    @Bean
    public List<ComponentProperty> coreClassifications() {
        List<ComponentProperty> classifications = new LinkedList<>();

        classifications.add(new Classification());
        classifications.add(new AdministrativeClassification());
        classifications.add(new EconomicClassification());
        classifications.add(new FunctionalClassification());
        classifications.add(new ProgrammeClassification());

        return classifications;
    }

    @Bean
    public List<ComponentProperty> coreMeasures() {
        List<ComponentProperty> measures = new LinkedList<>();

        measures.add(new Amount());

        return measures;
    }

    @Bean
    public TripleStoreService tripleStoreService() {
        DatasetAccessor datasetAccessor = DatasetAccessorFactory.createHTTP(aBuDaTConfiguration().fusekiDataEndpoint());
        return new FusekiService(datasetAccessor);
    }

    @Bean
    public DownloadUtils downloadUtils() {
        return new DownloadUtils();
    }

    @Bean
    public ValidatorService validatorService(){
        return new ValidatorService();
    }

    public static void main(String[] args) {
        SpringApplication.run(ABuDaTApplication.class, args);
    }
}
