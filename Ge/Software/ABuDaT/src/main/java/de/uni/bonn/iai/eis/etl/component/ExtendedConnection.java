package de.uni.bonn.iai.eis.etl.component;

import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesOntology;
import org.openrdf.model.IRI;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.vocabulary.RDF;

import java.util.UUID;

//TODO find a better name
public class ExtendedConnection extends Connection {
    private String id;

    private SimpleComponent sourceComponent;
    private SimpleComponent targetComponent;

    public ExtendedConnection(SimpleComponent from, SimpleComponent to) {
        this.sourceComponent = from;
        this.targetComponent = to;

        id = UUID.randomUUID().toString();
    }

    public void setParentPipelineURI(String pipelineURI) {
        IRI pipelineIRI = SimpleValueFactory.getInstance().createIRI(pipelineURI);

        String connectionIRIString = pipelineURI + "/connection/" + getId();
        setUri(connectionIRIString);

        IRI connectionIRI = SimpleValueFactory.getInstance().createIRI(connectionIRIString);

        //TODO this is a subset of what happens for generalConfiguration in case of components - maybe it can be generalised
        //TODO maybe something like commonGeneral  vs. specificGeneral config
        addStatement(SimpleValueFactory.getInstance().createStatement(
                connectionIRI, RDF.TYPE, LinkedPipesOntology.CONNECTION_TYPE, pipelineIRI));

        addStatement(SimpleValueFactory.getInstance().createStatement(
                connectionIRI,
                LinkedPipesOntology.CONNECTION_SOURCE,
                SimpleValueFactory.getInstance().createIRI(sourceComponent.getUri()),
                pipelineIRI));

        addStatement(SimpleValueFactory.getInstance().createStatement(
                connectionIRI, LinkedPipesOntology.CONNECTION_SOURCE_BINDING,
                SimpleValueFactory.getInstance().createLiteral(sourceComponent.getOutputPort()),
                pipelineIRI));

        addStatement(SimpleValueFactory.getInstance().createStatement(
                connectionIRI, LinkedPipesOntology.CONNECTION_TARGET,
                SimpleValueFactory.getInstance().createIRI(targetComponent.getUri()),
                pipelineIRI));

        addStatement(SimpleValueFactory.getInstance().createStatement(
                connectionIRI, LinkedPipesOntology.CONNECTION_TARGET_BINDING,
                SimpleValueFactory.getInstance().createLiteral(targetComponent.getInputPort()),
                pipelineIRI));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
