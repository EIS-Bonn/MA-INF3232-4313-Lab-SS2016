package de.uni.bonn.iai.eis.etl;

import com.linkedpipes.plugin.extractor.httpget.HttpGetConfiguration;
import com.linkedpipes.plugin.transformer.tabularuv.TabularConfig_V2;
import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.etl.component.*;
import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesRdfHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openrdf.model.Model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PipelineBuilderTest {

    @Mock
    private ABuDaTConfiguration aBuDaTConfiguration;

    @InjectMocks
    private PipelineBuilder pipelineBuilder;


    @Before
    public void setupMocks() {
        when(aBuDaTConfiguration.getPipelineBaseUrl()).thenReturn("http://example.com/pipeline");
        when(aBuDaTConfiguration.getComponentBaseUrl()).thenReturn("http://example.com/component");
    }

    @Test
    public void testBuildEmptyPipeline() throws IOException, URISyntaxException {
        Pipeline pipeline = pipelineBuilder.build();

        assertNotNull(pipeline);
        assertNotNull(pipeline.getId());
        assertNotNull(pipeline.getUri());

        assertNotNull(pipeline.getComponents());
        assertEquals(0, pipeline.getComponents().size());

        assertNotNull(pipeline.getConnections());
        assertEquals(0, pipeline.getConnections().size());

        assertNull(pipeline.getLabel());
    }

    @Test
    public void testBuildEmptyPipelineWithGivenLabel() throws URISyntaxException, IOException {
        String label = "A label for the pipeline.";

        Pipeline pipeline = pipelineBuilder.withLabel(label).build();

        assertNotNull(pipeline);
        assertNotNull(pipeline.getId());
        assertNotNull(pipeline.getUri());

        assertNotNull(pipeline.getComponents());
        assertEquals(0, pipeline.getComponents().size());

        assertNotNull(pipeline.getConnections());
        assertEquals(0, pipeline.getConnections().size());

        assertNotNull(pipeline.getLabel());
        assertEquals(label, pipeline.getLabel());
    }

    @Test
    public void testBuildPipelineWithComponent() throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet(null);
        httpGet.setPrefLabel("label");
        httpGet.setDescription("description");
        httpGet.setX(0);
        httpGet.setY(0);

        Pipeline pipeline = pipelineBuilder.withComponent(httpGet).build();

        assertNotNull(pipeline);
        assertNotNull(pipeline.getId());
        assertNotNull(pipeline.getUri());

        assertNotNull(pipeline.getComponents());
        assertEquals(1, pipeline.getComponents().size());

        assertNotNull(pipeline.getConnections());
        assertEquals(0, pipeline.getConnections().size());

        assertNull(pipeline.getLabel());
    }

    @Test
    public void testBuildPipelineWithLabelAndTwoConnectedComponents() throws IOException, URISyntaxException {
        String label = "A label for the pipeline.";

        HttpGet httpGet = new HttpGet(new HttpGetConfiguration());
        httpGet.setPrefLabel("label");
        httpGet.setDescription("description");
        httpGet.setX(0);
        httpGet.setY(0);
        httpGet.setId("1");

        TabularUv tabularUv = new TabularUv(new TabularConfig_V2());
        tabularUv.setPrefLabel("label");
        tabularUv.setDescription("description");
        tabularUv.setX(0);
        tabularUv.setY(0);
        tabularUv.setId("2");

        ExtendedConnection httpGetToTabularUv = new ExtendedConnection(httpGet, tabularUv);
        httpGetToTabularUv.setId("3");

        Pipeline pipeline = pipelineBuilder
                .withLabel(label)
                .withComponent(httpGet)
                .withComponent(tabularUv)
                .withConnection(httpGetToTabularUv)
                .build("pipelineId");

        assertNotNull(pipeline);
        assertNotNull(pipeline.getId());
        assertNotNull(pipeline.getUri());

        Collection<? extends Component> components = pipeline.getComponents();
        assertNotNull(components);
        assertEquals(2, components.size());

        Collection<Connection> connections = pipeline.getConnections();
        assertNotNull(connections);
        assertEquals(1, pipeline.getConnections().size());

        assertNotNull(pipeline.getLabel());
        assertEquals(label, pipeline.getLabel());

        Model expectedModel;
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("expectedPipeline.json")) {
            expectedModel = LinkedPipesRdfHelper.modelFromJsonLd(resourceAsStream);
        }
        String expected = LinkedPipesRdfHelper.modelToString(expectedModel);


        Model actualModel = LinkedPipesRdfHelper.modelFromPipeline(pipeline);
        String actual = LinkedPipesRdfHelper.modelToString(actualModel);

        assertEquals(expected, actual);
    }
}