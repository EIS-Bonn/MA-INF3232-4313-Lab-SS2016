package de.uni.bonn.iai.eis.etl.component;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public abstract class AbstractSimpleComponentTest<T extends SimpleComponent> {

    abstract T getComponentUnderTest();

    @Test
    public void testGetTemplateName() {
        assertNotNull(getComponentUnderTest().getTemplateName());
    }

    @Test
    public void testGetBase(){
        assertNotNull(getComponentUnderTest().getBase());
    }


}
