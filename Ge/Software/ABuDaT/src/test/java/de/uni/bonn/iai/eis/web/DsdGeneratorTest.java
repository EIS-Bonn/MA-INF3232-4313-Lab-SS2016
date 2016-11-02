package de.uni.bonn.iai.eis.web;

import de.uni.bonn.iai.eis.TestHelper;
import de.uni.bonn.iai.eis.rdf.obeu.BudgetPhase;
import de.uni.bonn.iai.eis.rdf.obeu.OperationCharacter;
import de.uni.bonn.iai.eis.web.model.CustomDimension;
import de.uni.bonn.iai.eis.web.model.CustomMeasure;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Model;
import org.openrdf.model.util.Models;
import org.openrdf.rio.RDFFormat;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DsdGeneratorTest {
    private DataTransformation transformation;

    @Before
    public void before() {
        transformation = new DataTransformation();
        transformation.setDsdUrl("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        transformation.setBudgetaryUnit("http://dbpedia.org/resource/European_Union");
        transformation.setCurrency("http://data.openbudgets.eu/codelist/currency/EUR");
        transformation.setFiscalPeriod("http://reference.data.gov.uk/id/year/2014");
        transformation.setDatasetUrl("http://data.openbudgets.eu/resource/dataset/ESIF-2014-2020");
        transformation.setBudgetPhase(BudgetPhase.EXECUTED);
        transformation.setOperationCharacter(OperationCharacter.EXPENDITURE);

        ArrayList<CustomDimension> customDimensions = new ArrayList<>();

        CustomDimension customDimensionA = new CustomDimension();
        customDimensionA.setIri("http://example.com/dimension/a");
        customDimensions.add(customDimensionA);

        transformation.setCustomDimensions(customDimensions);

        ArrayList<CustomMeasure> customMeasures = new ArrayList<>();

        CustomMeasure customMeasureA = new CustomMeasure();
        customMeasureA.setIri("http://example.com/measure/a");
        customMeasures.add(customMeasureA);

        transformation.setCustomMeasures(customMeasures);


        ArrayList<Mapping> mappings = new ArrayList<>();

        Mapping mapping1 = new Mapping();
        mapping1.setUri("http://example.com/dimension/b");
        mappings.add(mapping1);

        Mapping mapping2 = new Mapping();
        mapping2.setComponentProperty("http://example.com/dimension/a");
        mappings.add(mapping2);

        Mapping mapping3 = new Mapping();
        mapping3.setUri("http://example.com/measure/a");
        mapping3.setIsAmount(true);
        mappings.add(mapping3);

        Mapping mapping4 = new Mapping();
        mapping4.setComponentProperty("http://example.com/measure/b");
        mapping4.setIsAmount(true);
        mappings.add(mapping4);

        transformation.setMappings(mappings);

    }

    @Test
    public void generateDsdData() throws Exception {
        DsdGenerator dsdGenerator = new DsdGenerator();
        String actual = dsdGenerator.generateDsdData(transformation, RDFFormat.TURTLE);

        String expected;
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("generated_data.ttl")) {
            expected = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        }

        assertEquals(expected, actual);
    }

    @Test
    public void generateRDF() throws Exception {
        DsdGenerator dsdGenerator = new DsdGenerator();
        String generatedRDF = dsdGenerator.generateRDF(transformation, RDFFormat.TURTLE);

        Pattern pattern = Pattern.compile("(_:node\\w*)");
        Matcher matcher = pattern.matcher(generatedRDF);

        Set<String> toReplace = new HashSet<>();
        while (matcher.find()) {
            String group = matcher.group(1);
            toReplace.add(group);
        }

        String actual = generatedRDF;
        int index = 0;
        for (String s : toReplace) {
            actual = actual.replaceAll(s, "_:bNode" + index++);
        }

        Model actualModel;
        try (InputStream inputStream = IOUtils.toInputStream(generatedRDF, Charset.forName("UTF-8"))) {
            actualModel = TestHelper.modelFromStream(inputStream, RDFFormat.TURTLE);
        }

        Model expectedModel;
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("generated_dsd.ttl")) {
            expectedModel = TestHelper.modelFromStream(resourceAsStream, RDFFormat.TURTLE);
        }

        assertEquals(expectedModel.size(), actualModel.size());
        assertTrue(Models.isSubset(expectedModel, actualModel));
    }

}