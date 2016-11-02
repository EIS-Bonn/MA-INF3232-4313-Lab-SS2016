package de.uni.bonn.iai.eis.etl.linkedpipes;

/**
 * Represents an empty linkedpipes pipeline.
 */
public class EmptyPipelineResponseEntity {
    private String id;
    private String uri;

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
}