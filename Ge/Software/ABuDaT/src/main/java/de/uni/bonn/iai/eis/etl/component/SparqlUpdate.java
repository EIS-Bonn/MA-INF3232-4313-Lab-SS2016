package de.uni.bonn.iai.eis.etl.component;


import com.linkedpipes.etl.component.api.Component;
import com.linkedpipes.plugin.transformer.sparql.update.SparqlUpdateConfiguration;
import de.uni.bonn.iai.eis.etl.linkedpipes.ConfigToRdf;
import de.uni.bonn.iai.eis.etl.linkedpipes.PortHelper;
import org.openrdf.model.Model;

/**
 * Wrapper for a linkedpipes SparqlUpdate component.
 */
public class SparqlUpdate extends ComplexComponent {
    private SparqlUpdateConfiguration sparqlUpdateConfiguration;

    public SparqlUpdate(SparqlUpdateConfiguration sparqlUpdateConfiguration) {
        this.sparqlUpdateConfiguration = sparqlUpdateConfiguration;
    }

    @Override
    public Model getSpecificConfiguration() {
        return new ConfigToRdf().modelFromAnnotatedObject(sparqlUpdateConfiguration, resourceConfigURL, getConfigurationGraphURL());
    }

    @Override
    public Class<? extends Component.Sequential> getBase() {
        return com.linkedpipes.plugin.transformer.sparql.update.SparqlUpdate.class;
    }

    @Override
    public String getTemplateName() {
        return "t-sparqlUpdate";
    }

    @Override
    String getInputPort() {
        return PortHelper.getInputPort(getBase(), "InputRdf");
    }
}
