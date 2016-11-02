package de.uni.bonn.iai.eis;

import com.linkedpipes.plugin.extractor.httpget.HttpGetConfiguration;
import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.etl.component.Component;
import de.uni.bonn.iai.eis.etl.component.HttpGet;
import de.uni.bonn.iai.eis.etl.linkedpipes.GeneralConfiguration;
import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesETL;
import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesOntology;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.model.IRI;
import org.openrdf.model.impl.SimpleValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkedPipesEtlIntegrationTest {

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
    public void testUpdatePipeline() throws Exception {
        LinkedPipesETL linkedPipesETL = new LinkedPipesETL(aBuDaTConfiguration);

        String pipelineId = UUID.randomUUID().toString();
        String pipelineUri = aBuDaTConfiguration.getPipelineBaseUrl() + "/" + pipelineId;

        linkedPipesETL.createPipeline(pipelineId);

        String label = "Pipeline with http component.";

        Pipeline pipeline = new Pipeline();
        pipeline.setId(pipelineId);
        pipeline.setUri(pipelineUri);
        pipeline.setLabel(label);

        UUID uuid = UUID.randomUUID();
        String componentURL = aBuDaTConfiguration.getComponentBaseUrl() + "/" + uuid.toString();
        String componentConfigurationGraphURL = componentURL + "/configuration";

        SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();
        IRI httpGetUri = valueFactory.createIRI(componentURL);

        HttpGetConfiguration httpGetConfiguration = new HttpGetConfiguration();
        httpGetConfiguration.setUri("http://file.url/fsdf");
        httpGetConfiguration.setFileName("file.ext");

        HttpGet httpGet = new HttpGet(httpGetConfiguration);
        httpGet.setUri(httpGetUri.toString());
        httpGet.setConfigurationGraphURL(componentConfigurationGraphURL);

        GeneralConfiguration generalConfiguration = new GeneralConfiguration();
        generalConfiguration.id = httpGetUri;
        generalConfiguration.type = LinkedPipesOntology.COMPONENT_TYPE;
        generalConfiguration.description = "description";
        generalConfiguration.x = 300;
        generalConfiguration.y = 300;
        generalConfiguration.pipeline = valueFactory.createIRI(pipeline.getUri());
        generalConfiguration.prefLabel = "Http empty label";
        generalConfiguration.template = valueFactory.createIRI("http://localhost:8080/resources/components/e-httpGetFile");

        httpGet.addStatements(generalConfiguration.getModel());

        List<Component> components = new ArrayList<>();
        components.add(httpGet);

        pipeline.setComponents(components);

        linkedPipesETL.updatePipeline(pipeline);

        Pipeline readPipeline = linkedPipesETL.readPipeline(pipelineId);
        assertEquals(label, readPipeline.getLabel());
        assertTrue(readPipeline.getComponents().size() == 1);

        new RestTemplate().delete(pipeline.getUri());
    }

    @Test
    public void testRoundTripCreateReadDeletePipeline() throws Exception {
        LinkedPipesETL linkedPipesETL = new LinkedPipesETL(aBuDaTConfiguration);

        Pipeline pipeline = linkedPipesETL.createPipeline(UUID.randomUUID().toString());
        assertNotNull(pipeline);
        assertNotNull(pipeline.getId());
        assertNotNull(pipeline.getUri());

        Pipeline pipelineRead = linkedPipesETL.readPipeline(pipeline.getId());
        assertNotNull(pipelineRead);

        linkedPipesETL.deletePipeline(pipelineRead);

        assertFalse(linkedPipesETL.readPipelines().containsKey(pipelineRead.getId()));
    }

    @Test
    public void testGetExecutions() throws IOException, URISyntaxException {
        LinkedPipesETL linkedPipesETL = new LinkedPipesETL(aBuDaTConfiguration);

        Map<String, List<String>> executions = linkedPipesETL.getExecutionsByPipeline();

        assertNotNull(executions);
    }

    @Test
    public void testExecutePipeline() throws IOException, URISyntaxException {
        LinkedPipesETL linkedPipesETL = new LinkedPipesETL(aBuDaTConfiguration);

        String pipelineId = UUID.randomUUID().toString();
        Pipeline pipeline = linkedPipesETL.createPipeline(pipelineId);

        String executionURI = linkedPipesETL.executePipeline(pipeline);

        waitForExecutionToFinish(executionURI, linkedPipesETL);

        linkedPipesETL.deleteExecution(executionURI);
        linkedPipesETL.deletePipeline(pipeline);
    }

    private void waitForExecutionToFinish(String executionURI, LinkedPipesETL linkedPipesETL) throws IOException, URISyntaxException {
        int maxRetries = 3;
        int numberOfRetries = 0;

        while (true) {
            String executionStatus = linkedPipesETL.getExecutionStatus(executionURI);

            if (Optional.ofNullable(executionStatus).isPresent()) {
                if (executionStatus.equals("http://etl.linkedpipes.com/resources/status/finished") ||
                        executionStatus.equals("http://etl.linkedpipes.com/resources/status/failed")) {
                    break;
                }
            }

            if (++numberOfRetries > maxRetries) {
                throw new RuntimeException("operation timed out...");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
