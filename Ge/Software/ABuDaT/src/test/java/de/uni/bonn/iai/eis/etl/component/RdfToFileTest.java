package de.uni.bonn.iai.eis.etl.component;

import com.linkedpipes.plugin.transformer.rdftofile.RdfToFileConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RdfToFileTest extends AbstractComplexComponentTest<RdfToFile> {
    @Test
    public void testCreateRdfToFile() {
        RdfToFile rdfToFile = getComponentUnderTest();
        assertNotNull(rdfToFile);
        assertNotNull(rdfToFile.getInputPort());
        assertNotNull(rdfToFile.getOutputPort());
    }

    @Override
    RdfToFile getComponentUnderTest() {
        return new RdfToFile(new RdfToFileConfiguration());
    }
}