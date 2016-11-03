package de.uni.bonn.iai.eis.rdf;

import org.openrdf.model.IRI;
import org.openrdf.model.impl.SimpleValueFactory;

public class LinkedDataCube {
    public static final String PREFIX = "qb";
    public static final String NAMESPACE = "http://purl.org/linked-data/cube#";

    public static final IRI DSD;
    public static final IRI COMPONENT;
    public static final IRI DIMENSION;
    public static final IRI DATASET_TYPE;
    public static final IRI COMPONENT_ATTACHMENT;
    public static final IRI ATTRIBUTE;
    public static final IRI COMPONENT_REQUIRED;
    public static final IRI MEASURE;
    public static final IRI STRUCTURE;
    public static final IRI CODED_PROPERTY;
    public static final IRI CODELIST;
    public static final IRI MEASURE_PROPERTY;
    private static final IRI DATASET_PROPERTY;

    private static final SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();

    static {
        DSD = valueFactory.createIRI(NAMESPACE, "DataStructureDefinition");
        COMPONENT = valueFactory.createIRI(NAMESPACE, "component");
        DIMENSION = valueFactory.createIRI(NAMESPACE, "dimension");
        DATASET_TYPE = valueFactory.createIRI(NAMESPACE, "DataSet");
        DATASET_PROPERTY = valueFactory.createIRI(NAMESPACE, "dataSet");
        COMPONENT_ATTACHMENT = valueFactory.createIRI(NAMESPACE, "componentAttachment");
        ATTRIBUTE = valueFactory.createIRI(NAMESPACE, "attribute");
        COMPONENT_REQUIRED = valueFactory.createIRI(NAMESPACE, "componentRequired");
        MEASURE = valueFactory.createIRI(NAMESPACE, "measure");
        STRUCTURE = valueFactory.createIRI(NAMESPACE, "structure");
        CODED_PROPERTY = valueFactory.createIRI(NAMESPACE, "CodedProperty");
        CODELIST = valueFactory.createIRI(NAMESPACE, "codeList");
        MEASURE_PROPERTY = valueFactory.createIRI(NAMESPACE, "MeasureProperty");
    }

}
