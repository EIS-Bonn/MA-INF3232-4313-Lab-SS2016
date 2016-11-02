package de.uni.bonn.iai.eis.etl.component;


import com.linkedpipes.etl.component.api.Component;

/**
 * Wrapper for a linkedpipes GraphMerger component.
 */
public class GraphMerger extends SimpleComponent {
    @Override
    public Class<? extends Component.Sequential> getBase() {
        return com.linkedpipes.plugin.transformer.graphmerger.GraphMerger.class;
    }

    @Override
    public String getTemplateName() {
        return "t-graphMerger";
    }
}
