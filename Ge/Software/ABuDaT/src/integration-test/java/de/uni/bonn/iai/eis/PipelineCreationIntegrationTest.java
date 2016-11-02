package de.uni.bonn.iai.eis;

import com.linkedpipes.plugin.extractor.httpget.HttpGetConfiguration;
import com.linkedpipes.plugin.loader.local.LoaderLocalConfiguration;
import com.linkedpipes.plugin.transformer.filesToRdf.FilesToRdfConfiguration;
import com.linkedpipes.plugin.transformer.rdftofile.RdfToFileConfiguration;
import com.linkedpipes.plugin.transformer.sparql.update.SparqlUpdateConfiguration;
import com.linkedpipes.plugin.transformer.tabularuv.TabularConfig_V2;
import com.linkedpipes.plugin.transformer.textHolder.TextHolderConfiguration;
import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.etl.PipelineBuilder;
import de.uni.bonn.iai.eis.etl.component.*;
import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesETL;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.model.Model;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PipelineCreationIntegrationTest {

    @Autowired
    private ABuDaTConfiguration aBuDaTConfiguration;

    @Configuration
    public static class TestContextConfiguration {

        @Bean
        public ABuDaTConfiguration aBuDaTConfiguration() {
            return new ABuDaTConfiguration();
        }

    }

    @Test
    public void createPipelineWithHttpGetAndTabularUvComponent() throws URISyntaxException, IOException, InterruptedException, ParseException {

        PipelineBuilder pipelineBuilder = new PipelineBuilder(aBuDaTConfiguration);

        HttpGetConfiguration httpGetConfiguration = new HttpGetConfiguration();
        httpGetConfiguration.setFileName("esif.csv");
        httpGetConfiguration.setUri("https://cohesiondata.ec.europa.eu/api/views/e4v6-qrrq/rows.csv?accessType=DOWNLOAD");

        HttpGet httpGet = new HttpGet(httpGetConfiguration);
        httpGet.setPrefLabel("HttpGet");
        httpGet.setDescription("download esif.csv");
        httpGet.setX(300);
        httpGet.setY(300);

        //-------------

        TabularConfig_V2 tabularConfig_v2 = new TabularConfig_V2();
        tabularConfig_v2.setBaseURI("http://data.openbudgets.eu/resource/dataset/ESIF-2014-2020/observation/");
        tabularConfig_v2.setEncoding("UTF-8");
        tabularConfig_v2.setIgnoreMissingColumn(true);
        tabularConfig_v2.setIgnoreBlankCells(true);
        tabularConfig_v2.setGenerateRowTriple(false);
        tabularConfig_v2.setGenerateNew(false);
        tabularConfig_v2.setRowsClass("http://purl.org/linked-data/cube#Observation");

        List<TabularConfig_V2.ColumnInfo_V1> colums = new ArrayList<>();

        TabularConfig_V2.ColumnInfo_V1 ms_name = createColumnInfo(
                "MS Name", TabularConfig_V2.ColumnType.String,
                "http://data.openbudgets.eu/ontology/dsd/dimension/administrativeClassification");

        TabularConfig_V2.ColumnInfo_V1 cci = createColumnInfo(
                "CCI", TabularConfig_V2.ColumnType.String,
                "http://data.openbudgets.eu/ontology/dsd/ESIF/dimension/programClassification");

        TabularConfig_V2.ColumnInfo_V1 eafrd_fa = createColumnInfo(
                "EAFRD FA", TabularConfig_V2.ColumnType.String,
                "http://data.openbudgets.eu/ontology/dsd/ESIF/dimension/eafrdFaClassification");

        TabularConfig_V2.ColumnInfo_V1 amount_eu = createColumnInfo(
                "EU Amount", TabularConfig_V2.ColumnType.Decimal,
                "http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/measure/amountEU");

        TabularConfig_V2.ColumnInfo_V1 fund = createColumnInfo(
                "Fund", TabularConfig_V2.ColumnType.String,
                "http://data.openbudgets.eu/ontology/dsd/dimension/fund");

        TabularConfig_V2.ColumnInfo_V1 national_amount = createColumnInfo(
                "National Amount", TabularConfig_V2.ColumnType.Decimal,
                "http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/measure/amountNational");

        TabularConfig_V2.ColumnInfo_V1 eafrd_measure = createColumnInfo(
                "EAFRD MEASURE", TabularConfig_V2.ColumnType.String,
                "http://data.openbudgets.eu/ontology/dsd/ESIF/dimension/eafrdMeasureClassification");

        TabularConfig_V2.ColumnInfo_V1 to = createColumnInfo(
                "TO", TabularConfig_V2.ColumnType.String,
                "http://data.openbudgets.eu/ontology/dsd/ESIF/dimension/thematicObjectiveClassification");

        TabularConfig_V2.ColumnInfo_V1 priority = createColumnInfo(
                "Priority", TabularConfig_V2.ColumnType.String,
                "http://data.openbudgets.eu/ontology/dsd/ESIF/dimension/priorityClassification");

        colums.add(ms_name);
        colums.add(cci);
        colums.add(eafrd_fa);
        colums.add(amount_eu);
        colums.add(fund);
        colums.add(national_amount);
        colums.add(eafrd_measure);
        colums.add(to);
        colums.add(priority);

        tabularConfig_v2.setColumnsInfo(colums);

        TabularUv tabularUv = new TabularUv(tabularConfig_v2);
        tabularUv.setPrefLabel("Mapping");
        tabularUv.setDescription("mapping to obeu data model");
        tabularUv.setX(600);
        tabularUv.setY(300);

        //--------------

        GraphMerger graphMerger1 = new GraphMerger();
        graphMerger1.setPrefLabel("GraphMerger");
        graphMerger1.setX(900);
        graphMerger1.setY(300);

        //-------------


        String deleteRowNumbersQuery;
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("delete_rownumbers.sparql")){
            deleteRowNumbersQuery = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        }

        SparqlUpdateConfiguration sparqlUpdateConfiguration1 = new SparqlUpdateConfiguration();
        sparqlUpdateConfiguration1.setQuery(deleteRowNumbersQuery);

        SparqlUpdate sparqlUpdate1 = new SparqlUpdate(sparqlUpdateConfiguration1);
        sparqlUpdate1.setPrefLabel("SparqlUpdate");
        sparqlUpdate1.setDescription("delete tabular row number statements");
        sparqlUpdate1.setX(350);
        sparqlUpdate1.setY(400);

        //---------

        String updateDimensionsQuery;
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("update_dimensions.sparql")) {
            updateDimensionsQuery = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        }

        SparqlUpdateConfiguration sparqlUpdateConfiguration2 = new SparqlUpdateConfiguration();
        sparqlUpdateConfiguration2.setQuery(updateDimensionsQuery);

        SparqlUpdate sparqlUpdate2 = new SparqlUpdate(sparqlUpdateConfiguration2);
        sparqlUpdate2.setPrefLabel("SparqlUpdate");
        sparqlUpdate2.setDescription("update dimesions to urls");
        sparqlUpdate2.setX(800);
        sparqlUpdate2.setY(400);

        //----------

        String dataSetDefinition;
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("dsd.ttl")) {
            dataSetDefinition = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        }

        TextHolderConfiguration textHolderConfiguration = new TextHolderConfiguration();
        textHolderConfiguration.setContent(dataSetDefinition);
        //the .ttl extension is important so that the next component can determine the type.
        textHolderConfiguration.setFileName("dsd.ttl");

        TextHolder textHolder = new TextHolder(textHolderConfiguration);
        textHolder.setPrefLabel("TextHolder");
        textHolder.setDescription("data set definition");
        textHolder.setX(1200);
        textHolder.setY(300);

        //--------------

        FilesToRdfConfiguration filesToRdfConfiguration = new FilesToRdfConfiguration();
        FilesToRdf filesToRdf = new FilesToRdf(filesToRdfConfiguration);
        filesToRdf.setPrefLabel("FilesToRdf");
        filesToRdf.setDescription("data set definition rdf");
        filesToRdf.setX(1200);
        filesToRdf.setY(400);


        //--------------

        GraphMerger graphMerger2 = new GraphMerger();
        graphMerger2.setPrefLabel("GraphMerger");
        graphMerger2.setX(1200);
        graphMerger2.setY(500);

        //---------------

        GraphUnion graphUnion = new GraphUnion();
        graphUnion.setPrefLabel("Union");
        graphUnion.setX(1400);
        graphUnion.setY(600);

        //---------------

        String dsdData;
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("dsd_data.sparql")) {
            dsdData = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        }
        SparqlUpdateConfiguration dsdDataSparqlUpdateConfiguration = new SparqlUpdateConfiguration();
        dsdDataSparqlUpdateConfiguration.setQuery(dsdData);

        SparqlUpdate dsdDataSparqlUpdate = new SparqlUpdate(dsdDataSparqlUpdateConfiguration);
        dsdDataSparqlUpdate.setPrefLabel("SparqlUpdate");
        dsdDataSparqlUpdate.setDescription("dsd data");
        dsdDataSparqlUpdate.setX(900);
        dsdDataSparqlUpdate.setY(600);


        //------------
        RdfToFileConfiguration rdfToFileConfiguration = new RdfToFileConfiguration();
        rdfToFileConfiguration.setFileName("esif.ttl");
        rdfToFileConfiguration.setFileType("text/turtle");

        RdfToFile rdfToFile = new RdfToFile(rdfToFileConfiguration);
        rdfToFile.setPrefLabel("RdfToFile");
        rdfToFile.setX(1600);
        rdfToFile.setY(600);

        //------------
        LoaderLocalConfiguration loaderLocalConfiguration = new LoaderLocalConfiguration();
        loaderLocalConfiguration.setPath("/tmp/");

        FilesToLocal filesToLocal = new FilesToLocal(loaderLocalConfiguration);
        filesToLocal.setPrefLabel("FilesToLocal");
        filesToLocal.setDescription("copy result to local file system");
        filesToLocal.setX(1800);
        filesToLocal.setY(600);


        //------- CONNECTIONS --------

        Connection httpGetToTabularUv = new ExtendedConnection(httpGet, tabularUv);
        Connection tabularUvToGraphMerger = new ExtendedConnection(tabularUv, graphMerger1);
        Connection graphMergerToSparqlUpdate1 = new ExtendedConnection(graphMerger1, sparqlUpdate1);
        Connection sparqlUpdate1ToSparqlUpdate2 = new ExtendedConnection(sparqlUpdate1, sparqlUpdate2);
        Connection textHolderToFilesToRdf = new ExtendedConnection(textHolder, filesToRdf);
        Connection filesToRdfToGraphMerger = new ExtendedConnection(filesToRdf, graphMerger2);
        Connection graphMerger2ToUnion = new ExtendedConnection(graphMerger2, graphUnion);
        Connection sparqlUpdate2ToUnion = new ExtendedConnection(sparqlUpdate2, graphUnion);
        Connection dsdDataUpdateToGraphUnion = new ExtendedConnection(dsdDataSparqlUpdate, graphUnion);
        Connection graphUnionToRdfToFile = new ExtendedConnection(graphUnion, rdfToFile);
        Connection rdfToFileToFilesToLocal = new ExtendedConnection(rdfToFile, filesToLocal);

        //------ build the pipeline

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Pipeline pipelineBuilt = pipelineBuilder
                .withComponent(httpGet)
                .withComponent(tabularUv)
                .withComponent(graphMerger1)
                .withComponent(sparqlUpdate1)
                .withComponent(sparqlUpdate2)
                .withComponent(textHolder)
                .withComponent(filesToRdf)
                .withComponent(graphMerger2)
                .withComponent(graphUnion)
                .withComponent(dsdDataSparqlUpdate)
                .withComponent(rdfToFile)
                .withComponent(filesToLocal)
                .withConnection(httpGetToTabularUv)
                .withConnection(tabularUvToGraphMerger)
                .withConnection(graphMergerToSparqlUpdate1)
                .withConnection(sparqlUpdate1ToSparqlUpdate2)
                .withConnection(textHolderToFilesToRdf)
                .withConnection(filesToRdfToGraphMerger)
                .withConnection(graphMerger2ToUnion)
                .withConnection(sparqlUpdate2ToUnion)
                .withConnection(dsdDataUpdateToGraphUnion)
                .withConnection(graphUnionToRdfToFile)
                .withConnection(rdfToFileToFilesToLocal)
                .withLabel("Generated by ABuDaT integration test at: "+ dateTime)
                .build();

        assertEquals(12, pipelineBuilt.getComponents().size());
        assertEquals(11, pipelineBuilt.getConnections().size());

        String pipelineId = pipelineBuilt.getId();

        LinkedPipesETL linkedPipesETL = new LinkedPipesETL(aBuDaTConfiguration);
        linkedPipesETL.createPipeline(pipelineId);
        linkedPipesETL.updatePipeline(pipelineBuilt);

        Pipeline readPipeline = linkedPipesETL.readPipeline(pipelineId);

        assertEquals(12, readPipeline.getComponents().size());
        assertEquals(11, readPipeline.getConnections().size());

        linkedPipesETL.deletePipeline(readPipeline);

//        String executionIRIString = etl.executePipeline(pipeline);
//
//        System.out.println("executionIRIString = " + executionIRIString);
//
//        Thread.sleep(10000);
//
//        waitForExecutionToFinish(executionIRIString);
    }




    @Ignore
    @Test
    public void testReadTurtle() throws IOException {

        Model expectedModel = new LinkedHashModel();
        StatementCollector collector = new StatementCollector(expectedModel);

        RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);
        rdfParser.setRDFHandler(collector);

        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("expected_esif.ttl")) {
            rdfParser.parse(resourceAsStream, "http://localhost/base/");
        }
    }


    private TabularConfig_V2.ColumnInfo_V1 createColumnInfo(String name, TabularConfig_V2.ColumnType columnType, String uri) {
        TabularConfig_V2.ColumnInfo_V1 ms_name = new TabularConfig_V2.ColumnInfo_V1();
        ms_name.setName(name);
        ms_name.setType(columnType);
        ms_name.setURI(uri);
        return ms_name;
    }

}
