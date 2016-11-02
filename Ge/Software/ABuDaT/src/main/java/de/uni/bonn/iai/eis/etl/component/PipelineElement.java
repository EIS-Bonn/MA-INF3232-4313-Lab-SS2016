package de.uni.bonn.iai.eis.etl.component;


import org.openrdf.model.Statement;

import java.util.ArrayList;
import java.util.Collection;

/**
 * All relevant parts of a pipeline element
 */
public class PipelineElement {
    private String parentPipelineURI;
    private String uri;

    private Collection<Statement> statements = new ArrayList<>();

    /**
     * Getter for the uri of the element
     *
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * Setter for the uri of the element
     *
     * @param uri the uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Getter for the statements (rdf triples) the element consists of
     *
     * @return the statements
     */
    public Collection<Statement> getStatements() {
        return statements;
    }

    /**
     * Setter for the statements (rdf triples) the element consists of
     *
     * @param statements the statements
     */
    public void setStatements(Collection<Statement> statements) {
        this.statements = statements;
    }

    /**
     * Method that adds a statement to the collection of statements the elements consists of.
     *
     * @param statement statement to add
     */
    void addStatement(Statement statement) {
        statements.add(statement);
    }

    /**
     * Method that adds a collection of statements to the collection of statements the elements consists of.
     *
     * @param statements collection of statements to add
     */
    public void addStatements(Collection<Statement> statements) {
        this.statements.addAll(statements);
    }

    /**
     * Setter for the uri of the pipeline that this element belongs to.
     *
     * @param parentPipelineURI the uri of the parent pipeline
     */
    public void setParentPipelineURI(String parentPipelineURI) {
        this.parentPipelineURI = parentPipelineURI;
    }

    /**
     * Getter for the uri of the pipeline that this element belongs to.
     *
     * @return the uri of the parent pipeline
     */
    public String getParentPipelineURI() {
        return parentPipelineURI;
    }
}
