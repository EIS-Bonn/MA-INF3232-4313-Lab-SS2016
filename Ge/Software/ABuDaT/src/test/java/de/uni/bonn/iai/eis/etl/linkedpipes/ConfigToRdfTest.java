package de.uni.bonn.iai.eis.etl.linkedpipes;

import com.linkedpipes.etl.component.api.service.RdfToPojo;
import com.linkedpipes.plugin.extractor.httpget.HttpGetConfiguration;
import org.junit.Test;
import org.openrdf.model.*;
import org.openrdf.model.impl.SimpleValueFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ConfigToRdfTest {

    @Test
    public void testWriteRdf() {
        HttpGetConfiguration httpGetConfiguration = new HttpGetConfiguration();
        String fileName = "filename.ext";
        String fileURI = "http://example.com/file.ext";

        httpGetConfiguration.setFileName(fileName);
        httpGetConfiguration.setUri(fileURI);

        String configGraphURI = "http://foobar/component/id";
        String resourceConfigURI = "http://localhost/resources/configuration";


        Model model = new ConfigToRdf().modelFromAnnotatedObject(httpGetConfiguration, resourceConfigURI, configGraphURI);

        ValueFactory valueFactory = SimpleValueFactory.getInstance();

        assertTrue(model.contains(
                valueFactory.createIRI(resourceConfigURI),
                valueFactory.createIRI("http://plugins.linkedpipes.com/ontology/e-httpGetFile#fileUri"),
                valueFactory.createLiteral(fileURI),
                valueFactory.createIRI(configGraphURI))
        );

        assertTrue(model.contains(
                valueFactory.createIRI(resourceConfigURI),
                valueFactory.createIRI("http://plugins.linkedpipes.com/ontology/e-httpGetFile#fileName"),
                valueFactory.createLiteral(fileName),
                valueFactory.createIRI(configGraphURI))
        );

        assertTrue(model.contains(
                valueFactory.createIRI(resourceConfigURI),
                valueFactory.createIRI("http://plugins.linkedpipes.com/ontology/e-httpGetFile#hardRedirect"),
                valueFactory.createLiteral(false),
                valueFactory.createIRI(configGraphURI))
        );
    }

    class Unknown {
    }

    @RdfToPojo.Type(
            uri = "http://plugins.linkedpipes.com/ontology/e-someComponent#Configuration"
    )
    public class SomeComponenConfiguration {
        @RdfToPojo.Property(
                uri = "http://plugins.linkedpipes.com/ontology/e-someComponent#aField"
        )
        private Unknown aField = new Unknown();
    }

    @Test
    public void testWriteRdfFailure() {
        //Note: may occur if new components are added and the developer forgot
        //to implement a certain handling for unknown field type contained in the object
        //and annotated with RdfToPojo.Property

        String configGraphURI = "http://foobar/component/id";
        String resourceConfigURI = "http://localhost/resources/configuration";

        SomeComponenConfiguration componenConfiguration = new SomeComponenConfiguration();

        try {
            Model model = new ConfigToRdf().modelFromAnnotatedObject(componenConfiguration, resourceConfigURI, configGraphURI);
            fail("RuntimeException expected");
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals(e.getMessage(), "Unknown type, cant write literal for field: aField with type Unknown and URI http://plugins.linkedpipes.com/ontology/e-someComponent#aField");
        }
    }
}
