package de.uni.bonn.iai.eis.etl.component;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public abstract class AbstractComplexComponentTest<T extends ComplexComponent> extends AbstractSimpleComponentTest<T> {

    @Test
    public void testGetSpecificConfiguration() {
        T componentUnderTest = getComponentUnderTest();
        componentUnderTest.setConfigurationGraphURL("http://example.com/component/foo/bar");
        assertNotNull(componentUnderTest.getSpecificConfiguration());
    }

}
