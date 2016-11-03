package de.uni.bonn.iai.eis.etl;

import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.etl.component.Component;
import de.uni.bonn.iai.eis.etl.component.Connection;
import de.uni.bonn.iai.eis.etl.component.ExtendedConnection;
import de.uni.bonn.iai.eis.etl.component.SimpleComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.UUID;

@org.springframework.stereotype.Component
public class PipelineBuilder {

    private ABuDaTConfiguration aBuDaTConfiguration;

    @Autowired
    public PipelineBuilder(ABuDaTConfiguration aBuDaTConfiguration) {
        this.aBuDaTConfiguration = aBuDaTConfiguration;
    }

    private LinkedList<SimpleComponent> components = new LinkedList<>();
    private LinkedList<Connection> connections = new LinkedList<>();
    private String pipelineLabel;

    private boolean autoConnectComponents = false;

    public Pipeline build(String pipelineId) {
        Pipeline pipeline = new Pipeline();

        pipeline.setId(pipelineId);

        String pipelineURI = aBuDaTConfiguration.getPipelineBaseUrl() + "/" + pipelineId;
        pipeline.setUri(pipelineURI);

        pipeline.setLabel(pipelineLabel);

        for (Component component : components) {
            component.setParentPipelineURI(pipeline.getUri());
        }
        pipeline.setComponents(components);

        for (Connection connection : connections) {
            connection.setParentPipelineURI(pipeline.getUri());
        }
        pipeline.setConnections(connections);

        return pipeline;
    }

    public Pipeline build() throws URISyntaxException, IOException {
        return build(UUID.randomUUID().toString());
    }

    public PipelineBuilder withLabel(String label) {
        pipelineLabel = label;
        return this;
    }

    public PipelineBuilder withComponent(SimpleComponent component) {
        component.setTemplateURI(aBuDaTConfiguration.getComponentBaseUrl() + "/" + component.getTemplateName());

        if (autoConnectComponents) {
            if (!components.isEmpty()) {
                connections.add(new ExtendedConnection(components.getLast(), component));
            }
        }

        components.addLast(component);

        return this;
    }

    public PipelineBuilder withConnection(Connection connection) {
        connections.addLast(connection);
        return this;
    }

    public PipelineBuilder withAutoConnect() {
        return withAutoConnect(true);
    }

    public PipelineBuilder withAutoConnect(boolean doAutoConnect) {
        autoConnectComponents = doAutoConnect;
        return this;
    }

}
