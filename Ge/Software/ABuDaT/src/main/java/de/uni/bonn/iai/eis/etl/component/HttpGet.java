package de.uni.bonn.iai.eis.etl.component;


import com.linkedpipes.etl.component.api.Component;
import com.linkedpipes.plugin.extractor.httpget.HttpGetConfiguration;
import de.uni.bonn.iai.eis.etl.linkedpipes.ConfigToRdf;
import org.openrdf.model.Model;

/**
 * Wrapper for a linkedpipes HttpGet component.
 */
public class HttpGet extends ComplexComponent {
    private HttpGetConfiguration httpGetConfiguration;

    public HttpGet(HttpGetConfiguration httpGetConfiguration) {
        this.httpGetConfiguration = httpGetConfiguration;
    }

    @Override
    public Model getSpecificConfiguration() {
        return new ConfigToRdf().modelFromAnnotatedObject(httpGetConfiguration, resourceConfigURL, getConfigurationGraphURL());
    }

    @Override
    public Class<? extends Component.Sequential> getBase() {
        return com.linkedpipes.plugin.extractor.httpget.HttpGet.class;
    }

    @Override
    public String getTemplateName() {
        return "e-httpGetFile";
    }
}
