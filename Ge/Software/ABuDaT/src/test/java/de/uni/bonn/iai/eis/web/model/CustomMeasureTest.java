package de.uni.bonn.iai.eis.web.model;

import de.uni.bonn.iai.eis.rdf.obeu.measure.Amount;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openrdf.rio.RDFFormat;

import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class CustomMeasureTest {
    @Test
    public void generateRdf() throws Exception {
        CustomMeasure customMeasure = new CustomMeasure();

        customMeasure.setIri("http://example.com/measure");
        customMeasure.setLabel("A label.");
        customMeasure.setComment("A comment.");
        customMeasure.setSubPropertyOf(new Amount().getIri());
        customMeasure.setDefinedBy("http://example.com/dsd/foobar");

        String actual = customMeasure.generateRdf(RDFFormat.TURTLE);

        String expected;
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("custom_measure_expected_generated_rdf.ttl")) {
            expected = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        }

        assertEquals(expected, actual);
    }

    //example:
    //
    //esif-measure:amountTotal a rdf:Property, qb:MeasureProperty ;
    //    rdfs:label "Amount Total"@en ;
    //    rdfs:comment "Sum of the amount funded by the resp. EU fund and the national part."@en ;
    //    rdfs:subPropertyOf obeu-measure:amount ;
    //    rdfs:isDefinedBy <http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020> .

}