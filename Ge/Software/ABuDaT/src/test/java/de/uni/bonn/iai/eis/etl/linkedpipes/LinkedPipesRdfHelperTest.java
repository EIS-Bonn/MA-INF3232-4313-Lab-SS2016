package de.uni.bonn.iai.eis.etl.linkedpipes;

import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.TestHelper;
import de.uni.bonn.iai.eis.etl.component.Component;
import de.uni.bonn.iai.eis.etl.component.Connection;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openrdf.model.Model;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class LinkedPipesRdfHelperTest {

    @Test
    public void parseSimplePipeline() throws Exception {
        String pipelineId = "created-1465510928974";
        String pipelineURI = "http://localhost:8080/resources/pipelines/created-1465510928974";
        String pipelineResource = "created-1465510928974.json";

        String pipelineDefinition;
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(pipelineResource)) {
            pipelineDefinition = IOUtils.toString(resourceAsStream, "UTF-8");
        }

        Pipeline pipeline = LinkedPipesRdfHelper.parsePipeline(pipelineDefinition);

        assertNotNull(pipeline);
        assertEquals(pipelineId, pipeline.getId());
        assertEquals(pipelineURI, pipeline.getUri());
    }

    @Test
    public void pipelineFromModel() throws Exception {
        String pipelineId = "created-1465721125957";
        String pipelineURI = "http://localhost:8080/resources/pipelines/created-1465721125957";
        String pipelineResource = "created-1465721125957.json";

        InputStream pipelineResourceAsStream = this.getClass().getClassLoader().getResourceAsStream(pipelineResource);

        RDFParser parser = Rio.createParser(RDFFormat.JSONLD);
        Model expectedModel = new LinkedHashModel();
        StatementCollector collector = new StatementCollector(expectedModel);
        parser.setRDFHandler(collector);

        parser.parse(pipelineResourceAsStream, "http://localhost:8080");

        Pipeline pipeline = LinkedPipesRdfHelper.pipelineFromModel(expectedModel);

        assertNotNull(pipeline);
        assertEquals(pipelineId, pipeline.getId());
        assertEquals(pipelineURI, pipeline.getUri());

        Collection<? extends Component> configurableComponents = pipeline.getComponents();
        assertNotNull(configurableComponents);
        assertEquals(2, configurableComponents.size());

        Collection<Connection> connections = pipeline.getConnections();
        assertNotNull(connections);
        assertEquals(1, connections.size());

        Model actualModel = LinkedPipesRdfHelper.modelFromPipeline(pipeline);

        TestHelper.assertModelsEqual(expectedModel, actualModel);
    }

    @Test
    public void parseComplexPipeline() throws Exception {
        String pipelineId = "uv-011a7fd7-6877-4b04-8d37-216f5be0cccd";
        String pipelineURI = "http://localhost/pipelines/uv-011a7fd7-6877-4b04-8d37-216f5be0cccd";
        String pipelineResource = "aragon_2013_gastos_pipeline.jsonld";

        InputStream pipelineResourceAsStream = this.getClass().getClassLoader().getResourceAsStream(pipelineResource);

        RDFParser parser = Rio.createParser(RDFFormat.JSONLD);
        Model expectedModel = new LinkedHashModel();
        StatementCollector collector = new StatementCollector(expectedModel);
        parser.setRDFHandler(collector);

        parser.parse(pipelineResourceAsStream, "http://localhost:8080");

        Pipeline pipeline = LinkedPipesRdfHelper.pipelineFromModel(expectedModel);

        assertNotNull(pipeline);
        assertEquals(pipelineId, pipeline.getId());
        assertEquals(pipelineURI, pipeline.getUri());

        Collection<? extends Component> components = pipeline.getComponents();
        assertNotNull(components);
        assertEquals(11, components.size());

        Collection<Connection> connections = pipeline.getConnections();
        assertNotNull(connections);
        assertEquals(11, connections.size());

        Model actualModel = LinkedPipesRdfHelper.modelFromPipeline(pipeline);

        TestHelper.assertModelsEqual(expectedModel, actualModel);
    }

    @Test
    public void testModelToString() throws IOException {
        String pipelineResource = "pipelineDefinitionForRoundtripTest.json";

        InputStream pipelineResourceAsStream = this.getClass().getClassLoader().getResourceAsStream(pipelineResource);
        byte[] pipelineData = IOUtils.toByteArray(pipelineResourceAsStream);

        RDFParser parser = Rio.createParser(RDFFormat.JSONLD);
        Model model = new LinkedHashModel();
        StatementCollector collector = new StatementCollector(model);
        parser.setRDFHandler(collector);

        parser.parse(new ByteArrayInputStream(pipelineData), "http://localhost:8080");

        String expectedResult = IOUtils.toString(new ByteArrayInputStream(pipelineData), Charset.forName("UTF-8"));
        String result = LinkedPipesRdfHelper.modelToString(model);

        assertEquals(expectedResult, result);
    }

}