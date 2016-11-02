package de.uni.bonn.iai.eis.etl.linkedpipes;

import org.openrdf.model.IRI;
import org.openrdf.model.Model;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.SKOS;

/**
 * Represents the general configuration of a linkedpipes component.
 *
 * The general configuration includes:
 *
 *  - The values for x- and y-axis.
 *  - The description
 *  - The preferred label
 *  - The template uri
 *  - The pipeline uri
 *  - The id of the component
 *  - The type, which is always http://linkedpipes.com/ontology/Component
 *
 */
public class GeneralConfiguration {
    private ValueFactory valueFactory =  SimpleValueFactory.getInstance();

    public IRI type = LinkedPipesOntology.COMPONENT_TYPE;

    public String prefLabel;
    public IRI id;
    public IRI template;
    public int x;
    public int y;
    public String description;

    public IRI pipeline;

    private Model model = new LinkedHashModel();

    /**
     * Getter for the RDF model (collection of rdf statements) representing the general configuration.
     * @return
     */
    public Model getModel() {
        model.add(valueFactory.createStatement(id, RDF.TYPE, type, pipeline));

        if (prefLabel == null){
            throw new RuntimeException("Components need to have a label");
        }
        model.add(valueFactory.createStatement(id, SKOS.PREF_LABEL, valueFactory.createLiteral(prefLabel), pipeline));
        if (description != null){
            model.add(valueFactory.createStatement(id, valueFactory.createIRI("http://purl.org/dc/terms/description"), valueFactory.createLiteral(description), pipeline));
        }

        model.add(valueFactory.createStatement(id, LinkedPipesOntology.TEMPLATE_TYPE_URL, template, pipeline));
        model.add(valueFactory.createStatement(id, LinkedPipesOntology.X_TYPE_URL, valueFactory.createLiteral(x), pipeline));
        model.add(valueFactory.createStatement(id, LinkedPipesOntology.Y_TYPE_URL, valueFactory.createLiteral(y), pipeline));

        return model;
    }
}
