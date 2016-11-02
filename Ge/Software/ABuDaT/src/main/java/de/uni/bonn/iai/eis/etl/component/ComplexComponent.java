package de.uni.bonn.iai.eis.etl.component;

import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesOntology;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.SimpleValueFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A representation of a linkedpipes component that has a configuration graph, additionally to
 * the base configuration that every linkedpipes component has.
 */
public abstract class ComplexComponent extends SimpleComponent {
    String resourceConfigURL = "http://localhost/resources/configuration";

    private static final SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();
    private String configurationGraphURL;

    /**
     * Getter for the specific configuration graph url
     *
     * @return the url
     */
    public String getConfigurationGraphURL() {
        return configurationGraphURL;
    }

    /**
     * Getter for the rdf model of the specific configuration (statements of the configuration graph).
     *
     * @return
     */
    public abstract Model getSpecificConfiguration();

    @Override
    public Collection<Statement> getStatements() {
        Collection<Statement> result = new ArrayList<>();

        result.addAll(super.getStatements());

        result.add(valueFactory.createStatement(valueFactory.createIRI(getUri()),
                LinkedPipesOntology.CONFIGURATION_GRAPH_TYPE,
                valueFactory.createIRI(configurationGraphURL),
                getPipelineIRI()));

        result.addAll(getSpecificConfiguration());

        return result;
    }

    /**
     * Setter for the specific configuration graph url.
     *
     * @param configurationGraphURL the url
     */
    public void setConfigurationGraphURL(String configurationGraphURL) {
        this.configurationGraphURL = configurationGraphURL;
    }

    @Override
    public void setParentPipelineURI(String pipelineURI) {
        super.setParentPipelineURI(pipelineURI);

        String componentConfigurationGraphURL = getUri() + "/configuration";
        setConfigurationGraphURL(componentConfigurationGraphURL);
    }
}
