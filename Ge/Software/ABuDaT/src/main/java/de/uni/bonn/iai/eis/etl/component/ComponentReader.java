package de.uni.bonn.iai.eis.etl.component;


import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesOntology;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.SimpleValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class ComponentReader {
    private ComponentReader() {

    }

    private static final Logger log = LoggerFactory.getLogger(ComponentReader.class);

    public static Component get(Resource componentURI, Model model, Resource pipelineURI) {
        Component component = new Component();

        log.info(String.format("Component found: %s", componentURI.stringValue()));

        Model generalConfiguration = model.filter(componentURI, null, null, pipelineURI);

        String componentConfigurationGraphURL = getComponentConfigurationGraphURI(pipelineURI, generalConfiguration, componentURI);

        if (componentConfigurationGraphURL != null) {
            Resource configurationGraphURL = SimpleValueFactory.getInstance().createIRI(componentConfigurationGraphURL);
            Model specificConfiguartion = model.filter(null, null, null, configurationGraphURL);
            component.addStatements(specificConfiguartion);
        }

        component.setUri(componentURI.toString());
        component.addStatements(generalConfiguration);

        return component;
    }

    private static String getComponentConfigurationGraphURI(Resource pipelineURI, Model componentConfigurations, Resource componentURI) {
        Model filter = componentConfigurations.filter(componentURI, LinkedPipesOntology.CONFIGURATION_GRAPH_TYPE, null, pipelineURI);

        Iterator<Statement> iterator = filter.iterator();
        if (iterator.hasNext()) {
            Statement next = iterator.next();

            return next.getObject().stringValue();
        }

        return null;
    }
}
