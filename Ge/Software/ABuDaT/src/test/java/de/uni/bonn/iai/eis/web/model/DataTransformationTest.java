package de.uni.bonn.iai.eis.web.model;

import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.rdf.obeu.OperationCharacter;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import de.uni.bonn.iai.eis.web.model.mapping.MappingType;
import org.junit.Test;

import java.util.ArrayList;

public class DataTransformationTest extends AbstractTransformationTest {
    @Test
    public void testCreatePipeline() throws Exception {
        DataTransformation transformation = new DataTransformation();

        transformation.setId("1");
        transformation.setSource("http://example.com/source");
        transformation.setDsdUrl("http://example.com/dsd/1");
        transformation.setDsdLabel("dsd label");
        transformation.setCharset("UTF-8");
        transformation.setName("name");
        transformation.setDatasetUrl("http://example.com/dsd/1");
        transformation.setFiscalPeriod("http://example.com/fiscalPeriod");
        transformation.setCurrency("http://example.com/EUR");
        transformation.setBudgetaryUnit("http://example.com/budgetaryUnit");
        transformation.setDelimiterChar(";");
        transformation.setTableType("csv");
        transformation.setOperationCharacter(OperationCharacter.EXPENDITURE);

        Mapping mapping1 = new Mapping();
        mapping1.setUri("http://example.com/mapping/1");
        mapping1.setName("name");
        mapping1.setType(MappingType.String.name());
        mapping1.setComponentProperty("http://example.com/comProp/1");

        Mapping mapping2 = new Mapping();
        mapping2.setUri("http://example.com/mapping/2");
        mapping2.setName("name");
        mapping2.setComponentProperty("http://example.com/comProp/1");

        ArrayList<Mapping> mappings = new ArrayList<>();
        mappings.add(mapping1);
        mappings.add(mapping2);

        transformation.setMappings(mappings);

        CustomMeasure customMeasure = new CustomMeasure();
        customMeasure.setIri("http://example.com/measure/1");
        customMeasure.setLabel("label");
        customMeasure.setComment("comment");
        customMeasure.setDefinedBy("http://example.com/a");
        customMeasure.setSubPropertyOf("http://example.com/measure/0");
        transformation.addCustomMeasure(customMeasure);

        CustomDimension customDimension = new CustomDimension();
        customDimension.setIri("http://example.com/dimension/1");
        customDimension.setLabel("label");
        customDimension.setComment("comment");
        customDimension.setDefinedBy("http://example.com/a");
        customDimension.setSubPropertyOf("http://example.com/dimension/0");
        customDimension.setCodeList("http://example.com/codelists/1");
        customDimension.setRangeProperty("http://example.com/range/1");
        transformation.addCustomDimension(customDimension);

        SparqlUpdate sparqlUpdate = new SparqlUpdate();
        sparqlUpdate.setQuery("a query");
        transformation.addSparqlUpdate(sparqlUpdate);

        Pipeline pipeline = transformation.createPipeline("data.ttl", "/tmp", aBuDaTConfiguration);

        assertPipelineIsCorrect(pipeline, "data_transformation_create_pipeline_expected.json");
    }

}