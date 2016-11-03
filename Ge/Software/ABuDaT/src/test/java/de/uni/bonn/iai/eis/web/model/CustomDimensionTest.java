package de.uni.bonn.iai.eis.web.model;

import de.uni.bonn.iai.eis.rdf.obeu.classification.AdministrativeClassification;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openrdf.model.vocabulary.SKOS;
import org.openrdf.rio.RDFFormat;

import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class CustomDimensionTest {
    @Test
    public void generateRdf() throws Exception {

        CustomDimension customDimension = new CustomDimension();

        customDimension.setIri("http://example.com/property");
        customDimension.setLabel("a label");
        customDimension.setComment("a comment");
        customDimension.setSubPropertyOf(new AdministrativeClassification().getIri());
        customDimension.setRangeProperty(SKOS.CONCEPT.toString());
        customDimension.setDefinedBy("http://example.com/dsd/foobar");
        customDimension.setCodeList("http://example.com/codelist/abc");

        String actual = customDimension.generateRdf(RDFFormat.TURTLE);

        String expected;
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("custom_dimension_expected_generated_rdf.ttl")) {
            expected = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        }

        assertEquals(expected, actual);

        //Example:
    //
    //aragon-dimension:functionalClassification a rdf:Property, qb:CodedProperty, qb:DimensionProperty;
    //    rdfs:label "Functional Classification"@en ;
    //    rdfs:comment "Classifies expenditures by general government sector and by the purpose of the expenditure.
    //                  The functional classification is organized hierarchical into groups, functions, sub-functions and programs."@en ;
    //    rdfs:subPropertyOf obeu-dimension:functionalClassification ;
    //    rdfs:range skos:Concept ;
    //    qb:codeList <http://data.openbudgets.eu/resource/codelist/estructura_funcional_aragon_2014> ;
    //    rdfs:isDefinedBy <http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2014> .
    }

}