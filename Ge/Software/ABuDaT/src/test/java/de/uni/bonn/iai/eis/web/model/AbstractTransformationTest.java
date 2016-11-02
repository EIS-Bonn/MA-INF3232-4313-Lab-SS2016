package de.uni.bonn.iai.eis.web.model;

import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.etl.component.Component;
import de.uni.bonn.iai.eis.etl.component.Connection;
import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesRdfHelper;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.openrdf.model.Model;
import org.openrdf.model.util.Models;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractTransformationTest {

    protected ABuDaTConfiguration aBuDaTConfiguration = mock(ABuDaTConfiguration.class);

    @Before
    public void setupMocks() {
        when(aBuDaTConfiguration.getPipelineBaseUrl()).thenReturn("http://example.com/pipeline");
        when(aBuDaTConfiguration.getComponentBaseUrl()).thenReturn("http://example.com/component");
    }

    protected void assertPipelineIsCorrect(Pipeline pipeline, String expectedPipelineResourceFileName) throws IOException {
        HashMap<String, String> urisMap = new HashMap<>();

        int index = 0;
        for (Component component : pipeline.getComponents()) {
            String oldUri = component.getUri();
            String newUri = "http://example.com/component/" + index++;
            urisMap.put(oldUri, newUri);
        }

        index = 0;
        for (Connection connection : pipeline.getConnections()) {
            String oldUri = connection.getUri();
            String newUri = "http://example.com/connection/" + index++;
            urisMap.put(oldUri, newUri);
        }

        Model model = LinkedPipesRdfHelper.modelFromPipeline(pipeline);
        String actual = LinkedPipesRdfHelper.modelToString(model);


        for (String s : urisMap.keySet()) {
            actual = actual.replaceAll(s, urisMap.get(s));
        }

        actual = actual.replaceAll("http://example.com/pipeline/[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[0-9a-f]{12}", "http://example.com/pipeline/1");
        actual = actual.replaceAll("http://localhost/resources/temp/[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[0-9a-f]{12}", "http://localhost/resources/temp");

        Pattern pattern = Pattern.compile("(_:node\\w*)");
        Matcher matcher = pattern.matcher(actual);

        Set<String> toReplace = new HashSet<>();
        while (matcher.find()) {
            String group = matcher.group(1);
            toReplace.add(group);
        }

        for (String s : toReplace) {
            actual = actual.replaceAll(s, "_:bNode");
        }

        System.out.println(actual);

        Model expectedModel;
        Model actualModel;
        try (
                InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(expectedPipelineResourceFileName);
                InputStream inputStream = IOUtils.toInputStream(actual, Charset.forName("UTF-8"))
        ) {
            expectedModel = LinkedPipesRdfHelper.modelFromJsonLd(resourceAsStream);
            actualModel = LinkedPipesRdfHelper.modelFromJsonLd(inputStream);
        }

        assertEquals(expectedModel.size(), actualModel.size());
        assertTrue(Models.isSubset(expectedModel, actualModel));
    }
}
