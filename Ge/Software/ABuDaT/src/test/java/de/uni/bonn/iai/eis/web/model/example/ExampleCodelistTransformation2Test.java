package de.uni.bonn.iai.eis.web.model.example;

import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.web.model.AbstractTransformationTest;
import org.junit.Test;

import java.io.IOException;

public class ExampleCodelistTransformation2Test extends AbstractTransformationTest {
    @Test
    public void testTransformation() throws IOException {
        ExampleCodelistTransformation2 transformation = new ExampleCodelistTransformation2();

        Pipeline pipeline = transformation.createPipeline("data.ttl", "/tmp", aBuDaTConfiguration);

        assertPipelineIsCorrect(pipeline, "aragon_codelist_transformation2_create_pipeline_expected.json");
    }
}