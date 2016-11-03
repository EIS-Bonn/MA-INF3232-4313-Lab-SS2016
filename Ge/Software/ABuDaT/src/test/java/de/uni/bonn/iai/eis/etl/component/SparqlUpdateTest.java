package de.uni.bonn.iai.eis.etl.component;

import com.linkedpipes.plugin.transformer.sparql.update.SparqlUpdateConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SparqlUpdateTest extends AbstractComplexComponentTest<SparqlUpdate> {
    @Test
    public void testCreateSparqlUpdate() {
        SparqlUpdate sparqlUpdate = getComponentUnderTest();
        assertNotNull(sparqlUpdate.getId());
        assertNotNull(sparqlUpdate);
        assertNotNull(sparqlUpdate.getInputPort());
        assertNotNull(sparqlUpdate.getOutputPort());
    }

    @Override
    SparqlUpdate getComponentUnderTest() {
        return new SparqlUpdate(new SparqlUpdateConfiguration());
    }
}