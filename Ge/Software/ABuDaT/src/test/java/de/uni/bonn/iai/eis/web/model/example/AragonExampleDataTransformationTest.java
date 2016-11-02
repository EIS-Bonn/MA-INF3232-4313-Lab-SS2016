package de.uni.bonn.iai.eis.web.model.example;

import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.web.model.AbstractTransformationTest;
import org.junit.Test;

import java.io.IOException;

public class AragonExampleDataTransformationTest extends AbstractTransformationTest {
    @Test
    public void testTransformation() throws IOException {
        AragonExampleDataTransformation transformation = new AragonExampleDataTransformation();

        Pipeline pipeline = transformation.createPipeline("data.ttl", "/tmp", aBuDaTConfiguration);

        assertPipelineIsCorrect(pipeline, "aragon_data_transformation_create_pipeline_expected.json");
    }
}