package de.uni.bonn.iai.eis.etl.component;

import org.junit.Test;

import static org.junit.Assert.*;

public class GraphMergerTest extends AbstractSimpleComponentTest<GraphMerger> {
    @Test
    public void testGraphMerger() {
        GraphMerger graphMerger = getComponentUnderTest();

        assertNotNull(graphMerger);
        assertNotNull(graphMerger.getInputPort());
        assertNotNull(graphMerger.getOutputPort());
    }

    @Override
    GraphMerger getComponentUnderTest() {
        return new GraphMerger();
    }
}