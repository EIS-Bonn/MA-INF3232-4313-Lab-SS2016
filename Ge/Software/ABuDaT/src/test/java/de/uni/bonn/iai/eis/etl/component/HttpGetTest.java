package de.uni.bonn.iai.eis.etl.component;

import com.linkedpipes.plugin.extractor.httpget.HttpGetConfiguration;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpGetTest extends AbstractComplexComponentTest<HttpGet> {
    @Test
    public void testCreateHttpGetHasOutputPort() {
        HttpGet httpGet = getComponentUnderTest();

        assertNotNull(httpGet);
        assertNotNull(httpGet.getOutputPort());
        assertNull(httpGet.getInputPort());
    }

    @Override
    HttpGet getComponentUnderTest() {
        return new HttpGet(new HttpGetConfiguration());
    }
}