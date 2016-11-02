package de.uni.bonn.iai.eis.etl.component;

import org.junit.Test;

import static org.junit.Assert.*;

public class GraphUnionTest extends AbstractSimpleComponentTest<GraphUnion> {
    @Test
    public void testCreateGraphUnion() {
        GraphUnion graphUnion = getComponentUnderTest();

        assertNotNull(graphUnion);
        assertNotNull(graphUnion.getInputPort());
        assertNotNull(graphUnion.getOutputPort());
    }

    @Override
    GraphUnion getComponentUnderTest() {
        return new GraphUnion();
    }
}