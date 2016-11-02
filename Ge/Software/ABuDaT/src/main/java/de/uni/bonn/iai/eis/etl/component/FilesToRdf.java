package de.uni.bonn.iai.eis.etl.component;

import com.linkedpipes.etl.component.api.Component;
import com.linkedpipes.plugin.transformer.filesToRdf.FilesToRdfConfiguration;
import de.uni.bonn.iai.eis.etl.linkedpipes.ConfigToRdf;
import org.openrdf.model.Model;

/**
 * Wrapper for a linkedpipes FilesToRdf component.
 */
public class FilesToRdf extends ComplexComponent {
    private FilesToRdfConfiguration filesToRdfConfiguration;

    public FilesToRdf(FilesToRdfConfiguration filesToRdfConfiguration) {
        this.filesToRdfConfiguration = filesToRdfConfiguration;
    }

    @Override
    public Model getSpecificConfiguration() {
        return new ConfigToRdf().modelFromAnnotatedObject(filesToRdfConfiguration, resourceConfigURL, getConfigurationGraphURL());
    }

    @Override
    public Class<? extends Component.Sequential> getBase() {
        return com.linkedpipes.plugin.transformer.filesToRdf.FilesToRdf.class;
    }

    @Override
    public String getTemplateName() {
        return "t-filesToRdf";
    }
}
