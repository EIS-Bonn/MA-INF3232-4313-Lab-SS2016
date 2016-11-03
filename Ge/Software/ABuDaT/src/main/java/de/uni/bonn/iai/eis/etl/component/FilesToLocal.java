package de.uni.bonn.iai.eis.etl.component;

import com.linkedpipes.etl.component.api.Component;
import com.linkedpipes.plugin.loader.local.LoaderLocal;
import com.linkedpipes.plugin.loader.local.LoaderLocalConfiguration;
import de.uni.bonn.iai.eis.etl.linkedpipes.ConfigToRdf;
import org.openrdf.model.Model;

/**
 * Wrapper for a linkedpipes FilesToLocal component.
 */
public class FilesToLocal extends ComplexComponent {
    private LoaderLocalConfiguration loaderLocalConfiguration;

    public FilesToLocal(LoaderLocalConfiguration loaderLocalConfiguration) {
        this.loaderLocalConfiguration = loaderLocalConfiguration;
    }

    @Override
    public Model getSpecificConfiguration() {
        return new ConfigToRdf().modelFromAnnotatedObject(loaderLocalConfiguration, resourceConfigURL, getConfigurationGraphURL());
    }

    @Override
    public Class<? extends Component.Sequential> getBase() {
        return LoaderLocal.class;
    }

    @Override
    public String getTemplateName() {
        return "l-filesToLocal";
    }
}
