package de.uni.bonn.iai.eis.etl.component;

import com.linkedpipes.plugin.transformer.textHolder.TextHolderConfiguration;
import org.junit.Test;

import static org.junit.Assert.*;

public class TextHolderTest extends AbstractComplexComponentTest<TextHolder> {
    @Test
    public void testCreateTextHolder() {
        TextHolder textHolder = getComponentUnderTest();

        assertNotNull(textHolder);
        assertNull(textHolder.getInputPort());
        assertNotNull(textHolder.getOutputPort());
    }

    @Override
    TextHolder getComponentUnderTest() {
        return new TextHolder(new TextHolderConfiguration());
    }
}