package de.uni.bonn.iai.eis.etl.component;


import com.linkedpipes.etl.component.api.Component;
import com.linkedpipes.plugin.transformer.textHolder.TextHolderConfiguration;
import de.uni.bonn.iai.eis.etl.linkedpipes.ConfigToRdf;
import org.openrdf.model.Model;

/**
 * Wrapper for a linkedpipes TextHolder component.
 */
public class TextHolder extends ComplexComponent {
    private TextHolderConfiguration configuration;

    public TextHolder(TextHolderConfiguration textHolderConfiguration) {
        configuration = textHolderConfiguration;
    }

    @Override
    public Model getSpecificConfiguration() {
        return new ConfigToRdf().modelFromAnnotatedObject(configuration, resourceConfigURL, getConfigurationGraphURL());
    }

    @Override
    public Class<? extends Component.Sequential> getBase() {
        return com.linkedpipes.plugin.transformer.textHolder.TextHolder.class;
    }

    @Override
    public String getTemplateName() {
        return "e-textHolder";
    }
}
