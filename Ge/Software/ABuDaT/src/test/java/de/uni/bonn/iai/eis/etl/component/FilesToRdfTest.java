package de.uni.bonn.iai.eis.etl.component;

import com.linkedpipes.plugin.transformer.filesToRdf.FilesToRdfConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FilesToRdfTest extends AbstractComplexComponentTest<FilesToRdf> {
    @Test
    public void testCreateFilesToRdf() {
        FilesToRdf filesToRdf = getComponentUnderTest();

        assertNotNull(filesToRdf);
        assertNotNull(filesToRdf.getInputPort());
        assertNotNull(filesToRdf.getOutputPort());
    }

    @Override
    FilesToRdf getComponentUnderTest() {
        return new FilesToRdf(new FilesToRdfConfiguration());
    }
}
