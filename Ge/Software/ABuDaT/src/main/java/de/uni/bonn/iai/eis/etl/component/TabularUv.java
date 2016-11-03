package de.uni.bonn.iai.eis.etl.component;


import com.linkedpipes.etl.component.api.Component;
import com.linkedpipes.plugin.transformer.tabularuv.TabularConfig_V2;
import de.uni.bonn.iai.eis.etl.linkedpipes.ConfigToRdf;
import org.openrdf.model.Model;

/**
 * Wrapper for a linkedpipes TabularUv component.
 */
public class TabularUv extends ComplexComponent {
    private TabularConfig_V2 tabularConfig_v2;

    public TabularUv(TabularConfig_V2 tabularConfig_v2) {
        this.tabularConfig_v2 = tabularConfig_v2;
    }

    @Override
    public Model getSpecificConfiguration() {
        return new ConfigToRdf().modelFromAnnotatedObject(tabularConfig_v2, resourceConfigURL, getConfigurationGraphURL());
    }

    @Override
    public Class<? extends Component.Sequential> getBase() {
        return com.linkedpipes.plugin.transformer.tabularuv.Tabular.class;
    }

    @Override
    public String getTemplateName() {
        return "t-tabularUv";
    }
}
