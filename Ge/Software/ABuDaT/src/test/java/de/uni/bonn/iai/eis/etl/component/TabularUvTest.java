package de.uni.bonn.iai.eis.etl.component;

import com.linkedpipes.plugin.transformer.tabularuv.TabularConfig_V2;
import org.junit.Test;

import static org.junit.Assert.*;

public class TabularUvTest extends AbstractComplexComponentTest<TabularUv> {
    @Test
    public void testCreateTabularUv(){
        TabularUv tabularUv = getComponentUnderTest();

        assertNotNull(tabularUv);
        assertNotNull(tabularUv.getInputPort());
        assertNotNull(tabularUv.getOutputPort());
    }

    @Override
    TabularUv getComponentUnderTest() {
        return new TabularUv(new TabularConfig_V2());
    }
}