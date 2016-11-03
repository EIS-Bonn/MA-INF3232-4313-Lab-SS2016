package de.uni.bonn.iai.eis.web.model;

import de.uni.bonn.iai.eis.etl.Pipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;

@RunWith(MockitoJUnitRunner.class)
public class CodelistTransformationTest extends AbstractTransformationTest {

    @Test
    public void testCreatePipeline() throws Exception {

        CodelistTransformation transformation = new CodelistTransformation();

        CodelistSlice codelistSlice1 = new CodelistSlice();
        codelistSlice1.setStartRow(1);
        codelistSlice1.setEndRow(5);
        codelistSlice1.setId("2");
        codelistSlice1.setKeyColumn("col1");
        codelistSlice1.setLabelColumn("col2");
        codelistSlice1.setCodelistTransformation(transformation);

        CodelistSlice codelistSlice2 = new CodelistSlice();
        codelistSlice2.setStartRow(6);
        codelistSlice2.setEndRow(100);
        codelistSlice2.setId("2");
        codelistSlice2.setKeyColumn("col3");
        codelistSlice2.setLabelColumn("col4");
        codelistSlice2.setCodelistTransformation(transformation);
        codelistSlice2.setDoSlice(true);


        transformation.setId("1");
        transformation.setUri("http://example.com/codelist");
        transformation.setCharset("UTF-8");
        transformation.setName("name");
        transformation.setSource("http://example.com/source");
        transformation.setDescription("description");
        transformation.setTableType("csv");
        transformation.setColumns(new LinkedList<>(Arrays.asList("col1", "col2", "col3", "col4")));
        transformation.addSlice(codelistSlice1);
        transformation.addSlice(codelistSlice2);

        Pipeline pipeline = transformation.createPipeline("data.ttl", "/tmp", aBuDaTConfiguration);

        assertPipelineIsCorrect(pipeline, "codelist_transformation_create_pipeline_expected.json");
    }


}