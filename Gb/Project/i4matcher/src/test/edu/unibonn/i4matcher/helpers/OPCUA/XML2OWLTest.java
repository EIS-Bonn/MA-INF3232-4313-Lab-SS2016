package edu.unibonn.i4matcher.helpers.OPCUA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Writer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XML2OWLTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(XML2OWLTest.class);

    @Test
    public void testWriter() {

        // This part converts XML schema to OWL ontology.
        XSD2OWLMapper mapping = new XSD2OWLMapper(new File("src/main/resources/opcua.xsd"));
        mapping.setObjectPropPrefix("");
        mapping.setDataTypePropPrefix("has");
        mapping.convertXSD2OWL();

        // This part converts XML instance to RDF data model.
        XML2OWLMapper generator = new XML2OWLMapper(new File("src/test/resources/Topology.xml"), mapping);
        generator.convertXML2OWL();

        // This part prints the RDF data model to the specified file.
        try {
            File f = new File("src/test/resources/output/test-opcua.ttl");
            f.getParentFile().mkdirs();
            Writer writer = new FileWriter(f);
            generator.writeModel(writer, "ttl");
            writer.close();

        } catch (Exception e) {
            LOGGER.error("{}", e.getMessage());
        }
    }
}