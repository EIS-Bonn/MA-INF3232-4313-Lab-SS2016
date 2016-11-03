package de.uni.bonn.iai.eis.etl;

import de.uni.bonn.iai.eis.etl.component.Component;
import de.uni.bonn.iai.eis.etl.component.Connection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class representing an ETL pipeline.
 */
public class Pipeline {
    private String id;
    private String uri;
    private String label;

    private List<? extends Component> components = Collections.emptyList();
    private List<Connection> connections = Collections.emptyList();

    public boolean hasLabel() {
        return label != null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Collection<? extends Component> getComponents() {
        return components;
    }

    public void setComponents(List<? extends Component> components) {
        this.components = components;
    }

    public Collection<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

}