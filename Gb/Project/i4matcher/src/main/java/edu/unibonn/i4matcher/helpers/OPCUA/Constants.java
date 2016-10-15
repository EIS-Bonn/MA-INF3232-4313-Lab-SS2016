package edu.unibonn.i4matcher.helpers.OPCUA;

public class Constants {

    public static final String DEFAULT_OBP_PREFIX = "has";
    public static final String DEFAULT_DTP_PREFIX = "dtp";

    public static final String DATATYPE_SUFFIX = "Datatype";
    public static final String MIXED_CLASS_DEFAULT_PROP_NAME = "textContent"; /// VALUE???

    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

    public static final String ONTMALIZER_BASE_URI = "https://w3id.org/i40#";
    public static final String ONTMALIZER_BASE_URI_PREFIX = "dtype";

    public static final String ONTMALIZER_INSTANCE_BASE_URI = "https://w3id.org/i40/instance#";
    public static final String ONTMALIZER_INSTANCE_BASE_NS = "https://w3id.org/i40//instance";
    public static final String ONTMALIZER_INSTANCE_NAME_PREFIX = "";

    public static final String RDFS_TYPE_URI = "http://www.w3.org/2000/01/rdf-schema#Datatype";
    public static final String OWL_CLASS_URI = "http://www.w3.org/2002/07/owl#Class";

    public static String ONTMALIZER_VALUE_PROP_NAME;
    public static String ONTMALIZER_ENUMERATION_CLASS_NAME;
    public static String ONTMALIZER_ENUMERATEDVALUE_CLASS_NAME;

    static {
        ONTMALIZER_VALUE_PROP_NAME = ONTMALIZER_BASE_URI + "hasValue";
        ONTMALIZER_ENUMERATION_CLASS_NAME = ONTMALIZER_BASE_URI + "Enumeration";
        ONTMALIZER_ENUMERATEDVALUE_CLASS_NAME = ONTMALIZER_BASE_URI + "EnumeratedValue";
    }

}
