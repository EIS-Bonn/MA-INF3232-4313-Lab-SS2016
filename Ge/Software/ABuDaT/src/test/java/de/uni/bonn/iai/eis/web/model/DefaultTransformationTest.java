package de.uni.bonn.iai.eis.web.model;

import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultTransformationTest {

    //Note: getters and setters are tested in PojoTest

    @Test
    public void removeMapping() throws Exception {
        DataTransformation transformation = new DataTransformation();

        ArrayList<Mapping> mappings = new ArrayList<>();
        mappings.add(new Mapping());
        transformation.setMappings(mappings);

        transformation.removeMapping(0);

        assertTrue(transformation.getMappings().isEmpty());
    }

    @Test
    public void addSparqlUpdate() throws Exception {
        DataTransformation transformation = new DataTransformation();
        transformation.addSparqlUpdate(new SparqlUpdate());

        assertFalse(transformation.getSparqlUpdates().isEmpty());
    }

    @Test
    public void removeSparqlUpdate() throws Exception {
        DataTransformation transformation = new DataTransformation();

        ArrayList<SparqlUpdate> updates = new ArrayList<>();
        updates.add(new SparqlUpdate());
        transformation.setSparqlUpdates(updates);

        transformation.removeSparqlUpdate(0);

        assertTrue(transformation.getSparqlUpdates().isEmpty());
    }

}