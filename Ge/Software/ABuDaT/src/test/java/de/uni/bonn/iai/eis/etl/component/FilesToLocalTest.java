package de.uni.bonn.iai.eis.etl.component;

import com.linkedpipes.plugin.loader.local.LoaderLocalConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FilesToLocalTest extends AbstractComplexComponentTest<FilesToLocal> {
    @Test
    public void testCreateFilesToLocal() {
        FilesToLocal filesToLocal = getComponentUnderTest();

        assertNotNull(filesToLocal);
        assertNotNull(filesToLocal.getInputPort());
        assertNull(filesToLocal.getOutputPort());
    }

    @Override
    FilesToLocal getComponentUnderTest() {
        return new FilesToLocal(new LoaderLocalConfiguration());
    }
}