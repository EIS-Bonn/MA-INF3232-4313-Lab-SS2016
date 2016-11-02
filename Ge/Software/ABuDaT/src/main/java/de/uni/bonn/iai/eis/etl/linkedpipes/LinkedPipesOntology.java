package de.uni.bonn.iai.eis.etl.linkedpipes;

import org.openrdf.model.IRI;
import org.openrdf.model.impl.SimpleValueFactory;

/**
 * The linkedpipes RDF ontology.
 */
public class LinkedPipesOntology {

    private LinkedPipesOntology() {
    }

    private static final SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();

    private static final String PIPELINE_TYPE_URL = "http://linkedpipes.com/ontology/Pipeline";
    public static final IRI PIPELINE_TYPE = valueFactory.createIRI(PIPELINE_TYPE_URL);

    private static final String COMPONENT_TYPE_URL = "http://linkedpipes.com/ontology/Component";
    public static final IRI COMPONENT_TYPE = valueFactory.createIRI(COMPONENT_TYPE_URL);

    private static final String CONNECTION_TYPE_URL = "http://linkedpipes.com/ontology/Connection";
    public static final IRI CONNECTION_TYPE = valueFactory.createIRI(CONNECTION_TYPE_URL);

    private static final String CONNECTION_SOURCE_URL = "http://linkedpipes.com/ontology/sourceComponent";
    public static final IRI CONNECTION_SOURCE = valueFactory.createIRI(CONNECTION_SOURCE_URL);

    private static final String CONNECTION_TARGET_URL = "http://linkedpipes.com/ontology/targetComponent";
    public static final IRI CONNECTION_TARGET = valueFactory.createIRI(CONNECTION_TARGET_URL);

    private static final String CONNECTION_SOURCE_BINDING_URL = "http://linkedpipes.com/ontology/sourceBinding";
    public static final IRI CONNECTION_SOURCE_BINDING = valueFactory.createIRI(CONNECTION_SOURCE_BINDING_URL);

    private static final String CONNECTION_TARGET_BINDING_URL = "http://linkedpipes.com/ontology/targetBinding";
    public static final IRI CONNECTION_TARGET_BINDING = valueFactory.createIRI(CONNECTION_TARGET_BINDING_URL);

    private static final String CONFIGURATION_GRAPH_TYPE_URL = "http://linkedpipes.com/ontology/configurationGraph";
    public static final IRI CONFIGURATION_GRAPH_TYPE = valueFactory.createIRI(CONFIGURATION_GRAPH_TYPE_URL);

    private static final String TEMPLATE_TYPE = "http://linkedpipes.com/ontology/template";
    static final IRI TEMPLATE_TYPE_URL = valueFactory.createIRI(TEMPLATE_TYPE);

    private static final String X_TYPE = "http://linkedpipes.com/ontology/x";
    static final IRI X_TYPE_URL = valueFactory.createIRI(X_TYPE);

    private static final String Y_TYPE = "http://linkedpipes.com/ontology/y";
    static final IRI Y_TYPE_URL = valueFactory.createIRI(Y_TYPE);

    private static final String ETL_EXECTUION_TYPE_URL = "http://etl.linkedpipes.com/ontology/Execution";
    static final IRI ETL_EXECTUION_TYPE = valueFactory.createIRI(ETL_EXECTUION_TYPE_URL);

    private static final String LINKEDPIPES_ID_URL = "http://linkedpipes.com/ontology/id";
    static final IRI LINKEDPIPES_ID = valueFactory.createIRI(LINKEDPIPES_ID_URL);

    private static final String PIPELINE_TYPE_IN_READ_PIPELINES_RESPONSE_URL = "http://linkedpipes.com/ontology/pipeline";
    static final IRI PIPELINE_TYPE_IN_READ_PIPELINES_RESPONSE = valueFactory.createIRI(PIPELINE_TYPE_IN_READ_PIPELINES_RESPONSE_URL);

    private static final String PIPELINE_TYPE_IN_EXECUTE_PIPELINES_RESPONSE_URL = "http://etl.linkedpipes.com/ontology/pipeline";
    static final IRI PIPELINE_TYPE_IN_EXECUTE_PIPELINES_RESPONSE = valueFactory.createIRI(PIPELINE_TYPE_IN_EXECUTE_PIPELINES_RESPONSE_URL);

    private static final String ETL_EXECUTION_STATUS_URL = "http://etl.linkedpipes.com/ontology/status";
    static final IRI ETL_EXECUTION_STATUS = valueFactory.createIRI(ETL_EXECUTION_STATUS_URL);
}
