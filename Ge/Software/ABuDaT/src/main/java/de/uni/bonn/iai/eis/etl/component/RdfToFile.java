package de.uni.bonn.iai.eis.etl.component;


import com.linkedpipes.etl.component.api.Component;
import com.linkedpipes.plugin.transformer.rdftofile.RdfToFileConfiguration;
import de.uni.bonn.iai.eis.etl.linkedpipes.ConfigToRdf;
import org.openrdf.model.Model;

/**
 * Wrapper for a linkedpipes RdfToFile component.
 */
public class RdfToFile extends ComplexComponent {
    private RdfToFileConfiguration rdfToFileConfiguration;

    public RdfToFile(RdfToFileConfiguration rdfToFileConfiguration) {
        this.rdfToFileConfiguration = rdfToFileConfiguration;
    }

    @Override
    public Model getSpecificConfiguration() {
        return new ConfigToRdf().modelFromAnnotatedObject(rdfToFileConfiguration, resourceConfigURL, getConfigurationGraphURL());
    }

    @Override
    public Class<? extends Component.Sequential> getBase() {
        return com.linkedpipes.plugin.transformer.rdftofile.RdfToFile.class;
    }

    @Override
    public String getTemplateName() {
        return "t-rdfToFile";
    }
}
