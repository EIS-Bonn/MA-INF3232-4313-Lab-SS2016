package de.uni.bonn.iai.eis.etl.component;


import com.linkedpipes.etl.component.api.Component;
import com.linkedpipes.plugin.transformer.singleGraphUnion.SingleGraphUnion;

public class GraphUnion extends SimpleComponent {
    @Override
    public Class<? extends Component.Sequential> getBase() {
        return SingleGraphUnion.class;
    }

    @Override
    public String getTemplateName() {
        return "t-singleGraphUnion";
    }
}
