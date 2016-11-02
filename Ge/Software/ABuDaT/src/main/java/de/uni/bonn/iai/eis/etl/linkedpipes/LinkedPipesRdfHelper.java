package de.uni.bonn.iai.eis.etl.linkedpipes;

import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.etl.component.Component;
import de.uni.bonn.iai.eis.etl.component.ComponentReader;
import de.uni.bonn.iai.eis.etl.component.Connection;
import org.apache.commons.io.IOUtils;
import org.openrdf.model.*;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.SKOS;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LinkedPipesRdfHelper {
    private LinkedPipesRdfHelper() {
    }

    private static ValueFactory factory = SimpleValueFactory.getInstance();

    /**
     * Create a linkedpipes pipeline label RDF statement
     *
     * @param label the label
     * @param uri   the uri of the pipeline
     * @return the rdf statement
     */
    private static Statement createPipelineLabelStatement(String label, String uri) {
        IRI pipelineIRI = factory.createIRI(uri);

        return factory.createStatement(
                pipelineIRI,
                SKOS.PREF_LABEL,
                factory.createLiteral(label, XMLSchema.STRING),
                pipelineIRI
        );
    }

    /**
     * Create a linkedpipes pipeline type RDF statement
     *
     * @param uri the uri of the pipeline
     * @return the rdf statement
     */
    private static Statement createPipelineTypeStatement(String uri) {
        IRI pipelineIRI = factory.createIRI(uri);

        return factory.createStatement(
                pipelineIRI,
                RDF.TYPE,
                LinkedPipesOntology.PIPELINE_TYPE,
                pipelineIRI
        );
    }

    /**
     * Create an RDF model (collection of statements) from a given linkedpipes pipeline.
     *
     * @param pipeline the linkedpipes pipeline
     * @return the rdf model
     */
    public static Model modelFromPipeline(Pipeline pipeline) {
        Model model = new LinkedHashModel();

        model.add(createPipelineTypeStatement(pipeline.getUri()));

        if (pipeline.hasLabel()) {
            model.add(createPipelineLabelStatement(pipeline.getLabel(), pipeline.getUri()));
        }

        for (Component component : pipeline.getComponents()) {
            model.addAll(component.getStatements());
        }

        for (Connection connection : pipeline.getConnections()) {
            model.addAll(connection.getStatements());
        }

        return model;
    }

    /**
     * Create a pipeline from a given json rdf model.
     *
     * @param pipelineJSON the pipeline in rdf json format
     * @return the pipeline object
     */
    public static Pipeline parsePipeline(String pipelineJSON) {
        Model model = modelFromJsonLd(pipelineJSON);
        return pipelineFromModel(model);
    }

    /**
     * Create a pipeline from a given RDF model.
     *
     * @param model the RDF model
     * @return the pipeline object
     */
    public static Pipeline pipelineFromModel(Model model) {
        //first find the Pipeline URI
        Resource pipelineURI = extractPipelineURI(model);
        String pipelineURIString = pipelineURI.stringValue();

        //create the pipeline
        Pipeline pipeline = new Pipeline();
        pipeline.setUri(pipelineURIString);
        pipeline.setId(pipelineURIString.substring(pipelineURIString.lastIndexOf('/') + 1));

        //find and set the pipelineLabel
        pipeline.setLabel(extractPipelineLabel(model, pipelineURI));

        //find and set the connections in the model and st
        pipeline.setConnections(getConnections(model, pipelineURI));

        //find all the components in the model
        List<Resource> componentURIs = extractComponentURIs(model, pipelineURI);
//        log.info(String.format("Found %d component(s)", componentURIs.size()));
        List<Component> components = new ArrayList<>();

        for (Resource componentURI : componentURIs) {
            Component component = ComponentReader.get(componentURI, model, pipelineURI);
            components.add(component);
        }

        //set the components
        pipeline.setComponents(components);

        return pipeline;
    }

    /**
     * Extract a list of linkedpipes component connections from a pipeline RDF model.
     *
     * @param model       the model
     * @param pipelineURI the uri of the pipeline
     * @return the list of connections found in the model
     */
    private static List<Connection> getConnections(Model model, Resource pipelineURI) {
        List<Resource> connectionsURIs = extractConnectionURIs(model, pipelineURI);
//        log.info(String.format("Found %d connection(s)", connectionsURIs.size()));
        List<Connection> connections = new ArrayList<>();

        for (Resource connectionURI : connectionsURIs) {
            Model connectionModel = model.filter(connectionURI, null, null, pipelineURI);

            Connection connection = new Connection();
            connection.setUri(connectionURI.toString());
            connection.addStatements(connectionModel);

            connections.add(connection);
        }
        return connections;
    }

    /**
     * Create a RDF model from a rdf in json ld format
     *
     * @param input the rdf triples in json ld format
     * @return the model
     */
    static Model modelFromJsonLd(String input) {
        try (InputStream inputStream = IOUtils.toInputStream(input, "UTF-8")) {
            return modelFromJsonLd(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create input stream.", e);
        }
    }

    /**
     * Create a RDF model from a rdf in json ld format
     *
     * @param inputStream  the rdf triples in json ld format
     * @return the model
     */
    public static Model modelFromJsonLd(InputStream inputStream) {
        Model model = new LinkedHashModel();

        RDFParser parser = Rio.createParser(RDFFormat.JSONLD);
        StatementCollector collector = new StatementCollector(model);
        parser.setRDFHandler(collector);

        try {
            parser.parse(inputStream, null);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse rdf data from stream", e);
        }

        return model;
    }

    /**
     * Extract the pipeline URI.
     *
     * @param model the RDF model to extract from
     * @return The URI of the pipeline.
     */
    private static Resource extractPipelineURI(Model model) {
        Model modelFilteredForLinkedPipesPipelineType = model.filter(null, RDF.TYPE, LinkedPipesOntology.PIPELINE_TYPE);

        if (modelFilteredForLinkedPipesPipelineType.size() != 1) {
            throw new RuntimeException("Expected to find exactly one pipeline URI in the given Model, but found: "
                    + modelFilteredForLinkedPipesPipelineType.size());
        }

        return modelFilteredForLinkedPipesPipelineType.subjects().iterator().next();
    }

    /**
     * Extract the pipeline label.
     *
     * @param model       the RDF model to extract from
     * @param pipelineURI the URI of the pipeline
     * @return The label of the pipeline.
     */
    private static String extractPipelineLabel(Model model, Resource pipelineURI) {
        Model modelFilteredForPipelineLabel = model.filter(pipelineURI, SKOS.PREF_LABEL, null, pipelineURI);
        Assert.isTrue(1 >= modelFilteredForPipelineLabel.objects().size());

        if (modelFilteredForPipelineLabel.size() == 1) {
            return modelFilteredForPipelineLabel.objects().iterator().next().stringValue();
        }

        return null;
    }

    /**
     * Extract all URIs representing component entities.
     *
     * @param model       the RDF model to extract from
     * @param pipelineURI the URI of the pipeline
     * @return A list of connection URI's
     */
    private static List<Resource> extractComponentURIs(Model model, Resource pipelineURI) {
        return extractComponents(model, pipelineURI).stream().map(Statement::getSubject).collect(Collectors.toList());
    }

    /**
     * Extract all RDF triples belonging to components.
     *
     * @param model       the RDF model to extract from
     * @param pipelineURI the URI of the pipeline
     * @return A model containing all the component related RDF Triples.
     */
    private static Model extractComponents(Model model, Resource pipelineURI) {
        return model.filter(null, RDF.TYPE, LinkedPipesOntology.COMPONENT_TYPE, pipelineURI);
    }

    /**
     * Extract all URIs representing connection entities.
     *
     * @param model       the RDF model to extract from
     * @param pipelineURI the URI of the pipeline
     * @return A list of connection URIs.
     */
    private static List<Resource> extractConnectionURIs(Model model, Resource pipelineURI) {
        return extractConnections(model, pipelineURI).stream().map(Statement::getSubject).collect(Collectors.toList());
    }

    /**
     * Extract all RDF Triples belonging to connections betweeen components.
     *
     * @param model       the RDF model to read from
     * @param pipelineURI the URI of the pipeline
     * @return A model containing all the connection related RDF Triples.
     */
    private static Model extractConnections(Model model, Resource pipelineURI) {
        return model.filter(null, RDF.TYPE, LinkedPipesOntology.CONNECTION_TYPE, pipelineURI);
    }

    /**
     * Function to transform an RDF model to string.
     *
     * @param model the model to transform
     * @return the rdf model in json-ld format
     */
    public static String modelToString(Model model) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Rio.write(model, byteArrayOutputStream, RDFFormat.JSONLD);
            return IOUtils.toString(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Unable to write model to string", e);
        }
    }

}
