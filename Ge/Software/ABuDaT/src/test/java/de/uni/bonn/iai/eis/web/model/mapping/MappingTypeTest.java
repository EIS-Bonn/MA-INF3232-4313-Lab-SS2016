package de.uni.bonn.iai.eis.web.model.mapping;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MappingTypeTest {
    @Test
    public void testMappingTypeFromString() {
        for (MappingType mappingType : MappingType.values()) {
            assertEquals(MappingType.valueOf(mappingType.name()), mappingType);
        }
    }
}